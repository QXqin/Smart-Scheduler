package com.smartscheduler.app.util

import biweekly.Biweekly
import biweekly.component.VEvent
import com.smartscheduler.app.domain.model.FixedEvent
import java.io.InputStream
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IcsParser @Inject constructor() {

    fun parse(inputStream: InputStream): List<FixedEvent> {
        val calendars = Biweekly.parse(inputStream).all()
        val events = mutableListOf<FixedEvent>()

        for (calendar in calendars) {
            for (event in calendar.events) {
                events.addAll(parseEvent(event))
            }
        }
        return events
    }

    fun parseFromText(icsText: String): List<FixedEvent> {
        val calendars = Biweekly.parse(icsText).all()
        val events = mutableListOf<FixedEvent>()

        for (calendar in calendars) {
            for (event in calendar.events) {
                events.addAll(parseEvent(event))
            }
        }
        return events
    }

    private fun parseEvent(event: VEvent): List<FixedEvent> {
        val summary = event.summary?.value ?: return emptyList()
        val dtStart = event.dateStart?.value ?: return emptyList()
        val dtEnd = event.dateEnd?.value
        
        val description = event.description?.value ?: ""
        val location = event.location?.value ?: ""
        val rruleStr = event.recurrenceRule?.value?.toString()

        val results = mutableListOf<FixedEvent>()
        val zoneId = ZoneId.systemDefault()
        
        // Calculate duration to apply to each occurrence
        val durationMillis = if (dtEnd != null) {
            dtEnd.time - dtStart.time
        } else {
            3600000L // Default 1 hour
        }

        // Use biweekly's DateIterator to expand RRULE
        val iterator = event.getDateIterator(java.util.TimeZone.getDefault())
        // Limit expansion to prevent infinite loops (e.g., max 2 years or 300 instances)
        val limitDate = java.util.Date(System.currentTimeMillis() + 2L * 365 * 24 * 60 * 60 * 1000)
        var count = 0

        while (iterator.hasNext() && count < 500) {
            val occurrenceStart = iterator.next()
            if (occurrenceStart.after(limitDate)) break
            
            // Fix: Use the iterator's timezone or system default more safely
            val occStartInstant = Instant.ofEpochMilli(occurrenceStart.time)
            val occStartZoned = occStartInstant.atZone(zoneId)
            val startDate = occStartZoned.toLocalDate()
            val startTime = occStartZoned.toLocalTime().truncatedTo(ChronoUnit.MINUTES)
            
            val occEndInstant = Instant.ofEpochMilli(occurrenceStart.time + durationMillis)
            val endTime = occEndInstant.atZone(zoneId).toLocalTime().truncatedTo(ChronoUnit.MINUTES)

            results.add(
                FixedEvent(
                    title = summary,
                    description = description,
                    location = location,
                    date = startDate,
                    startTime = startTime,
                    endTime = endTime,
                    recurrenceRule = rruleStr
                )
            )
            count++
        }

        return results
    }

    private fun java.util.Date.toInstant(): Instant? {
        return try {
            Instant.ofEpochMilli(this.time)
        } catch (e: Exception) {
            null
        }
    }
}
