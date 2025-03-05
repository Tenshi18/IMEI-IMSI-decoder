package com.tenshi18.imeiimsidecoder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tenshi18.imeiimsidecoder.data.local.SettingsLocalDataSource
import com.tenshi18.imeiimsidecoder.data.repository.SettingsRepositoryImpl
import com.tenshi18.imeiimsidecoder.presentation.theme.IMEIIMSIDecoderTheme
import com.tenshi18.imeiimsidecoder.presentation.components.NavigationController
import com.tenshi18.imeiimsidecoder.presentation.viewmodels.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val localDataSource = SettingsLocalDataSource(applicationContext)
        val settingsRepository = SettingsRepositoryImpl(localDataSource)

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SettingsViewModel(settingsRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val settingsViewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            IMEIIMSIDecoderTheme (useDynamicColours = settingsViewModel.useDynamicColoursFlow.collectAsState(initial = true).value) {

                NavigationController(settingsViewModel)

            }
        }
    }
}