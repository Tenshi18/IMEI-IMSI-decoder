package com.tenshi18.imeiimsidecoder.presentation.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically

@Composable
fun SettingsScreen(settingsViewModel : SettingsViewModel) {

    // Подписываемся на StateFlow из ViewModel
    val useDynamicColours by settingsViewModel.useDynamicColoursFlow.collectAsState()

    Row(
        verticalAlignment = CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(text = "Динамческая тема (Material You)")
        Spacer(Modifier.width(12.dp))
        Switch(
            checked = useDynamicColours,
            onCheckedChange = { settingsViewModel.setDynamicColoursEnabled(it) }
        )
    }
}