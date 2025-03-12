package com.tenshi18.imeiimsidecoder.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.tenshi18.imeiimsidecoder.db.data.local.TAC
import com.tenshi18.imeiimsidecoder.db.data.local.MCCMNC
import com.tenshi18.imeiimsidecoder.db.domain.repository.DeviceRepository
@Composable
fun IMEIScreen(deviceViewModel: DeviceViewModel) {
    val imeiResult by deviceViewModel.imeiResult.collectAsState()
    var imeiInput by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        Text("Введите IMEI (15 цифр)")
        TextField(
            value = imeiInput,
            onValueChange = { if (it.length <= 15 && it.all { char -> char.isDigit() }) imeiInput = it },
            label = { Text("IMEI") }
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            deviceViewModel.decodeIMEI(imeiInput)
        }) {
            Text("Декодировать")
        }
        Spacer(Modifier.height(16.dp))
        imeiResult?.let { result ->
            Text("Результат")
            Text("Бренд: ${result.brand}")
            Text("Модель: ${result.model}")
            Text("AKA: ${result.aka}")
        }

    }

}


//// Предпросмотр
//class DummyDeviceRepository : DeviceRepository {
//    override suspend fun getTAC(tac: Int): TAC? {
//        // Возвращаем реальные классы из db.data.local
//        return TAC(
//            tac = 12345678,
//            brand = "Nokia",
//            model = "3310",
//            aka = "Legacy phone"
//        )
//    }
//
//    override suspend fun getMCCMNC(mcc: Int, mnc: Int): MCCMNC? {
//        return MCCMNC(
//            mcc = 250,
//            mnc = 99,
//            plmn = "25099",
//            operator = "Beeline",
//            country = "Russia",
//            region = "Europe",
//            iso = "RU",
//            brand = "Beeline",
//            bands = "GSM 900 / 1800",
//            tadig = "",
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewIMEIScreen() {
//    val dummyRepo = DummyDeviceRepository()
//    val deviceViewModel = remember { DeviceViewModel(dummyRepo) }
//
//    LaunchedEffect(Unit) {
//        deviceViewModel.decodeIMEI("123456789012345")
//    }
//
//    IMEIIMSIDecoderTheme(themeMode = ThemeMode.LIGHT, useDynamicColours = false) {
//        IMEIScreen(deviceViewModel)
//    }
//}