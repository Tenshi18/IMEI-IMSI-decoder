package com.tenshi18.imeiimsidecoder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import com.tenshi18.imeiimsidecoder.db.presentation.viewmodels.DeviceViewModel
import com.tenshi18.imeiimsidecoder.history.presentation.viewmodels.HistoryViewModel
import com.tenshi18.imeiimsidecoder.ui.theme.IMEIIMSIDecoderTheme
import com.tenshi18.imeiimsidecoder.ui.components.NavigationController
import com.tenshi18.imeiimsidecoder.settings.presentation.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val deviceViewModel: DeviceViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val historyViewModel: HistoryViewModel = androidx.hilt.navigation.compose.hiltViewModel()

            val useDynamicColours = settingsViewModel.useDynamicColoursFlow.collectAsState(initial = true).value
            val themeMode = settingsViewModel.themeModeFlow.collectAsState(initial = com.tenshi18.imeiimsidecoder.ui.theme.ThemeMode.SYSTEM).value

            IMEIIMSIDecoderTheme (useDynamicColours = useDynamicColours, themeMode = themeMode) {
                NavigationController(deviceViewModel, settingsViewModel, historyViewModel)
            }
        }
    }
}