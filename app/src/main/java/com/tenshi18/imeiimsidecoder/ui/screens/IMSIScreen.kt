package com.tenshi18.imeiimsidecoder.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tenshi18.imeiimsidecoder.db.presentation.viewmodels.DeviceViewModel
import com.tenshi18.imeiimsidecoder.ui.theme.IMEIIMSIDecoderTheme
import com.tenshi18.imeiimsidecoder.ui.theme.ThemeMode

@Composable
fun IMSIScreen(deviceViewModel: DeviceViewModel) {
    val imsiResult by deviceViewModel.imsiResult.collectAsState()
    var imsiInput by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        Text("Введите IMSI")
        OutlinedTextField(
            value = imsiInput,
            onValueChange = { imsiInput = it },
            label = { Text("IMSI") }
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            deviceViewModel.decodeIMSI(imsiInput)
        }) {
            Text("Декодировать")
        }
        Spacer(Modifier.height(16.dp))
        imsiResult?.let { result ->
            Text("MCC: ${result.mcc}")
            Text("MNC: ${result.mnc}")
            Text("Оператор: ${result.operator}")
            Text("Страна: ${result.country}")
            Text("Регион: ${result.region}")
            Text("ISO: ${result.iso}")
            Text("Бренд: ${result.brand}")
            Text("Частоты: ${result.bands}")
        }
    }
}

//// Предпросмотр
//@Preview(showBackground = true)
//@Composable
//fun PreviewIMSIScreen() {
//    val dummyRepo = DummyDeviceRepository()
//    val deviceViewModel = remember { DeviceViewModel(dummyRepo) }
//
//    LaunchedEffect(Unit) {
//        deviceViewModel.decodeIMSI("25099...") // MCC=250, MNC=99
//    }
//
//    IMEIIMSIDecoderTheme(themeMode = ThemeMode.LIGHT, useDynamicColours = false) {
//        IMSIScreen(deviceViewModel)
//    }
//}

