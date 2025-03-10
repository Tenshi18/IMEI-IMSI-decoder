package com.tenshi18.imeiimsidecoder.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    // Динамические цвета MD3
    val useDynamicColoursFlow: Flow<Boolean>
    suspend fun setDynamicColoursEnabled(enabled: Boolean)

    // Тёмная тема
    val isDarkThemeFlow: Flow<Boolean>
    suspend fun setDarkThemeEnabled(enabled: Boolean)
}