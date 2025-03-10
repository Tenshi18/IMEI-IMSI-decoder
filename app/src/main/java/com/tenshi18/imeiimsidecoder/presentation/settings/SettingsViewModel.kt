package com.tenshi18.imeiimsidecoder.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tenshi18.imeiimsidecoder.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    // Динамические цвета MD3
    val useDynamicColoursFlow = settingsRepository.useDynamicColoursFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun setDynamicColoursEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDynamicColoursEnabled(enabled)
        }
    }

    // Тёмная тема
    val isDarkThemeFlow = settingsRepository.isDarkThemeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false // пока не загрузились реальные данные
        )

    fun setDarkThemeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkThemeEnabled(enabled)
        }
    }
}
