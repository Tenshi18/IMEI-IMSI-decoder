package com.tenshi18.imeiimsidecoder.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tenshi18.imeiimsidecoder.db.presentation.viewmodels.DeviceViewModel
import com.tenshi18.imeiimsidecoder.settings.domain.model.IMEIMode
import com.tenshi18.imeiimsidecoder.settings.presentation.SettingsViewModel

@Composable
fun IMEIScreen(deviceViewModel: DeviceViewModel, settingsViewModel: SettingsViewModel) {
    val imeiResult by deviceViewModel.imeiResult.collectAsState()
    var imeiInput by remember { mutableStateOf("") }

    val imeiModeState by settingsViewModel.IMEIModeFlow
        .collectAsStateWithLifecycle(
            initialValue = IMEIMode.LOCAL,
            lifecycle = LocalLifecycleOwner.current.lifecycle
        )

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Введите IMEI")
        OutlinedTextField(
            value = imeiInput,
            onValueChange = { imeiInput = it.filter(Char::isDigit) },
            label = { Text("IMEI") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            if (imeiInput.length != 15) {
                Toast.makeText(context, "IMEI должен быть из 15 цифр", Toast.LENGTH_SHORT).show()
            } else {
                deviceViewModel.decodeIMEI(imeiInput)
            }
        }) {
            Text("Декодировать")
        }
        Spacer(Modifier.height(16.dp))
        // Примеры
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            when (imeiModeState) {
                IMEIMode.LOCAL -> {
                    Text("Примеры (нажмите):")
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("Nokia 6700 Classic: ")
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = FontFamily.Monospace,
                                        background = Color.LightGray.copy(alpha = 0.2f)
                                    )
                                ) { append("356943031234567") }
                            },
                            modifier = Modifier.fillMaxWidth()
                                .clickable { imeiInput = "356943031234567" }
                        )
                        Text(
                            text = buildAnnotatedString {
                                append("HTC One: ")
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = FontFamily.Monospace,
                                        background = Color.LightGray.copy(alpha = 0.2f)
                                    )
                                ) { append("357864051234567") }
                            },
                            modifier = Modifier.fillMaxWidth()
                                .clickable { imeiInput = "357864051234567" }
                        )
                    }
                }
                IMEIMode.API -> {
                    Text("Примеры (нажмите):")
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("Sony Xperia 1 VI (XQ-EC72)")
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = FontFamily.Monospace,
                                        background = Color.LightGray.copy(alpha = 0.2f)
                                    )
                                ) { append("355723388827246") }
                            },
                            modifier = Modifier.fillMaxWidth()
                                .clickable { imeiInput = "355723388827246" }
                        )
                        Text(
                            text = buildAnnotatedString {
                                append("Apple iPhone 16 Pro")
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = FontFamily.Monospace,
                                        background = Color.LightGray.copy(alpha = 0.2f)
                                    )
                                ) { append("358876620213828") }
                            },
                            modifier = Modifier.fillMaxWidth()
                                .clickable { imeiInput = "358876620213828" }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Блок результатов
            imeiResult?.let { result ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Бренд: ${result.brand}")
                    Text("Модель: ${result.model}")
                    Text("AKA: ${result.aka}")
                }
            }
        }
    }
}