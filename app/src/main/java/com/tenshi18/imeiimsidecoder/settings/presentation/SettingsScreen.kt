package com.tenshi18.imeiimsidecoder.settings.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tenshi18.imeiimsidecoder.settings.domain.model.IMEIMode
import com.tenshi18.imeiimsidecoder.ui.theme.ThemeMode
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    navController: NavHostController
) {
    val currentIMEIMode by settingsViewModel.IMEIModeFlow.collectAsState()
    val useDynamicColours by settingsViewModel.useDynamicColoursFlow.collectAsState()
    val themeMode by settingsViewModel.themeModeFlow.collectAsState()

    var showThemeDialog by remember { mutableStateOf(false) }

    var showWarningDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    // ActivityResultLauncher для экспорта (создание документа)
    val exportHistoryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri: Uri? ->
        uri?.let {
            settingsViewModel.exportHistoryToCsv(context, it)
        } ?: run {
            scope.launch {
                snackbarHostState.showSnackbar("Экспорт отменен")
            }
        }
    }

    // ActivityResultLauncher для импорта (открытие документа)
    val importHistoryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            settingsViewModel.importHistoryFromCsv(context, it)
        } ?: run {
            scope.launch {
                snackbarHostState.showSnackbar("Импорт отменен")
            }
        }
    }

    // Подписываемся на событие показа
    LaunchedEffect(Unit) {
        settingsViewModel.showAPIWarningEvent.collect {
            showWarningDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            PreferenceGroupTitle("Работа с IMEI")
            SwitchPreference(
                modifier = Modifier.fillMaxWidth(),
                title = { Text("Режим работы с IMEI") },
                description = when (currentIMEIMode) {
                    IMEIMode.LOCAL -> "Использовать локальную базу TAC"
                    IMEIMode.API -> "Декодировать IMEI по API"
                },
                checked = currentIMEIMode == IMEIMode.LOCAL,
                onCheckedChange = { isLocal ->
                    val newMode = if (isLocal) IMEIMode.LOCAL else IMEIMode.API
                    settingsViewModel.setIMEIModeWithWarning(newMode)
                }
            )

            PreferenceGroupTitle("Экспорт и импорт истории")
            PreferenceEntry(
                title = { Text("Экспорт истории") },
                description = "Сохранить историю запросов в CSV-файл",
                icon = { Icon(Icons.Filled.FileUpload, contentDescription = "Экспорт истории") },
                onClick = {
                    val date = Date(System.currentTimeMillis())
                    val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    val formattedDateTime = dateFormat.format(date)
                    exportHistoryLauncher.launch("imei_imsi_decoder_history_${formattedDateTime}.csv")
                }
            )

            PreferenceEntry(
                title = { Text("Импорт истории") },
                description = "Загрузить историю запросов из CSV файла",
                icon = { Icon(Icons.Filled.FileDownload, contentDescription = "Импорт истории") },
                onClick = {
                    importHistoryLauncher.launch(arrayOf("text/csv", "text/plain", "*/*"))
                }
            )


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
                    ThemeMode.BLACK -> "Чёрная"
                },
                onClick = { showThemeDialog = true }
            )
        }
    }

    // Если showWarningDialog == true, показываем предупреждение при первом переключении в режим API
    if (showWarningDialog) {
        AlertDialog(
            onDismissRequest = { showWarningDialog = false },
            title = { Text("Внимание") },
            text = { Text("В режиме декодирования IMEI по API приложение будет делать сетевые запросы к https://alpha.imeicheck.com/api/" +
                    "Потребуется доступ к Интернету") },
            confirmButton = {
                TextButton(onClick = {
                    settingsViewModel.setAPIWarningShown()
                    settingsViewModel.setIMEIMode(IMEIMode.API)
                    showWarningDialog = false
                } )
                { Text("Продолжить") }
            },
            dismissButton = {
                TextButton(onClick = { showWarningDialog = false }) {
                    Text("Отмена")
                }
            })
    }

    // Если showThemeDialog == true, показываем диалог выбора темы
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            confirmButton = { },
            dismissButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Отмена")
                }
            },
            text = {
                Column {
                    // 1. Системная
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (themeMode == ThemeMode.SYSTEM),
                            onClick = {
                                settingsViewModel.setThemeMode(ThemeMode.SYSTEM)
                                showThemeDialog = false
                            }
                        )
                        Text(
                            text = "Авто (системная)",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable {
                                    settingsViewModel.setThemeMode(ThemeMode.SYSTEM)
                                    showThemeDialog = false
                                }
                        )
                    }

                    // 2. Тёмная
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (themeMode == ThemeMode.DARK),
                            onClick = {
                                settingsViewModel.setThemeMode(ThemeMode.DARK)
                                showThemeDialog = false
                            }
                        )
                        Text(
                            text = "Тёмная",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable {
                                    settingsViewModel.setThemeMode(ThemeMode.DARK)
                                    showThemeDialog = false
                                }
                        )
                    }

                    // 3. Светлая
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (themeMode == ThemeMode.LIGHT),
                            onClick = {
                                settingsViewModel.setThemeMode(ThemeMode.LIGHT)
                                showThemeDialog = false
                            }
                        )
                        Text(
                            text = "Светлая",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable {
                                    settingsViewModel.setThemeMode(ThemeMode.LIGHT)
                                    showThemeDialog = false
                                }
                        )
                    }

                    // 4. Чёрная
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (themeMode == ThemeMode.BLACK),
                            onClick = {
                                settingsViewModel.setThemeMode(ThemeMode.BLACK)
                                showThemeDialog = false
                            }
                        )
                        Text(
                            text = "Чёрная",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable {
                                    settingsViewModel.setThemeMode(ThemeMode.BLACK)
                                    showThemeDialog = false
                                }
                        )
                    }
                }
            }
        )
    }
}
