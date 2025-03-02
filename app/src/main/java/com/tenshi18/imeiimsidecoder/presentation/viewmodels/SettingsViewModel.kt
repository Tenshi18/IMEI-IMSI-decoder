package com.tenshi18.imeiimsidecoder.presentation.viewmodels

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val SETTINGS_DATASTORE_NAME = "settings"

private val Application.settingsDataStore by preferencesDataStore(
    name = SETTINGS_DATASTORE_NAME
)

class SettingsViewModel (application: Application) : AndroidViewModel(application) {

    private val _dynamicThemeEnabled = MutableStateFlow(true) // По умолчанию
    val dynamicThemeEnabled = _dynamicThemeEnabled.asStateFlow()

    fun setDynamicThemeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _dynamicThemeEnabled.value = enabled



        }
    }
}