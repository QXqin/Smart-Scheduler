package com.smartscheduler.app.ui.focus

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartscheduler.app.domain.timer.FocusTimerManager
import com.smartscheduler.app.domain.timer.TimerState
import com.smartscheduler.app.service.FocusTimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FocusTimerViewModel @Inject constructor(
    application: Application,
    private val timerManager: FocusTimerManager
) : AndroidViewModel(application) {

    val uiState: StateFlow<TimerState> = timerManager.timerState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TimerState()
        )

    fun setTimerMinutes(minutes: Int) {
        timerManager.setTimer(minutes * 60 * 1000L)
    }

    fun startTimer() {
        if (uiState.value.isRunning) return
        
        // Start Foreground Service
        val intent = Intent(getApplication(), FocusTimerService::class.java).apply {
            action = FocusTimerService.ACTION_START
        }
        getApplication<Application>().startService(intent) // or startForegroundService on SDK >= 26
        
        // Start Timer Logic
        timerManager.startTimer()
    }

    fun pauseTimer() {
        timerManager.pauseTimer()
    }

    fun stopTimerEarly() {
        val intent = Intent(getApplication(), FocusTimerService::class.java).apply {
            action = FocusTimerService.ACTION_STOP
        }
        getApplication<Application>().startService(intent)
        
        timerManager.stopTimerEarly()
    }
}
