package com.tenshi18.imeiimsidecoder.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen(settingsViewModel : SettingsViewModel) {

    // Подписываемся на StateFlow из ViewModel
    val useDynamicColours by settingsViewModel.useDynamicColoursFlow.collectAsState()

    Column {

        SwitchPreference(
            modifier = Modifier.fillMaxWidth(),
            title = { Text("Динамическая тема (Material You)") },
            description = "Использовать цвета обоев",
            checked = useDynamicColours,
            onCheckedChange = { settingsViewModel.setDynamicColoursEnabled(it) }
        )

    }
}

