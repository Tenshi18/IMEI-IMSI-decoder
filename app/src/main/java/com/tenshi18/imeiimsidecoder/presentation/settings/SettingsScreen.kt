package com.tenshi18.imeiimsidecoder.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tenshi18.imeiimsidecoder.presentation.theme.ThemeMode

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    val useDynamicColours by settingsViewModel.useDynamicColoursFlow.collectAsState()
    val themeMode by settingsViewModel.themeModeFlow.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    Column {
        PreferenceGroupTitle("Внешний вид")

        // Динамические цвета MD3
        SwitchPreference(
            modifier = Modifier.fillMaxWidth(),
            title = { Text("Динамическая тема (Material You)") },
            description = "Использовать цвета обоев",
            checked = useDynamicColours,
            onCheckedChange = { settingsViewModel.setDynamicColoursEnabled(it) }
        )

        // Системная/тёмная/светлая тема
        PreferenceEntry(
            title = { Text("Тема") },
            description = when (themeMode) {
                ThemeMode.SYSTEM -> "Авто (системная)"
                ThemeMode.DARK -> "Тёмная"
                ThemeMode.LIGHT -> "Светлая"
            },
            onClick = { showDialog = true }
        )
    }

    // Если showDialog == true, показываем диалог
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = { },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            },
            text = {
                // Три RadioButton
                Column {
                    // 1. Системная
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (themeMode == ThemeMode.SYSTEM),
                            onClick = {
                                settingsViewModel.setThemeMode(ThemeMode.SYSTEM)
                                showDialog = false
                            }
                        )
                        Text(
                            text = "Авто (системная)",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable {
                                    settingsViewModel.setThemeMode(ThemeMode.SYSTEM)
                                    showDialog = false
                                }
                        )
                    }

                    // 2. Тёмная
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (themeMode == ThemeMode.DARK),
                            onClick = {
                                settingsViewModel.setThemeMode(ThemeMode.DARK)
                                showDialog = false
                            }
                        )
                        Text(
                            text = "Тёмная",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable {
                                    settingsViewModel.setThemeMode(ThemeMode.DARK)
                                    showDialog = false
                                }
                        )
                    }

                    // 3. Светлая
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (themeMode == ThemeMode.LIGHT),
                            onClick = {
                                settingsViewModel.setThemeMode(ThemeMode.LIGHT)
                                showDialog = false
                            }
                        )
                        Text(
                            text = "Светлая",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable {
                                    settingsViewModel.setThemeMode(ThemeMode.LIGHT)
                                    showDialog = false
                                }
                        )
                    }
                }
            }
        )
    }
}

//class DummySettingsRepository : SettingsRepository {
//
//    private val _useDynamicColours = MutableStateFlow(false)
//    override val useDynamicColoursFlow: StateFlow<Boolean> = _useDynamicColours
//
//    override suspend fun setDynamicColoursEnabled(enabled: Boolean) {
//        _useDynamicColours.value = enabled
//    }
//
//    private val _isDarkTheme = MutableStateFlow(false)
//    override val isDarkThemeFlow: StateFlow<Boolean> = _isDarkTheme
//    override suspend fun setDarkThemeEnabled(enabled: Boolean) {
//        _isDarkTheme.value = enabled
//    }
//}
//
//// Предпросмотр
//@Preview(showBackground = true)
//@Composable
//fun PreviewSettingsScreen() {
//    val dummyRepo = DummySettingsRepository()
//
//    val dummyViewModel = SettingsViewModel(dummyRepo)
//
//    IMEIIMSIDecoderTheme(useDynamicColours = false, isDarkTheme = false) {
//        SettingsScreen(settingsViewModel = dummyViewModel)
//    }
//}