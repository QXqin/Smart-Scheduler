package com.smartscheduler.app.domain.timer

import com.smartscheduler.app.data.repository.FocusSessionRepository
import com.smartscheduler.app.domain.model.FocusSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

data class TimerState(
    val isRunning: Boolean = false,
    val timeRemainingMillis: Long = 0L,
    val totalTimeMillis: Long = 0L,
    val progress: Float = 1f
)

@Singleton
class FocusTimerManager @Inject constructor(
    private val focusSessionRepository: FocusSessionRepository
) {
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null
    private var startTime: LocalTime? = null
    private var sessionDate: LocalDate? = null

    init {
        // Default to a 25-minute Pomodoro out of the box
        setTimer(25 * 60 * 1000L)
    }

    fun setTimer(durationMillis: Long) {
        if (_timerState.value.isRunning) return
        _timerState.update {
            it.copy(
                timeRemainingMillis = durationMillis,
                totalTimeMillis = durationMillis,
                progress = 1f
            )
        }
    }

    fun startTimer() {
        if (_timerState.value.isRunning || _timerState.value.timeRemainingMillis <= 0) return
        
        if (startTime == null) {
            startTime = LocalTime.now()
            sessionDate = LocalDate.now()
        }

        _timerState.update { it.copy(isRunning = true) }
        
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (_timerState.value.timeRemainingMillis > 0 && _timerState.value.isRunning) {
                delay(1000L) // tick every second
                _timerState.update { current ->
                    val newTime = current.timeRemainingMillis - 1000L
                    val prog = if (current.totalTimeMillis > 0) {
                        newTime.toFloat() / current.totalTimeMillis.toFloat()
                    } else 0f
                    
                    current.copy(
                        timeRemainingMillis = newTime.coerceAtLeast(0L),
                        progress = prog
                    )
                }
            }

            if (_timerState.value.timeRemainingMillis <= 0L) {
                // Done
                finishTimer(completed = true)
            }
        }
    }

    fun pauseTimer() {
        _timerState.update { it.copy(isRunning = false) }
        timerJob?.cancel()
    }

    fun stopTimerEarly() {
        pauseTimer()
        finishTimer(completed = false)
    }

    private fun finishTimer(completed: Boolean) {
        // IMPORTANT: Capture totalTimeMillis BEFORE resetting state
        val totalMinutes = (_timerState.value.totalTimeMillis / (60 * 1000)).toInt()
        val endTime = LocalTime.now()

        // Save to Room DB
        if (startTime != null && sessionDate != null && totalMinutes > 0) {
            val actualMinutes = if (completed) {
                totalMinutes
            } else {
                // If stopped early, calculate actual focused time
                val elapsedMillis = _timerState.value.totalTimeMillis - _timerState.value.timeRemainingMillis
                (elapsedMillis / (60 * 1000)).toInt().coerceAtLeast(1)
            }

            val session = FocusSession(
                date = sessionDate!!,
                startTime = startTime!!,
                endTime = endTime,
                durationMinutes = actualMinutes,
                completed = completed
            )
            CoroutineScope(Dispatchers.IO).launch {
                focusSessionRepository.insertSession(session)
            }
        }

        // Reset state for next use
        startTime = null
        sessionDate = null

        // Reset timer to default 25 minutes for next session
        _timerState.update {
            TimerState(
                isRunning = false,
                timeRemainingMillis = 25 * 60 * 1000L,
                totalTimeMillis = 25 * 60 * 1000L,
                progress = 1f
            )
        }
    }
}
