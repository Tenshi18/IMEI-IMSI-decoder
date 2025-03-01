package com.tenshi18.imeiimsidecoder.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tenshi18.imeiimsidecoder.ui.screens.HistoryScreen
import com.tenshi18.imeiimsidecoder.ui.screens.IMEIScreen
import com.tenshi18.imeiimsidecoder.ui.screens.IMSIScreen
import com.tenshi18.imeiimsidecoder.ui.screens.SettingsScreen

data class NavItem(val label: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationController() {
    val navController: NavHostController = rememberNavController()

    // Нижняя навигация: только IMEI, IMSI и History
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
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 15.dp,
                            bottomEnd = 15.dp
                        )
                    ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                title = { Text(screenTitle) },
                actions = {
                    // Переход на экран настроек по нажатию на значок в TopAppBar
                    IconButton(onClick = {
                        navController.navigate("Settings") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
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
                            val route = item.label  // маршруты: "IMEI", "IMSI", "History"
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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
        },
        content = { paddingValues: PaddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavigationHost(navController = navController)
            }
        }
    )

    // Обновляем selectedItemIndex при изменении маршрута (для экранов нижней навигации)
    navController.addOnDestinationChangedListener { _, destination, _ ->
        when (destination.route) {
            "IMEI" -> selectedItemIndex = 0
            "IMSI" -> selectedItemIndex = 1
            "History" -> selectedItemIndex = 2
        }
    }
}

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "IMEI") {
        composable("IMEI") { IMEIScreen() }
        composable("IMSI") { IMSIScreen() }
        composable("History") { HistoryScreen() }
        composable("Settings") { SettingsScreen() }
    }
}