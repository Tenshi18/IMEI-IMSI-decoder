package com.tenshi18.imeiimsidecoder.settings.data.repository

import com.tenshi18.imeiimsidecoder.settings.data.local.SettingsLocalDataSource
import com.tenshi18.imeiimsidecoder.settings.domain.model.IMEIMode
import com.tenshi18.imeiimsidecoder.settings.domain.repository.SettingsRepository
import com.tenshi18.imeiimsidecoder.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val localDataSource: SettingsLocalDataSource
) : SettingsRepository {

    // Режим работы с IMEI (локальная БД или API)
    override val IMEIModeFlow: Flow<IMEIMode> = localDataSource.IMEIModeFlow
    override suspend fun setIMEIMode(mode: IMEIMode) {
        localDataSource.setIMEIMode(mode)
    }

    // Предупреждение при первом переключении в режим API
    override val hasShownAPIWarningFlow: Flow<Boolean> = localDataSource.hasShownAPIWarningFlow
    override suspend fun setAPIWarningShown() {
        localDataSource.setAPIWarningShown()
    }

    // Динамические цвета MD3
    override val useDynamicColoursFlow: Flow<Boolean>
        get() = localDataSource.useDynamicColoursFlow

    override suspend fun setDynamicColoursEnabled(enabled: Boolean) {
        localDataSource.setDynamicColoursEnabled(enabled)
    }

    // Системная/тёмная/светлая тема
    override val themeModeFlow: Flow<ThemeMode> = localDataSource.themeModeFlow
    override suspend fun setThemeMode(mode: ThemeMode) {
        localDataSource.setThemeMode(mode)
    }

}