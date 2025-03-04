package com.tenshi18.imeiimsidecoder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tenshi18.imeiimsidecoder.data.local.SettingsLocalDataSource
import com.tenshi18.imeiimsidecoder.data.repository.SettingsRepositoryImpl
import com.tenshi18.imeiimsidecoder.presentation.theme.IMEIIMSIDecoderTheme
import com.tenshi18.imeiimsidecoder.presentation.components.NavigationController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val localDataSource = SettingsLocalDataSource(applicationContext)
        val settingsRepository = SettingsRepositoryImpl(localDataSource)

        enableEdgeToEdge()
        setContent {
            IMEIIMSIDecoderTheme {

                NavigationController()

            }
        }
    }
}