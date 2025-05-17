package com.tenshi18.imeiimsidecoder.settings.domain.repository

import com.tenshi18.imeiimsidecoder.settings.domain.model.IMEIMode
import com.tenshi18.imeiimsidecoder.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    // Режим работы с IMEI (локальная БД или API)
    val IMEIModeFlow: Flow<IMEIMode>
    suspend fun setIMEIMode(mode: IMEIMode)

    // Предупреждение при первом переключении в режим API
    val hasShownAPIWarningFlow: Flow<Boolean>
    suspend fun setAPIWarningShown()

    // Динамические цвета MD3
    val useDynamicColoursFlow: Flow<Boolean>
    suspend fun setDynamicColoursEnabled(enabled: Boolean)

    // Системная/тёмная/светлая тема
    val themeModeFlow: Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
}