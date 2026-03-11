package com.smartscheduler.app.domain.model

import com.smartscheduler.app.data.local.FocusSessionEntity
import java.time.LocalDate
import java.time.LocalTime

data class FocusSession(
    val id: Long = 0,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val durationMinutes: Int,
    val completed: Boolean
) {
    fun toEntity() = FocusSessionEntity(
        id = id,
        date = date.toString(),
        startTime = startTime.toString(),
        endTime = endTime.toString(),
        durationMinutes = durationMinutes,
        completed = completed
    )
}

fun FocusSessionEntity.toDomain() = FocusSession(
    id = id,
    date = LocalDate.parse(date),
    startTime = LocalTime.parse(startTime),
    endTime = LocalTime.parse(endTime),
    durationMinutes = durationMinutes,
    completed = completed
)
