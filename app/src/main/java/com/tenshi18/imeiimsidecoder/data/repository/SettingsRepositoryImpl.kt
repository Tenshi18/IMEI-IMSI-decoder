package com.tenshi18.imeiimsidecoder.data.repository

import com.tenshi18.imeiimsidecoder.data.local.SettingsLocalDataSource
import com.tenshi18.imeiimsidecoder.domain.repository.SettingsRepository
import com.tenshi18.imeiimsidecoder.presentation.theme.ThemeMode
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val localDataSource: SettingsLocalDataSource
) : SettingsRepository {

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