package com.tenshi18.imeiimsidecoder.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tenshi18.imeiimsidecoder.settings.domain.model.IMEIMode
import com.tenshi18.imeiimsidecoder.settings.domain.repository.SettingsRepository
import com.tenshi18.imeiimsidecoder.ui.theme.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    // Режим работы с IMEI (локальная БД/API)
    val IMEIModeFlow = settingsRepository.IMEIModeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = IMEIMode.LOCAL
        )

    fun setIMEIMode(mode: IMEIMode) {
        viewModelScope.launch {
            settingsRepository.setIMEIMode(mode)
        }
    }

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

    // Системная/тёмная/светлая тема
    val themeModeFlow = settingsRepository.themeModeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeMode.SYSTEM
        )

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(mode)
        }
    }
}
