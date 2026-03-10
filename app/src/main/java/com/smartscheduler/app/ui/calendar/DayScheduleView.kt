package com.smartscheduler.app.ui.calendar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartscheduler.app.domain.model.EventType
import com.smartscheduler.app.domain.model.TimelineEvent
import com.smartscheduler.app.ui.theme.FixedEventColor
import com.smartscheduler.app.ui.theme.ScheduledEventColor
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val HOUR_HEIGHT: Dp = 72.dp
private val START_HOUR = 6    // Start display from 6 AM
private val END_HOUR = 24     // End display at midnight
private val TIME_LABEL_WIDTH = 52.dp
private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun DayScheduleView(
    events: List<TimelineEvent>,
    modifier: Modifier = Modifier,
    currentTime: LocalTime? = null,
    onEventClick: (TimelineEvent) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val hourCount = END_HOUR - START_HOUR
    val totalHeight = HOUR_HEIGHT * hourCount

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .verticalScroll(scrollState)
                .height(totalHeight)
                .fillMaxWidth()
        ) {
            TimeLabelsColumn(hourCount = hourCount)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                HourGridLines(hourCount = hourCount)

                if (currentTime != null) {
                    CurrentTimeIndicator(currentTime = currentTime)
                }

                events.forEach { event ->
                    EventBlock(
                        event = event,
                        onClick = if (event.type == EventType.SCHEDULED) {
                            { onEventClick(event) }
                        } else null
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeLabelsColumn(hourCount: Int) {
    Column(
        modifier = Modifier
            .width(TIME_LABEL_WIDTH)
            .fillMaxHeight()
    ) {
        for (i in 0 until hourCount) {
            val hour = START_HOUR + i
            Box(
                modifier = Modifier.height(HOUR_HEIGHT),
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    text = String.format("%02d:00", hour),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 8.dp, top = 2.dp),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun HourGridLines(hourCount: Int) {
    val lineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    Canvas(modifier = Modifier.fillMaxSize()) {
        val hourHeightPx = HOUR_HEIGHT.toPx()
        for (i in 0..hourCount) {
            val y = i * hourHeightPx
            drawLine(
                color = lineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f,
                pathEffect = if (i > 0) PathEffect.dashPathEffect(floatArrayOf(8f, 4f)) else null
            )
        }
    }
}

@Composable
private fun BoxScope.CurrentTimeIndicator(currentTime: LocalTime) {
    val minutesSinceStart = (currentTime.hour - START_HOUR) * 60 + currentTime.minute
    if (minutesSinceStart < 0) return
    val topOffset = (minutesSinceStart.toFloat() / 60f) * HOUR_HEIGHT.value

    val indicatorColor = Color(0xFFFF4444)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = topOffset.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(2.dp)) {
            drawLine(
                color = indicatorColor,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 2.5f
            )
        }
        Canvas(
            modifier = Modifier
                .offset(x = (-4).dp, y = (-4).dp)
                .size(8.dp)
        ) {
            drawCircle(color = indicatorColor)
        }
    }
}

@Composable
private fun BoxScope.EventBlock(event: TimelineEvent, onClick: (() -> Unit)?) {
    val startMinutes = (event.startTime.hour - START_HOUR) * 60 + event.startTime.minute
    val endMinutes = (event.endTime.hour - START_HOUR) * 60 + event.endTime.minute
    if (startMinutes < 0 && endMinutes < 0) return

    val topOffset = (maxOf(0, startMinutes).toFloat() / 60f) * HOUR_HEIGHT.value
    val blockHeight = ((endMinutes - maxOf(0, startMinutes)).toFloat() / 60f) * HOUR_HEIGHT.value

    val bgColor = when (event.type) {
        EventType.FIXED -> FixedEventColor
        EventType.SCHEDULED -> ScheduledEventColor
    }

    val clickModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else Modifier

    Column(
        modifier = Modifier
            .offset(y = topOffset.dp)
            .padding(horizontal = 4.dp, vertical = 1.dp)
            .fillMaxWidth()
            .height(maxOf(blockHeight, 28f).dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor.copy(alpha = 0.15f))
            .then(clickModifier)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(14.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(bgColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = event.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = bgColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${event.startTime.format(timeFormatter)}-${event.endTime.format(timeFormatter)}",
                style = MaterialTheme.typography.labelSmall,
                color = bgColor.copy(alpha = 0.7f)
            )
        }
        if (blockHeight > 44f && event.description.isNotBlank()) {
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 11.dp)
            )
        }
        if (blockHeight > 44f && event.location.isNotBlank()) {
            Text(
                text = event.location,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 11.dp)
            )
        }
    }
}
