package com.tenshi18.imeiimsidecoder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tenshi18.imeiimsidecoder.ui.screens.imei.IMEIScreen
import com.tenshi18.imeiimsidecoder.ui.theme.IMEIIMSIDecoderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IMEIIMSIDecoderTheme {
                IMEIScreen()

            }
        }
    }
}