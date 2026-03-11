package com.smartscheduler.app.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartscheduler.app.data.repository.FocusSessionRepository
import com.smartscheduler.app.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

data class StatisticsUiState(
    val weeklyFocusMinutes: Map<DayOfWeek, Int> = emptyMap(),
    val totalFocusMinutesThisWeek: Int = 0,
    val completedTodosThisWeek: Int = 0,
    val totalTodosThisWeek: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val focusSessionRepository: FocusSessionRepository,
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val startOfWeek = today.minusDays(today.dayOfWeek.value.toLong() - 1) // Monday
            val endOfWeek = startOfWeek.plusDays(6) // Sunday

            // 1. Load Focus Sessions
            focusSessionRepository.getSessionsByRange(startOfWeek, endOfWeek).collect { sessions ->
                val dailyMinutes = mutableMapOf<DayOfWeek, Int>()
                
                // Initialize all days of week to 0
                DayOfWeek.values().forEach { dailyMinutes[it] = 0 }
                
                var totalWeeklyMinutes = 0
                sessions.forEach { session ->
                    val day = session.date.dayOfWeek
                    dailyMinutes[day] = (dailyMinutes[day] ?: 0) + session.durationMinutes
                    totalWeeklyMinutes += session.durationMinutes
                }

                _uiState.value = _uiState.value.copy(
                    weeklyFocusMinutes = dailyMinutes,
                    totalFocusMinutesThisWeek = totalWeeklyMinutes,
                    isLoading = false
                )
            }
        }

        // 2. Load Todos — count ALL todos (not just this week's deadline)
        viewModelScope.launch {
            todoRepository.getAllTodos().collect { allTodos ->
                val completed = allTodos.count { it.isCompleted }
                
                _uiState.value = _uiState.value.copy(
                    completedTodosThisWeek = completed,
                    totalTodosThisWeek = allTodos.size
                )
            }
        }
    }
}
