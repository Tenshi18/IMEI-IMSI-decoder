package com.tenshi18.imeiimsidecoder.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tenshi18.imeiimsidecoder.data.model.NavItems
import com.tenshi18.imeiimsidecoder.ui.screens.imei.IMEIScreen
import com.tenshi18.imeiimsidecoder.ui.screens.imsi.IMSIScreen
import com.tenshi18.imeiimsidecoder.ui.screens.history.HistoryScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar() {

    val navController = rememberNavController()

    val items = listOf(
        NavItems("IMEI", Icons.Filled.Phone),
        NavItems("IMSI", Icons.Filled.SimCard),
        NavItems("History", Icons.Filled.History)
    )

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    var screenTitle = when (selectedItemIndex) {
        0 -> "IMEI"
        1 -> "IMSI"
        2 -> "History"
        else -> "IMEI"
    }

    Scaffold (
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 15.dp,
                            bottomEnd = 15.dp
                        )
                    ),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                title = { Text(screenTitle) },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Settings,
                            modifier = Modifier.size(24.dp),
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },

        bottomBar = {
            NavigationBar (
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 2.dp, start = 0.dp, end = 0.dp, bottom = 0.dp)

            ){

                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            val route = when (selectedItemIndex) {
                                0 -> "IMEI"
                                1 -> "IMSI"
                                2 -> "History"
                                else -> "IMEI"
                            }

                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = if (selectedItemIndex == index) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onPrimary
                                })
                        })
                }

            }

        }
    ) { paddingValues ->  // Здесь явно указываем параметр paddingValues

        navController.addOnDestinationChangedListener { _, destination, _ ->
            selectedItemIndex = when (destination.route) {
                "IMEI" -> 0
                "IMSI" -> 1
                "History" -> 2
                else -> 0
            }
        }

        NavHost(
            navController = navController,
            startDestination = "IMEI",
            modifier = Modifier.padding(paddingValues)  // Применяем padding к NavHost
        ) {
            composable("IMEI") { IMEIScreen() }
            composable("IMSI") { IMSIScreen() }
            composable("History") { HistoryScreen() }
        }

    }
}