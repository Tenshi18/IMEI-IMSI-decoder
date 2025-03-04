package com.tenshi18.imeiimsidecoder.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tenshi18.imeiimsidecoder.data.local.SettingsLocalDataSource
import com.tenshi18.imeiimsidecoder.presentation.screens.HistoryScreen
import com.tenshi18.imeiimsidecoder.presentation.screens.IMEIScreen
import com.tenshi18.imeiimsidecoder.presentation.screens.IMSIScreen
import com.tenshi18.imeiimsidecoder.presentation.screens.SettingsScreen
import com.tenshi18.imeiimsidecoder.presentation.viewmodels.SettingsViewModel

data class NavItem(val label: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationController(settingsViewModel: SettingsViewModel) {
    val navController: NavHostController = rememberNavController()

    // Три пункта нижней навигации
    val navItems = listOf(
        NavItem("IMEI", Icons.Filled.Phone),
        NavItem("IMSI", Icons.Filled.SimCard),
        NavItem("History", Icons.Filled.History)
    )

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val screenTitle = navItems.getOrNull(selectedItemIndex)?.label ?: "IMEI"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle) },
                // Кнопка настроек
                actions = {
                    IconButton(onClick = {
                        // Переход на экран "Settings"
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
        },
        bottomBar = {
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
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(navController = navController, startDestination = "IMEI") {
                composable("IMEI") { IMEIScreen() }
                composable("IMSI") { IMSIScreen() }
                composable("History") { HistoryScreen() }
                composable("Settings") { SettingsScreen(settingsViewModel) }
            }
        }
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