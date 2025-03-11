package com.tenshi18.imeiimsidecoder.db.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tenshi18.imeiimsidecoder.db.data.local.MCCMNC
import com.tenshi18.imeiimsidecoder.db.data.local.TAC
import com.tenshi18.imeiimsidecoder.db.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeviceViewModel(
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    // Храним результат декодирования IMEI
    private val _imeiResult = MutableStateFlow<TAC?>(null)
    val imeiResult: StateFlow<TAC?> = _imeiResult

    // Храним результат декодирования IMSI
    private val _imsiResult = MutableStateFlow<MCCMNC?>(null)
    val imsiResult: StateFlow<MCCMNC?> = _imsiResult

    fun decodeIMEI(imei: String) {
        viewModelScope.launch {
            // Извлекаем первые 8 цифр IMEI → TAC
            val tacString = imei.take(8)
            val tacInt = tacString.toIntOrNull() ?: return@launch
            val result = deviceRepository.getTAC(tacInt)
            _imeiResult.value = result
        }
    }

    fun decodeIMSI(imsi: String) {
        viewModelScope.launch {
            // MCC обычно 3 цифры, MNC 2 или 3, в простом случае assume MNC = 2
            // (сделать логику) TODO !!!
            val mccString = imsi.take(3)
            val mncString = imsi.drop(3).take(2) // assume 2
            val mccInt = mccString.toIntOrNull() ?: return@launch
            val mncInt = mncString.toIntOrNull() ?: return@launch
            val result = deviceRepository.getMCCMNC(mccInt, mncInt)
            _imsiResult.value = result
        }
    }
}