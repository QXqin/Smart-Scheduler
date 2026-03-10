package com.smartscheduler.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartscheduler.app.data.repository.EventRepository
import com.smartscheduler.app.data.repository.SettingsRepository
import com.smartscheduler.app.data.repository.TodoRepository
import com.smartscheduler.app.domain.model.LlmSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val eventRepository: EventRepository,
    private val todoRepository: TodoRepository
) : ViewModel() {

    val settings: StateFlow<LlmSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LlmSettings())

    fun saveSettings(settings: LlmSettings) {
        viewModelScope.launch {
            settingsRepository.updateSettings(settings)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            eventRepository.clearFixedEvents()
            eventRepository.clearScheduledBlocks()
            todoRepository.clearAll()
        }
    }
}
