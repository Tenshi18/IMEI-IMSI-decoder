package com.tenshi18.imeiimsidecoder.db.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tenshi18.imeiimsidecoder.db.data.local.MCCMNC
import com.tenshi18.imeiimsidecoder.db.data.local.TAC
import com.tenshi18.imeiimsidecoder.db.domain.repository.DeviceRepository
import com.tenshi18.imeiimsidecoder.history.domain.model.HistoryItem
import com.tenshi18.imeiimsidecoder.history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeviceViewModel(
    private val deviceRepository: DeviceRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    // Храним результат декодирования IMEI
    private val _imeiResult = MutableStateFlow<TAC?>(null)
    val imeiResult: StateFlow<TAC?> = _imeiResult

    // Храним результат декодирования IMSI
    private val _imsiResult = MutableStateFlow<MCCMNC?>(null)
    val imsiResult: StateFlow<MCCMNC?> = _imsiResult

    fun decodeIMEI(imei: String) {
        viewModelScope.launch {
            // Извлекаем первые 8 цифр IMEI (TAC)
            val tacString = imei.take(8)
            val tacInt = tacString.toIntOrNull() ?: return@launch
            val result = deviceRepository.getTAC(tacInt)
            _imeiResult.value = result

            // Сохраняем результат в историю (для отображения на HistoryScreen)
            historyRepository.addHistoryItem(
                HistoryItem(
                    type = "IMEI",
                    value = imei,
                    decoded = result?.toString() ?: "Не удалось декодировать",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun decodeIMSI(imsi: String) {
        viewModelScope.launch {
            // MCC обычно 3 цифры, MNC 2 или 3, в простом случае примем, что MNC = 2
            // (сделать логику) TODO !!!
            val mccString = imsi.take(3)
            val mncString = imsi.drop(3).take(2) // примем, что MNC = 2
            val mccInt = mccString.toIntOrNull() ?: return@launch
            val mncInt = mncString.toIntOrNull() ?: return@launch
            val result = deviceRepository.getMCCMNC(mccInt, mncInt)
            _imsiResult.value = result

            // Сохраняем результат в историю (для отображения на HistoryScreen)
            historyRepository.addHistoryItem(
                HistoryItem(
                    type = "IMSI",
                    value = imsi,
                    decoded = result?.toString() ?: "Не удалось декодировать",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}