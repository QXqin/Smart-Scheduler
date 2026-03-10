package com.smartscheduler.app.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fixed_events")
data class FixedEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val location: String = "",
    val date: String,        // ISO LocalDate "2026-03-10"
    val startTime: String,   // ISO LocalTime "08:30"
    val endTime: String,     // ISO LocalTime "10:00"
    val recurrenceRule: String? = null,
    val sourceFile: String = ""
)

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val deadlineDate: String,      // ISO LocalDate
    val estimatedMinutes: Int,
    val isCompleted: Boolean = false,
    @ColumnInfo(defaultValue = "0")
    val isDaily: Boolean = false,  // repeat every day
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "scheduled_blocks")
data class ScheduledBlockEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val todoId: Long? = null,
    val title: String,
    val date: String,        // ISO LocalDate
    val startTime: String,   // ISO LocalTime
    val endTime: String,     // ISO LocalTime
    val note: String = "",
    val generatedAt: Long = System.currentTimeMillis()
)
