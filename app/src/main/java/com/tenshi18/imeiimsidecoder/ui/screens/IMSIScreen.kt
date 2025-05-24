package com.tenshi18.imeiimsidecoder.ui.screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import com.tenshi18.imeiimsidecoder.db.presentation.viewmodels.DeviceViewModel

@Composable
fun IMSIScreen(deviceViewModel: DeviceViewModel) {
    val imsiResult by deviceViewModel.imsiResult.collectAsState()
    var imsiInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Введите IMSI")
        OutlinedTextField(
            value = imsiInput,
            onValueChange = { imsiInput = it.filter(Char::isDigit) },
            label = { Text("IMSI") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            if (imsiInput.length != 15) {
                Toast.makeText(context, "IMSI должен быть из 15 цифр", Toast.LENGTH_SHORT).show()
            } else {
                deviceViewModel.decodeIMSI(imsiInput)
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
            Text("Примеры (нажмите, чтобы подставить):")
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("МТС: ")
                        withStyle(
                            style = SpanStyle(
                                fontFamily = FontFamily.Monospace,
                                background = Color.LightGray.copy(alpha = 0.2f)
                            )
                        ) { append("250012345678901") }
                    },
                    modifier = Modifier.fillMaxWidth().clickable { imsiInput = "250012345678901" }
                )
                Text(
                    text = buildAnnotatedString {
                        append("Мегафон: ")
                        withStyle(
                            style = SpanStyle(
                                fontFamily = FontFamily.Monospace,
                                background = Color.LightGray.copy(alpha = 0.2f)
                            )
                        ) { append("250023456789012") }
                    },
                    modifier = Modifier.fillMaxWidth().clickable { imsiInput = "250023456789012" }
                )
            }
            Spacer(Modifier.height(16.dp))

            // Блок результатов
            imsiResult?.let { result ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("MCC: ${result.mcc}")
                    Text("MNC: ${result.mnc}")
                    Text("PLMN: ${result.plmn}")
                    Text("Регион: ${result.region}")
                    Text("Страна: ${result.country}")
                    Text("ISO-код страны: ${result.iso}")
                    Text("Оператор: ${result.operator}")
                    Text("Бренд: ${result.brand}")
                    Text("Частоты: ${result.bands}")

                    Spacer(Modifier.height(16.dp))

                    // Кнопка "копировать результат в буфер обмена"
                    Button(
                        onClick = {
                            clipboardManager.setText(
                                AnnotatedString(
                                    "IMSI: ${imsiInput}\n" +
                                        "MCC: ${imsiResult?.mcc}\n" +
                                        "MNC: ${imsiResult?.mnc}\n" +
                                        "PLMN: ${imsiResult?.plmn}\n" +
                                        "Регион: ${imsiResult?.region}\n" +
                                        "Страна: ${imsiResult?.country}\n" +
                                        "ISO-код страны: ${imsiResult?.iso}\n" +
                                        "Оператор: ${imsiResult?.operator}\n" +
                                        "Бренд: ${imsiResult?.brand}\n" +
                                        "Частоты: ${imsiResult?.bands}"
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Копировать результат в буфер обмена")
                    }
                }
            }
        }
    }
}
