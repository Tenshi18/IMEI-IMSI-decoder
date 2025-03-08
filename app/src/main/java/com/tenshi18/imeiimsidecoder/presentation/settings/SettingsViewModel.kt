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

    // Подписываемся на Flow<Boolean> из репозитория и превращаем в StateFlow, чтобы удобно использовать в Compose
    val useDynamicColoursFlow = settingsRepository.useDynamicColoursFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true // пока не загрузились реальные данные
        )

    // Метод для обновления настройки
    fun setDynamicColoursEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDynamicColoursEnabled(enabled)
        }
    }
}
