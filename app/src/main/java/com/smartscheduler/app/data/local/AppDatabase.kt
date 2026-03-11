package com.smartscheduler.app.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FixedEventEntity::class, TodoEntity::class, ScheduledBlockEntity::class, FocusSessionEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fixedEventDao(): FixedEventDao
    abstract fun todoDao(): TodoDao
    abstract fun scheduledBlockDao(): ScheduledBlockDao
    abstract fun focusSessionDao(): FocusSessionDao
}
