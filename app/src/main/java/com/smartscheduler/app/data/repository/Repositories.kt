package com.smartscheduler.app.data.repository

import com.smartscheduler.app.data.local.FixedEventDao
import com.smartscheduler.app.data.local.ScheduledBlockDao
import com.smartscheduler.app.data.local.TodoDao
import com.smartscheduler.app.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val fixedEventDao: FixedEventDao,
    private val scheduledBlockDao: ScheduledBlockDao
) {
    fun getFixedEventsByDate(date: LocalDate): Flow<List<FixedEvent>> =
        fixedEventDao.getByDate(date.toString()).map { list -> list.map { it.toDomain() } }

    fun getFixedEventsByRange(start: LocalDate, end: LocalDate): Flow<List<FixedEvent>> =
        fixedEventDao.getByDateRange(start.toString(), end.toString())
            .map { list -> list.map { it.toDomain() } }

    fun getAllFixedEvents(): Flow<List<FixedEvent>> =
        fixedEventDao.getAll().map { list -> list.map { it.toDomain() } }

    suspend fun insertFixedEvents(events: List<FixedEvent>, sourceFile: String = "") {
        fixedEventDao.insertAll(events.map { it.toEntity(sourceFile) })
    }

    suspend fun clearFixedEvents() = fixedEventDao.deleteAll()

    fun getScheduledBlocksByDate(date: LocalDate): Flow<List<ScheduledBlock>> =
        scheduledBlockDao.getByDate(date.toString()).map { list -> list.map { it.toDomain() } }

    fun getScheduledBlocksByRange(start: LocalDate, end: LocalDate): Flow<List<ScheduledBlock>> =
        scheduledBlockDao.getByDateRange(start.toString(), end.toString())
            .map { list -> list.map { it.toDomain() } }

    suspend fun getScheduledBlockById(id: Long): ScheduledBlock? =
        scheduledBlockDao.getById(id)?.toDomain()

    suspend fun insertScheduledBlocks(blocks: List<ScheduledBlock>) {
        scheduledBlockDao.insertAll(blocks.map { it.toEntity() })
    }

    suspend fun updateScheduledBlock(block: ScheduledBlock) =
        scheduledBlockDao.update(block.toEntity())

    suspend fun deleteScheduledBlock(block: ScheduledBlock) =
        scheduledBlockDao.delete(block.toEntity())

    suspend fun clearScheduledBlocks() = scheduledBlockDao.deleteAll()

    suspend fun clearScheduledBlocksByRange(start: LocalDate, end: LocalDate) =
        scheduledBlockDao.deleteByDateRange(start.toString(), end.toString())
}

@Singleton
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {
    fun getActiveTodos(): Flow<List<Todo>> =
        todoDao.getActiveTodos().map { list -> list.map { it.toDomain() } }

    fun getAllTodos(): Flow<List<Todo>> =
        todoDao.getAll().map { list -> list.map { it.toDomain() } }

    suspend fun getTodoById(id: Long): Todo? = todoDao.getById(id)?.toDomain()

    suspend fun insertTodo(todo: Todo): Long = todoDao.insert(todo.toEntity())

    suspend fun updateTodo(todo: Todo) = todoDao.update(todo.toEntity())

    suspend fun deleteTodo(todo: Todo) = todoDao.delete(todo.toEntity())

    /**
     * For a daily todo: instead of marking it done, advance the deadline by 1 day.
     * This keeps it active for the next day.
     */
    suspend fun advanceDailyTodo(todo: Todo) =
        todoDao.update(todo.copy(deadlineDate = todo.deadlineDate.plusDays(1)).toEntity())

    suspend fun clearAll() = todoDao.deleteAll()
}

@Singleton
class FocusSessionRepository @Inject constructor(
    private val focusSessionDao: com.smartscheduler.app.data.local.FocusSessionDao
) {
    fun getAllSessions(): Flow<List<FocusSession>> =
        focusSessionDao.getAll().map { list -> list.map { it.toDomain() } }

    fun getSessionsByDate(date: LocalDate): Flow<List<FocusSession>> =
        focusSessionDao.getByDate(date.toString()).map { list -> list.map { it.toDomain() } }

    fun getSessionsByRange(start: LocalDate, end: LocalDate): Flow<List<FocusSession>> =
        focusSessionDao.getByDateRange(start.toString(), end.toString())
            .map { list -> list.map { it.toDomain() } }

    suspend fun insertSession(session: FocusSession): Long =
        focusSessionDao.insert(session.toEntity())

    suspend fun deleteSession(session: FocusSession) =
        focusSessionDao.delete(session.toEntity())

    suspend fun clearAll() = focusSessionDao.deleteAll()
}
