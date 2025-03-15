package com.tenshi18.imeiimsidecoder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tenshi18.imeiimsidecoder.settings.data.local.SettingsLocalDataSource
import com.tenshi18.imeiimsidecoder.db.data.local.DatabaseProviderMCCMNC
import com.tenshi18.imeiimsidecoder.db.data.local.DatabaseProviderTac
import com.tenshi18.imeiimsidecoder.db.data.repository.DeviceRepositoryImpl
import com.tenshi18.imeiimsidecoder.db.presentation.viewmodels.DeviceViewModel
import com.tenshi18.imeiimsidecoder.history.data.local.HistoryLocalDataSource
import com.tenshi18.imeiimsidecoder.history.data.repository.HistoryRepositoryImpl
import com.tenshi18.imeiimsidecoder.history.presentation.viewmodels.HistoryViewModel
import com.tenshi18.imeiimsidecoder.settings.data.repository.SettingsRepositoryImpl
import com.tenshi18.imeiimsidecoder.ui.theme.IMEIIMSIDecoderTheme
import com.tenshi18.imeiimsidecoder.ui.components.NavigationController
import com.tenshi18.imeiimsidecoder.settings.presentation.SettingsViewModel
import com.tenshi18.imeiimsidecoder.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TAC и MCC/MNC (DeviceRepository)
        val tacDao = DatabaseProviderTac.getDatabase(applicationContext).tacDao()
        val mccMncDao = DatabaseProviderMCCMNC.getDatabase(applicationContext).mccMncDao()
        val deviceRepository = DeviceRepositoryImpl(tacDao, mccMncDao)

        // История
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val historyLocalDataSource = HistoryLocalDataSource(
            context = applicationContext,
            moshi = moshi
        )
        val historyRepository = HistoryRepositoryImpl(historyLocalDataSource)

        val historyViewModel = HistoryViewModel(historyRepository)

        // Фабрика DeviceViewModel
        val deviceFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(DeviceViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return DeviceViewModel(deviceRepository, historyRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val deviceViewModel = ViewModelProvider(this, deviceFactory)[DeviceViewModel::class.java]

        // Настройки
        val localDataSource = SettingsLocalDataSource(applicationContext)
        val settingsRepository = SettingsRepositoryImpl(localDataSource)

        val settingsFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SettingsViewModel(settingsRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val settingsViewModel = ViewModelProvider(this, settingsFactory)[SettingsViewModel::class.java]

        enableEdgeToEdge()
        setContent {

            val useDynamicColours = settingsViewModel.useDynamicColoursFlow.collectAsState(initial = true).value
            val themeMode = settingsViewModel.themeModeFlow.collectAsState(initial = ThemeMode.SYSTEM).value

            IMEIIMSIDecoderTheme (useDynamicColours = useDynamicColours, themeMode = themeMode) {
                NavigationController(deviceViewModel, settingsViewModel, historyViewModel)
            }
        }
    }
}