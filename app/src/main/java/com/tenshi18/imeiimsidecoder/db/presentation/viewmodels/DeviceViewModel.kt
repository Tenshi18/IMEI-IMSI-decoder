package com.tenshi18.imeiimsidecoder.db.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tenshi18.imeiimsidecoder.db.data.local.MCCMNC
import com.tenshi18.imeiimsidecoder.db.data.local.TAC
import com.tenshi18.imeiimsidecoder.db.domain.repository.DeviceRepository
import com.tenshi18.imeiimsidecoder.history.domain.model.HistoryItem
import com.tenshi18.imeiimsidecoder.history.domain.repository.HistoryRepository
import com.tenshi18.imeiimsidecoder.db.utils.formatIMEIResult
import com.tenshi18.imeiimsidecoder.remote.model.APIIMEIResponse
import com.tenshi18.imeiimsidecoder.settings.domain.model.IMEIMode
import com.tenshi18.imeiimsidecoder.settings.presentation.SettingsViewModel
import com.tenshi18.imeiimsidecoder.db.utils.formatIMSIResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeviceViewModel(
    private val deviceRepository: DeviceRepository,
    private val historyRepository: HistoryRepository,
    private val settingsViewModel: SettingsViewModel,
) : ViewModel() {

    // Храним результат декодирования IMEI
    private val _imeiResult = MutableStateFlow<TAC?>(null)
    val imeiResult: StateFlow<TAC?> = _imeiResult

    // Храним результат декодирования IMSI
    private val _imsiResult = MutableStateFlow<MCCMNC?>(null)
    val imsiResult: StateFlow<MCCMNC?> = _imsiResult

    // Состояние загрузки ответа API
    private val _isApiResponseLoading = MutableStateFlow(false)
    val isApiResponseLoading: StateFlow<Boolean> = _isApiResponseLoading.asStateFlow()

    fun decodeIMEI(imei: String) {
        viewModelScope.launch {

            when (settingsViewModel.IMEIModeFlow.value) {

                IMEIMode.LOCAL -> {
                // Извлекаем первые 8 цифр IMEI (TAC)
                val tacString = imei.take(8)
                val tacInt = tacString.toIntOrNull() ?: return@launch
                val result = deviceRepository.getTAC(tacInt)
                _imeiResult.value = result

                // Сохраняем результат в историю (для отображения на HistoryScreen)
                historyRepository.addHistoryItem(
                    HistoryItem(
                        timestamp = System.currentTimeMillis(),
                        type = "IMEI",
                        value = imei,
                        decoded = formatIMEIResult(result).toString()
                    )
                )
            }
                IMEIMode.API -> {
                    _isApiResponseLoading.value = true
                    try {
                        val apiResp: APIIMEIResponse = deviceRepository.getModelBrandNameFromAPI(imei)
                        val mapped = TAC(
                            tac   = apiResp.imei.take(8).toIntOrNull() ?: 0,
                            brand = apiResp.objectInfo.brand,
                            model = apiResp.objectInfo.model,
                            aka = apiResp.objectInfo.modelName
                        )
                        _imeiResult.value = mapped
                        historyRepository.addHistoryItem(
                            HistoryItem(
                                timestamp = System.currentTimeMillis(),
                                type = "IMEI",
                                value = imei,
                                decoded = formatIMEIResult(mapped).toString()

                            )
                        )
                    } catch (e: Exception) {
                        _imeiResult.value = null
                        historyRepository.addHistoryItem(
                            HistoryItem(
                                timestamp = System.currentTimeMillis(),
                                type = "IMEI",
                                value = imei,
                                decoded = "Ошибка API: ${e.localizedMessage}"
                            )
                        )
                    }
                    finally {
                        _isApiResponseLoading.value = false
                    }
                }
            }
        }
    }

    fun decodeIMSI(imsi: String) {
        viewModelScope.launch {
            // Извлекаем MCC (всегда 3 цифры)
            val mcc = imsi.take(3).toIntOrNull() ?: return@launch

            var result: MCCMNC? = null

            // Попробуем MNC длиной 2, затем 3
            for (len in listOf(2, 3)) {
                val mnc = imsi.drop(3).take(len).toIntOrNull() ?: continue
                result = deviceRepository.getMCCMNC(mcc, mnc)
                if (result != null) break
            }

            _imsiResult.value = result

            // Сохраняем результат в историю (для отображения на HistoryScreen)
            historyRepository.addHistoryItem(
                HistoryItem(
                    timestamp = System.currentTimeMillis(),
                    type = "IMSI",
                    value = imsi,
                    decoded = formatIMSIResult(result).toString()
                )
            )
        }
    }
}