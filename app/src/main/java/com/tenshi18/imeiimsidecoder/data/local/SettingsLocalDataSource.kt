package com.tenshi18.imeiimsidecoder.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val SETTINGS_DATASTORE_NAME = "user_settings"

private val Context.settingsDataStore by preferencesDataStore(
    name = SETTINGS_DATASTORE_NAME
)

class SettingsLocalDataSource(
    private val context: Context
) {
    // Ключ для настройки "использовать динамические цвета MD3"
    private val USE_DYNAMIC_COLOURS_KEY = booleanPreferencesKey("use_dynamic_colours")

    val useDynamicColoursFlow: Flow<Boolean> = context.settingsDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }

        .map { preferences ->
            preferences[USE_DYNAMIC_COLOURS_KEY] ?: true
        }

    suspend fun setDynamicColoursEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[USE_DYNAMIC_COLOURS_KEY] = enabled
        }
    }

    // Ключ для настройки "тёмная тема"
    private val IS_DARK_THEME_KEY = booleanPreferencesKey("is_dark_theme")
    val isDarkThemeFlow: Flow<Boolean> = context.settingsDataStore.data
        .map { preferences ->
            preferences[IS_DARK_THEME_KEY] ?: false
        }

    suspend fun setDarkThemeEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[IS_DARK_THEME_KEY] = enabled
        }
    }

}