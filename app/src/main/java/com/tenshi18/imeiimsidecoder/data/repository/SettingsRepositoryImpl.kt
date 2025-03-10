package com.tenshi18.imeiimsidecoder.data.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tenshi18.imeiimsidecoder.data.local.SettingsLocalDataSource
import com.tenshi18.imeiimsidecoder.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsRepositoryImpl(
    private val localDataSource: SettingsLocalDataSource
) : SettingsRepository {

    // Динамические цвета MD3
    override val useDynamicColoursFlow: Flow<Boolean>
        get() = localDataSource.useDynamicColoursFlow

    override suspend fun setDynamicColoursEnabled(enabled: Boolean) {
        localDataSource.setDynamicColoursEnabled(enabled)
    }


    // Тёмная тема
    override val isDarkThemeFlow: Flow<Boolean>
        get() = localDataSource.isDarkThemeFlow

    override suspend fun setDarkThemeEnabled(enabled: Boolean) {
        localDataSource.setDarkThemeEnabled(enabled)
    }
}