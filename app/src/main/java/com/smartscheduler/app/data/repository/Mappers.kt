package com.smartscheduler.app.data.repository

import com.smartscheduler.app.data.local.*
import com.smartscheduler.app.domain.model.*
import java.time.LocalDate
import java.time.LocalTime

// FixedEventEntity <-> FixedEvent
fun FixedEventEntity.toDomain() = FixedEvent(
    id = id,
    title = title,
    description = description,
    location = location,
    date = LocalDate.parse(date),
    startTime = LocalTime.parse(startTime),
    endTime = LocalTime.parse(endTime),
    recurrenceRule = recurrenceRule
)

fun FixedEvent.toEntity(sourceFile: String = "") = FixedEventEntity(
    id = id,
    title = title,
    description = description,
    location = location,
    date = date.toString(),
    startTime = startTime.toString(),
    endTime = endTime.toString(),
    recurrenceRule = recurrenceRule,
    sourceFile = sourceFile
)

// TodoEntity <-> Todo
fun TodoEntity.toDomain() = Todo(
    id = id,
    title = title,
    description = description,
    deadlineDate = LocalDate.parse(deadlineDate),
    estimatedMinutes = estimatedMinutes,
    isCompleted = isCompleted,
    isDaily = isDaily,
    createdAt = createdAt
)

fun Todo.toEntity() = TodoEntity(
    id = id,
    title = title,
    description = description,
    deadlineDate = deadlineDate.toString(),
    estimatedMinutes = estimatedMinutes,
    isCompleted = isCompleted,
    isDaily = isDaily,
    createdAt = createdAt
)

// ScheduledBlockEntity <-> ScheduledBlock
fun ScheduledBlockEntity.toDomain() = ScheduledBlock(
    id = id,
    todoId = todoId,
    title = title,
    date = LocalDate.parse(date),
    startTime = LocalTime.parse(startTime),
    endTime = LocalTime.parse(endTime),
    note = note,
    generatedAt = generatedAt
)

fun ScheduledBlock.toEntity() = ScheduledBlockEntity(
    id = id,
    todoId = todoId,
    title = title,
    date = date.toString(),
    startTime = startTime.toString(),
    endTime = endTime.toString(),
    note = note,
    generatedAt = generatedAt
)

// Convert to unified TimelineEvent
fun FixedEvent.toTimelineEvent() = TimelineEvent(
    id = id,
    title = title,
    date = date,
    startTime = startTime,
    endTime = endTime,
    type = EventType.FIXED,
    description = description,
    location = location
)

fun ScheduledBlock.toTimelineEvent() = TimelineEvent(
    id = id,
    title = title,
    date = date,
    startTime = startTime,
    endTime = endTime,
    type = EventType.SCHEDULED,
    description = note
)
