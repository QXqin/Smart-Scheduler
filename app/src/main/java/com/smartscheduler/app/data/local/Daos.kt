package com.smartscheduler.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FixedEventDao {
    @Query("SELECT * FROM fixed_events ORDER BY date, startTime")
    fun getAll(): Flow<List<FixedEventEntity>>

    @Query("SELECT * FROM fixed_events WHERE date = :date ORDER BY startTime")
    fun getByDate(date: String): Flow<List<FixedEventEntity>>

    @Query("SELECT * FROM fixed_events WHERE date BETWEEN :startDate AND :endDate ORDER BY date, startTime")
    fun getByDateRange(startDate: String, endDate: String): Flow<List<FixedEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<FixedEventEntity>)

    @Delete
    suspend fun delete(event: FixedEventEntity)

    @Query("DELETE FROM fixed_events WHERE sourceFile = :sourceFile")
    suspend fun deleteBySource(sourceFile: String)

    @Query("DELETE FROM fixed_events")
    suspend fun deleteAll()
}

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos WHERE isCompleted = 0 ORDER BY deadlineDate, createdAt")
    fun getActiveTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos ORDER BY deadlineDate, createdAt")
    fun getAll(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getById(id: Long): TodoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity): Long

    @Update
    suspend fun update(todo: TodoEntity)

    @Delete
    suspend fun delete(todo: TodoEntity)

    @Query("DELETE FROM todos")
    suspend fun deleteAll()
}

@Dao
interface ScheduledBlockDao {
    @Query("SELECT * FROM scheduled_blocks WHERE date = :date ORDER BY startTime")
    fun getByDate(date: String): Flow<List<ScheduledBlockEntity>>

    @Query("SELECT * FROM scheduled_blocks WHERE date BETWEEN :startDate AND :endDate ORDER BY date, startTime")
    fun getByDateRange(startDate: String, endDate: String): Flow<List<ScheduledBlockEntity>>

    @Query("SELECT * FROM scheduled_blocks WHERE id = :id")
    suspend fun getById(id: Long): ScheduledBlockEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(blocks: List<ScheduledBlockEntity>)

    @Update
    suspend fun update(block: ScheduledBlockEntity)

    @Delete
    suspend fun delete(block: ScheduledBlockEntity)

    @Query("DELETE FROM scheduled_blocks")
    suspend fun deleteAll()

    @Query("DELETE FROM scheduled_blocks WHERE date BETWEEN :startDate AND :endDate")
    suspend fun deleteByDateRange(startDate: String, endDate: String)
}

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY date DESC, startTime DESC")
    fun getAll(): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE date = :date ORDER BY startTime")
    fun getByDate(date: String): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE date BETWEEN :startDate AND :endDate ORDER BY date, startTime")
    fun getByDateRange(startDate: String, endDate: String): Flow<List<FocusSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: FocusSessionEntity): Long

    @Delete
    suspend fun delete(session: FocusSessionEntity)

    @Query("DELETE FROM focus_sessions")
    suspend fun deleteAll()
}
