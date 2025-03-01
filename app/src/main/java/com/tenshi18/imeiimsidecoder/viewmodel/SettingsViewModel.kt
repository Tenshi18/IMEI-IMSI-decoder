package com.tenshi18.imeiimsidecoder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private val _dynamicThemeEnabled = MutableStateFlow(true) // По умолчанию
    val dynamicThemeEnabled = _dynamicThemeEnabled.asStateFlow()

    fun setDynamicThemeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _dynamicThemeEnabled.value = enabled

            // Тут сохранение в DataStore или SharedPreferences

        }
    }
}