package com.tenshi18.imeiimsidecoder.domain.repository

import com.tenshi18.imeiimsidecoder.presentation.theme.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    // Динамические цвета MD3
    val useDynamicColoursFlow: Flow<Boolean>
    suspend fun setDynamicColoursEnabled(enabled: Boolean)

    // Системная/тёмная/светлая тема
    val themeModeFlow: Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
}