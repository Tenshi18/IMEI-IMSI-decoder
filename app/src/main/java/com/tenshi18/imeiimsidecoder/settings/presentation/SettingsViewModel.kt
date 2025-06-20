package com.tenshi18.imeiimsidecoder.settings.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tenshi18.imeiimsidecoder.history.domain.repository.HistoryRepository
import com.tenshi18.imeiimsidecoder.settings.domain.model.IMEIMode
import com.tenshi18.imeiimsidecoder.settings.domain.repository.SettingsRepository
import com.tenshi18.imeiimsidecoder.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val historyRepository: HistoryRepository
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

    // Предупреждение при первом переключении в режим API
    val hasShownAPIWarningFlow = settingsRepository.hasShownAPIWarningFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    fun setAPIWarningShown() {
        viewModelScope.launch {
            settingsRepository.setAPIWarningShown()
        }
    }
    fun setIMEIModeWithWarning(newMode: IMEIMode) {
        viewModelScope.launch {
            // Если переключаем на API и ещё не показывали — отложить смену режима
            val alreadyShown = hasShownAPIWarningFlow.first()
            if (newMode == IMEIMode.API && !alreadyShown) {
                _showAPIWarningEvent.emit(Unit)
            } else {
                settingsRepository.setIMEIMode(newMode)
            }
        }
    }
    // Канал для UI-события
    private val _showAPIWarningEvent = MutableSharedFlow<Unit>()
    val showAPIWarningEvent = _showAPIWarningEvent.asSharedFlow()

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

    fun exportHistoryToCsv(context: Context, uri: Uri) {
        viewModelScope.launch {
            val history = historyRepository.getAllHistoryItems()
            historyRepository.exportHistoryToCsv(context, uri, history)
        }
    }

    fun importHistoryFromCsv(context: Context, uri: Uri) {
        viewModelScope.launch {
            val importedItems = historyRepository.importHistoryFromCsv(context, uri)
            if (importedItems.isNotEmpty()) {
                historyRepository.saveImportedHistoryItems(importedItems)
            }
        }
    }

}
