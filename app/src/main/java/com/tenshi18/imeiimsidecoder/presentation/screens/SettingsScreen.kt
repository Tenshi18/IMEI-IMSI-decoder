package com.tenshi18.imeiimsidecoder.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tenshi18.imeiimsidecoder.presentation.viewmodels.SettingsViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {

    // Подписываемся на StateFlow из ViewModel
    val useDynamicColours by settingsViewModel.useDynamicColoursFlow.collectAsState()

    Column (modifier = Modifier.padding(16.dp)){
        Text(text = "Настройки")

        Row {
            Text(text = "Динамческая тема (Material You)")
            Switch(
                checked = useDynamicColours,
                onCheckedChange = { settingsViewModel.setDynamicColoursEnabled(it) }
            )
        }
    }

}