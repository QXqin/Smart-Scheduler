package com.smartscheduler.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.smartscheduler.app.domain.model.LlmSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "llm_settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val BASE_URL = stringPreferencesKey("base_url")
        val API_KEY = stringPreferencesKey("api_key")
        val MODEL_NAME = stringPreferencesKey("model_name")
        val CUSTOM_PROMPT = stringPreferencesKey("custom_prompt")
    }

    val settings: Flow<LlmSettings> = context.settingsDataStore.data.map { prefs ->
        LlmSettings(
            baseUrl = prefs[Keys.BASE_URL] ?: "https://api.deepseek.com",
            apiKey = prefs[Keys.API_KEY] ?: "",
            modelName = prefs[Keys.MODEL_NAME] ?: "deepseek-chat",
            customPrompt = prefs[Keys.CUSTOM_PROMPT] ?: ""
        )
    }

    suspend fun updateSettings(settings: LlmSettings) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.BASE_URL] = settings.baseUrl
            prefs[Keys.API_KEY] = settings.apiKey
            prefs[Keys.MODEL_NAME] = settings.modelName
            prefs[Keys.CUSTOM_PROMPT] = settings.customPrompt
        }
    }
}
