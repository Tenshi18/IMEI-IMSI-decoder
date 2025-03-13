package com.tenshi18.imeiimsidecoder.ui.components

import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.DeviceUnknown
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tenshi18.imeiimsidecoder.db.presentation.viewmodels.DeviceViewModel
import com.tenshi18.imeiimsidecoder.ui.screens.HistoryScreen
import com.tenshi18.imeiimsidecoder.ui.screens.IMEIScreen
import com.tenshi18.imeiimsidecoder.ui.screens.IMSIScreen
import com.tenshi18.imeiimsidecoder.settings.presentation.SettingsScreen
import com.tenshi18.imeiimsidecoder.settings.presentation.SettingsViewModel

data class NavItem(val label: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationController(deviceViewModel: DeviceViewModel, settingsViewModel: SettingsViewModel) {
    val navController: NavHostController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val showBottomBar = (currentRoute != "Settings")

    val screenTitle = when (currentRoute) {
        "IMEI" -> "IMEI"
        "IMSI" -> "IMSI"
        "History" -> "История"
        "Settings" -> "Настройки"
        else -> "IMEI"
    }

    // Три пункта нижней навигации
    val navItems = listOf(
        NavItem("IMEI", Icons.Filled.DeviceUnknown),
        NavItem("IMSI", Icons.Filled.SimCard),
        NavItem("History", Icons.Filled.History)
    )

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            if (currentRoute != "Settings") {
                TopAppBar(
                    title = { Text(screenTitle) },
                    actions = {
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
        }
    ) { paddingValues ->
        Box(modifier = Modifier) {
            NavHost(navController = navController, startDestination = "IMEI") {
                composable("IMEI") { Column(modifier = Modifier.padding(paddingValues)) { IMEIScreen(deviceViewModel) } }
                composable("IMSI") { Column(modifier = Modifier.padding(paddingValues)) { IMSIScreen(deviceViewModel) } }
                composable("History") { Column(modifier = Modifier.padding(paddingValues)) { HistoryScreen() } }
                composable("Settings") { SettingsScreen(settingsViewModel, navController) }
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