package com.smartscheduler.app.domain.usecase

import com.smartscheduler.app.data.repository.*
import com.smartscheduler.app.domain.model.ScheduledBlock
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class GenerateScheduleUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val todoRepository: TodoRepository,
    private val llmRepository: LlmRepository,
    private val settingsRepository: SettingsRepository
) {
    /**
     * Generate a weekly schedule starting from [weekStartDate].
     * If null, uses the Monday of the current week.
     */
    suspend operator fun invoke(weekStartDate: LocalDate? = null): Result<List<ScheduledBlock>> {
        return try {
            val settings = settingsRepository.settings.first()
            if (settings.apiKey.isBlank()) {
                return Result.failure(IllegalStateException("未配置 API Key。请前往设置页面进行配置。"))
            }

            val monday = weekStartDate
                ?: LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val sunday = monday.plusDays(6)

            // Collect fixed events for the week
            val fixedEvents = eventRepository.getFixedEventsByRange(monday, sunday).first()

            // Collect active (incomplete) todos
            val todos = todoRepository.getActiveTodos().first()

            if (todos.isEmpty()) {
                return Result.failure(IllegalStateException("没有可排程的任务。请先添加待办事项。"))
            }

            // Call LLM
            val blocks = llmRepository.generateSchedule(settings, fixedEvents, todos, monday)

            // Clear old generated schedule for this week and save new
            eventRepository.clearScheduledBlocksByRange(monday, sunday)
            eventRepository.insertScheduledBlocks(blocks)

            // Duration Backfilling: Update Todo estimatedMinutes if it was 0
            val blocksByTodo = blocks.filter { it.todoId != null }.groupBy { it.todoId }
            for (todo in todos) {
                if (todo.estimatedMinutes == 0) {
                    val taskBlocks = blocksByTodo[todo.id] ?: emptyList()
                    if (taskBlocks.isNotEmpty()) {
                        val totalMinutes = taskBlocks.sumOf { block ->
                            java.time.Duration.between(block.startTime, block.endTime).toMinutes().toInt()
                        }
                        if (totalMinutes > 0) {
                            todoRepository.updateTodo(todo.copy(estimatedMinutes = totalMinutes))
                        }
                    }
                }
            }

            Result.success(blocks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
