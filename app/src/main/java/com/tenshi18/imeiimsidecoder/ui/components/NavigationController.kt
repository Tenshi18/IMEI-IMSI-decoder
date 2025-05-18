package com.tenshi18.imeiimsidecoder.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DeviceUnknown
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tenshi18.imeiimsidecoder.db.presentation.viewmodels.DeviceViewModel
import com.tenshi18.imeiimsidecoder.history.presentation.screens.HistoryScreen
import com.tenshi18.imeiimsidecoder.history.presentation.viewmodels.HistoryViewModel
import com.tenshi18.imeiimsidecoder.settings.domain.model.IMEIMode
import com.tenshi18.imeiimsidecoder.settings.presentation.SettingsScreen
import com.tenshi18.imeiimsidecoder.settings.presentation.SettingsViewModel
import com.tenshi18.imeiimsidecoder.ui.screens.IMEIScreen
import com.tenshi18.imeiimsidecoder.ui.screens.IMSIScreen

data class NavItem(val label: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationController(deviceViewModel: DeviceViewModel, settingsViewModel: SettingsViewModel, historyViewModel: HistoryViewModel) {
    val navController: NavHostController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val historyList by historyViewModel.historyFlow.collectAsState()

    val showBottomBar = (currentRoute != "Settings")

    val imeiModeState by settingsViewModel.IMEIModeFlow
        .collectAsStateWithLifecycle(
            initialValue = IMEIMode.LOCAL,
            lifecycle = LocalLifecycleOwner.current.lifecycle
        )

    val screenTitle = when (currentRoute) {
        "IMEI" -> when (imeiModeState) {
            IMEIMode.LOCAL -> "IMEI (локальная БД)"
            IMEIMode.API -> "IMEI (запросы к API)"
        }
        "IMSI" -> "IMSI"
        "History" -> "История"
        "Settings" -> "Настройки"
        else -> "Decoder"
    }

    // Три пункта нижней навигации
    val navItems = listOf(
        NavItem("IMEI", Icons.Filled.DeviceUnknown),
        NavItem("IMSI", Icons.Filled.SimCard),
        NavItem("History", Icons.Filled.History)
    )

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    var showHelpDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (currentRoute != "Settings") {
                TopAppBar(
                    title = { Text(screenTitle) },
                    actions = {
                        IconButton(onClick = { showHelpDialog = true }) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "Help",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        IconButton(onClick = {
                            navController.navigate("Settings") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    // Цвета
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    navItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                navController.navigate(item.label) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },

                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onPrimary
                            )

                        )
                    }
                }
            }
        },

        // FAB для очистки истории
        floatingActionButton = {
            if (currentRoute == "History" && historyList.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { historyViewModel.clearHistory() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Filled.DeleteForever,
                        contentDescription = "Очистить историю"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Box(modifier = Modifier) {
            NavHost(navController = navController, startDestination = "IMEI") {
                composable("IMEI") { Column(modifier = Modifier.padding(paddingValues)) { IMEIScreen(deviceViewModel, settingsViewModel) } }
                composable("IMSI") { Column(modifier = Modifier.padding(paddingValues)) { IMSIScreen(deviceViewModel) } }
                composable("History") { Column(modifier = Modifier.padding(paddingValues)) { HistoryScreen(historyViewModel = historyViewModel) } }
                composable("Settings") { SettingsScreen(settingsViewModel, navController) }
            }
        }
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("Что такое IMEI и IMSI?") },
            text = {
                Box(
                    modifier = Modifier
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                    "IMEI (International Mobile Equipment Identity) — уникальный идентификатор устройства.\n" +
                        "IMSI (International Mobile Subscriber Identity) — уникальный идентификатор абонента в мобильной сети.\n\n" +
                        "IMEI присваивается каждому устройству. Он включает в себя TAC (Type Allocation Code) — код, определяющий производителя и модель устройства — и уникальный номер устройства.\n\n" +
                        "IMSI, в свою очередь, присваивается SIM-карте и используется для идентификации пользователя в сети мобильного оператора. Он включает в себя MCC (Mobile Country Code — код страны), MNC (Mobile Network Code — код оператора) и уникальный номер абонента.\n" +
                        "IMEI позволяет однозначно идентифицировать устройство, а IMSI — абонента и его оператора.\n\n" +
                        "MCC и MNC, определяющие страну и оператора мобильной связи, доступны приложениям без отдельного разрешения пользователя. Они могут быть использованы, например, для гео-блокировки или таргетирования контента.\n"
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("Закрыть")
                }
            }
        )
    }

    // Слушаем изменение маршрута, чтобы обновлять selectedItemIndex
    navController.addOnDestinationChangedListener { _, destination, _ ->
        selectedItemIndex = when (destination.route) {
            "IMEI" -> 0
            "IMSI" -> 1
            "History" -> 2
            else -> selectedItemIndex
        }
    }
}