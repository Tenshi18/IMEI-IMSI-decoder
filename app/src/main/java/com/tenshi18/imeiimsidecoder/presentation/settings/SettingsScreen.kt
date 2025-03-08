package com.tenshi18.imeiimsidecoder.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tenshi18.imeiimsidecoder.domain.repository.SettingsRepository
import com.tenshi18.imeiimsidecoder.presentation.theme.IMEIIMSIDecoderTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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

class DummySettingsRepository : SettingsRepository {

    private val _useDynamicColours = MutableStateFlow(false)
    override val useDynamicColoursFlow: StateFlow<Boolean> = _useDynamicColours

    override suspend fun setDynamicColoursEnabled(enabled: Boolean) {
        _useDynamicColours.value = enabled
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    val dummyRepo = DummySettingsRepository()

    val dummyViewModel = SettingsViewModel(dummyRepo)

    IMEIIMSIDecoderTheme(useDynamicColours = false) {
        SettingsScreen(settingsViewModel = dummyViewModel)
    }
}