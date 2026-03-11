package com.smartscheduler.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartscheduler.app.data.repository.EventRepository
import com.smartscheduler.app.data.repository.TodoRepository
import com.smartscheduler.app.data.repository.toTimelineEvent
import com.smartscheduler.app.domain.model.TimelineEvent
import com.smartscheduler.app.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

data class DashboardUiState(
    val todayEvents: List<TimelineEvent> = emptyList(),
    val pendingTodos: List<Todo> = emptyList()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val todayFlow = flow {
        // Simple ticker to emit current date. In a real app we might want to observe midnight broadcasts.
        emit(LocalDate.now())
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        todayFlow.flatMapLatest { today ->
            combine(
                eventRepository.getFixedEventsByDate(today),
                eventRepository.getScheduledBlocksByDate(today)
            ) { fixed, scheduled ->
                (fixed.map { it.toTimelineEvent() } + scheduled.map { it.toTimelineEvent() })
                    .sortedBy { it.startTime }
            }
        },
        todoRepository.getActiveTodos()
    ) { events, todos ->
        DashboardUiState(
            todayEvents = events,
            pendingTodos = todos.sortedBy { it.deadlineDate }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState())
}
