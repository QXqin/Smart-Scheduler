package com.smartscheduler.app.data.repository

import com.smartscheduler.app.data.remote.*
import com.smartscheduler.app.domain.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LlmRepository @Inject constructor(
    private val moshi: Moshi
) {
    private fun buildService(baseUrl: String): LlmApiService {
        val url = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        val finalUrl = if (url.endsWith("/v1/")) url
            else if (url.endsWith("/")) "${url}v1/"
            else "${url}/v1/"

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(150, TimeUnit.SECONDS)
            .writeTimeout(150, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(finalUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(LlmApiService::class.java)
    }

    suspend fun generateSchedule(
        settings: LlmSettings,
        fixedEvents: List<FixedEvent>,
        todos: List<Todo>,
        weekStartDate: LocalDate
    ): List<ScheduledBlock> {
        val service = buildService(settings.baseUrl)
        val today = LocalDate.now()
        val systemPrompt = buildSystemPrompt(today)
        val userPrompt = buildUserPrompt(fixedEvents, todos, settings.customPrompt, weekStartDate, today)

        val request = ChatRequest(
            model = settings.modelName,
            messages = listOf(
                ChatMessage(role = "system", content = systemPrompt),
                ChatMessage(role = "user", content = userPrompt)
            ),
            temperature = 0.3
        )

        val response = service.chatCompletion(
            auth = "Bearer ${settings.apiKey}",
            request = request
        )

        val content = response.choices.firstOrNull()?.message?.content ?: return emptyList()
        return parseScheduleResponse(content).filter { !it.date.isBefore(today) }
    }

    private fun buildSystemPrompt(today: LocalDate): String = """
You are an elite Time Management Planner and Executive Secretary. Today is ${today}. 
Given a list of fixed events (classes, meetings, etc.) and to-do tasks, generate a weekly schedule that maximizes productivity while protecting the user's well-being.
CRITICAL: You MUST act as a human-centric planner. DO NOT blindly fill every empty hour with tasks.

Follow these strict rules:
1. NEVER overlap with fixed events.
2. NEVER schedule anything before today (${today}).
3. REST BLOCKS: You MUST reserve absolute protected time for daily life:
   - Lunch & Rest: Reserve at least 1.5 - 2 hours between 11:30 and 14:00 every day.
   - Dinner & Relaxation: Reserve at least 1.5 - 2 hours between 17:30 and 20:00 every day.
   - Commute/Buffer: If an offline fixed event exists, leave at least 30-45 minutes of travel/buffer time before and after it.
4. Breaks large tasks into reasonable time blocks (max 1.5 - 2 hours per block).
5. Leaves at least 20-30 minutes of unstructured breathing room between consecutive scheduled task blocks.
6. Core Working Hours: Only schedule tasks between 09:00 - 22:00. DO NOT schedule heavy tasks late at night unless the user explicitly asks.
7. Prioritizes tasks closer to their deadlines.
8. When a task has UNKNOWN duration, estimate it yourself based on the task title and description. Use realistic estimates (e.g. "Read 30 pages" ≈ 60 min, "Write essay" ≈ 90-120 min).

Respond with ONLY a JSON object in this exact format:
{
  "schedule": [
    {
      "todo_id": 123,
      "title": "Task title",
      "date": "2026-03-10",
      "start_time": "14:00",
      "end_time": "15:30",
      "note": "Optional note about this block"
    }
  ]
}

Rules:
- todo_id: The ID of the task from the provided TODO list. MANDATORY.
- date format: YYYY-MM-DD
- time format: HH:MM (24-hour)
- All scheduled blocks must be within the given week AND on or after today (${today})
- Do NOT include fixed events in the output, only new scheduled blocks for todos
- If a task needs multiple blocks, use the same todo_id for all of them.
- Be realistic with task durations. If a task is "unknown", use your best judgment.
""".trimIndent()

    private fun buildUserPrompt(
        fixedEvents: List<FixedEvent>,
        todos: List<Todo>,
        customPrompt: String,
        weekStartDate: LocalDate,
        today: LocalDate
    ): String {
        val sb = StringBuilder()
        val weekEndDate = weekStartDate.plusDays(6)
        val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        sb.appendLine("=== TODAY: ${today.format(dateFormatter)} ===")
        sb.appendLine("=== WEEK: ${weekStartDate.format(dateFormatter)} to ${weekEndDate.format(dateFormatter)} ===")
        sb.appendLine("IMPORTANT: Only schedule on or after ${today.format(dateFormatter)}!")
        sb.appendLine()

        sb.appendLine("=== FIXED EVENTS (do NOT schedule over these) ===")
        if (fixedEvents.isEmpty()) {
            sb.appendLine("No fixed events.")
        } else {
            for (event in fixedEvents) {
                sb.appendLine("- ${event.date.format(dateFormatter)} ${event.startTime.format(timeFormatter)}-${event.endTime.format(timeFormatter)}: ${event.title}")
                if (event.location.isNotBlank()) sb.appendLine("  Location: ${event.location}")
            }
        }
        sb.appendLine()

        sb.appendLine("=== TODO TASKS (schedule these) ===")
        if (todos.isEmpty()) {
            sb.appendLine("No tasks to schedule.")
        } else {
            for (todo in todos) {
                val durationStr = if (todo.estimatedMinutes > 0) {
                    val h = todo.estimatedMinutes / 60
                    val m = todo.estimatedMinutes % 60
                    if (h > 0 && m > 0) "${h}h${m}m" else if (h > 0) "${h}h" else "${m}m"
                } else {
                    "unknown (please estimate based on task title)"
                }
                val dailyTag = if (todo.isDaily) " [DAILY RECURRING]" else ""
                sb.appendLine("- ID: ${todo.id} | \"${todo.title}\"$dailyTag | Deadline: ${todo.deadlineDate.format(dateFormatter)} | Est. duration: $durationStr")
                if (todo.description.isNotBlank()) sb.appendLine("  Details: ${todo.description}")
            }
        }
        sb.appendLine()

        if (customPrompt.isNotBlank()) {
            sb.appendLine("=== USER PREFERENCES ===")
            sb.appendLine(customPrompt)
            sb.appendLine()
        }

        sb.appendLine("Generate the optimal schedule for these tasks within this week (on or after today ${today.format(dateFormatter)}), avoiding all fixed events.")
        return sb.toString()
    }

    private fun parseScheduleResponse(jsonString: String): List<ScheduledBlock> {
        return try {
            val cleanJson = jsonString
                .replace(Regex("```json\\s*"), "")
                .replace(Regex("```\\s*"), "")
                .trim()

            val type = Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
            val adapter = moshi.adapter<Map<String, Any>>(type)
            val map = adapter.fromJson(cleanJson) ?: return emptyList()

            @Suppress("UNCHECKED_CAST")
            val schedule = map["schedule"] as? List<Map<String, Any>> ?: return emptyList()

            schedule.mapNotNull { block ->
                try {
                    val todoId = (block["todo_id"] as? Number)?.toLong() ?: (block["todo_id"] as? String)?.toLongOrNull()
                    val title = block["title"] as? String ?: return@mapNotNull null
                    val date = block["date"] as? String ?: return@mapNotNull null
                    val startTime = block["start_time"] as? String ?: return@mapNotNull null
                    val endTime = block["end_time"] as? String ?: return@mapNotNull null
                    val note = block["note"] as? String ?: ""

                    ScheduledBlock(
                        todoId = todoId,
                        title = title,
                        date = LocalDate.parse(date),
                        startTime = LocalTime.parse(startTime),
                        endTime = LocalTime.parse(endTime),
                        note = note
                    )
                } catch (e: Exception) { null }
            }
        } catch (e: Exception) { emptyList() }
    }
}
