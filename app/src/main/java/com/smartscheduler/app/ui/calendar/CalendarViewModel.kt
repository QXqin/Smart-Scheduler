package com.smartscheduler.app.ui.calendar

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartscheduler.app.data.repository.EventRepository
import com.smartscheduler.app.data.repository.toTimelineEvent
import com.smartscheduler.app.domain.model.FixedEvent
import com.smartscheduler.app.domain.model.ScheduledBlock
import com.smartscheduler.app.domain.model.TimelineEvent
import com.smartscheduler.app.domain.usecase.GenerateScheduleUseCase
import com.smartscheduler.app.util.IcsParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val events: List<TimelineEvent> = emptyList(),
    val currentTime: LocalTime = LocalTime.now(),
    val isGenerating: Boolean = false,
    val message: String? = null,
    // ICS week offset dialog
    val showWeekOffsetDialog: Boolean = false,
    val pendingImportCount: Int = 0,
    // Block edit
    val editingBlock: ScheduledBlock? = null
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val application: Application,
    private val eventRepository: EventRepository,
    private val icsParser: IcsParser,
    private val generateScheduleUseCase: GenerateScheduleUseCase
) : AndroidViewModel(application) {

    // ICS import buffering
    private val _pendingEvents = MutableStateFlow<List<FixedEvent>>(emptyList())
    private var _pendingSource: String = ""

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    private val _isGenerating = MutableStateFlow(false)
    private val _message = MutableStateFlow<String?>(null)
    private val _showWeekOffsetDialog = MutableStateFlow(false)
    private val _editingBlock = MutableStateFlow<ScheduledBlock?>(null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CalendarUiState> = combine(
        _selectedDate,
        _selectedDate.flatMapLatest { date ->
            combine(
                eventRepository.getFixedEventsByDate(date),
                eventRepository.getScheduledBlocksByDate(date)
            ) { fixedEvents, scheduledBlocks ->
                val fixed = fixedEvents.map { it.toTimelineEvent() }
                val scheduled = scheduledBlocks.map { it.toTimelineEvent() }
                (fixed + scheduled).sortedBy { it.startTime }
            }
        },
        _isGenerating,
        _message,
        combine(_showWeekOffsetDialog, _pendingEvents) { show, pending -> show to pending.size },
        _editingBlock
    ) { args ->
        // args: [date, events, generating, msg, (showDialog, pendingCount), editingBlock]
        val date = args[0] as LocalDate
        @Suppress("UNCHECKED_CAST")
        val events = args[1] as List<TimelineEvent>
        val generating = args[2] as Boolean
        val msg = args[3] as String?
        @Suppress("UNCHECKED_CAST")
        val pair = args[4] as Pair<Boolean, Int>
        val (showDialog, pendingCount) = pair
        val editing = args[5] as ScheduledBlock?
        CalendarUiState(
            selectedDate = date,
            events = events,
            currentTime = LocalTime.now(),
            isGenerating = generating,
            message = msg,
            showWeekOffsetDialog = showDialog,
            pendingImportCount = pendingCount,
            editingBlock = editing
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CalendarUiState())

    // ── Date selection ────────────────────────────────────────────────────
    fun selectDate(date: LocalDate) { _selectedDate.value = date }

    // ── ICS Import ────────────────────────────────────────────────────────
    fun importIcs(uri: Uri) {
        viewModelScope.launch {
            try {
                val inputStream = application.contentResolver.openInputStream(uri)
                    ?: throw Exception("Cannot open file")
                val events = icsParser.parse(inputStream)
                inputStream.close()
                if (events.isEmpty()) { _message.value = "No events found in file"; return@launch }
                _pendingEvents.value = events
                _pendingSource = uri.toString()
                _showWeekOffsetDialog.value = true
            } catch (e: Exception) {
                _message.value = "Import failed: ${e.message}"
            }
        }
    }

    fun importIcsFromUrl(url: String) {
        viewModelScope.launch {
            try {
                val client = okhttp3.OkHttpClient()
                val request = okhttp3.Request.Builder().url(url).build()
                val body = client.newCall(request).execute().body?.string()
                    ?: throw Exception("Empty response")
                val events = icsParser.parseFromText(body)
                if (events.isEmpty()) { _message.value = "No events found at URL"; return@launch }
                _pendingEvents.value = events
                _pendingSource = url
                _showWeekOffsetDialog.value = true
            } catch (e: Exception) {
                _message.value = "Import failed: ${e.message}"
            }
        }
    }

    fun confirmImportWithWeekOffset(currentWeekNumber: Int) {
        viewModelScope.launch {
            val events = _pendingEvents.value
            if (events.isEmpty()) { _showWeekOffsetDialog.value = false; return@launch }
            val earliestDate = events.minOf { it.date }
            val icsWeek1Monday = earliestDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val todayMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val daysBetween = icsWeek1Monday.until(todayMonday, java.time.temporal.ChronoUnit.DAYS)
            val shiftDays = daysBetween - (currentWeekNumber - 1) * 7L
            val shifted = events.map { it.copy(date = it.date.plusDays(shiftDays)) }
            eventRepository.insertFixedEvents(shifted, _pendingSource)
            _pendingEvents.value = emptyList()
            _pendingSource = ""
            _showWeekOffsetDialog.value = false
            _message.value = "Imported ${shifted.size} events (shifted ${shiftDays}d)"
        }
    }

    fun cancelImport() {
        _pendingEvents.value = emptyList()
        _pendingSource = ""
        _showWeekOffsetDialog.value = false
    }

    // ── Schedule block editing ────────────────────────────────────────────
    fun startEditBlock(event: TimelineEvent) {
        viewModelScope.launch {
            val block = eventRepository.getScheduledBlockById(event.id) ?: return@launch
            _editingBlock.value = block
        }
    }

    fun saveBlock(block: ScheduledBlock) {
        viewModelScope.launch {
            eventRepository.updateScheduledBlock(block)
            _editingBlock.value = null
        }
    }

    fun deleteBlock(block: ScheduledBlock) {
        viewModelScope.launch {
            eventRepository.deleteScheduledBlock(block)
            _editingBlock.value = null
        }
    }

    fun dismissBlockEdit() { _editingBlock.value = null }

    // ── AI schedule generation ────────────────────────────────────────────
    fun generateSchedule() {
        viewModelScope.launch {
            _isGenerating.value = true
            _message.value = null
            generateScheduleUseCase().fold(
                onSuccess = { blocks -> _message.value = "Generated ${blocks.size} schedule blocks" },
                onFailure = { error -> _message.value = error.message ?: "Schedule generation failed" }
            )
            _isGenerating.value = false
        }
    }

    fun clearMessage() { _message.value = null }
}
