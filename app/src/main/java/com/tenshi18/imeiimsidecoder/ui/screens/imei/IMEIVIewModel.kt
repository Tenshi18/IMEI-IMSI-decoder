package com.tenshi18.imeiimsidecoder.ui.screens.imei

class IMEIViewModel(
    private val decodeIMEIUseCase: DecodeIMEIUseCase
) : ViewModel() {
    private val _imeiInput = MutableStateFlow("")
    val imeiInput: StateFlow<String> = _imeiInput

    private val _imeiInfo = MutableStateFlow<IMEIInfo?>(null)
    val imeiInfo: StateFlow<IMEIInfo?> = _imeiInfo

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    fun setIMEI(imei: String) {
        _imeiInput.value = imei
        _isError.value = imei.isNotEmpty() && !IMEIValidator.isValid(imei)
    }

    fun decodeIMEI() {
        val imei = _imeiInput.value

        if (!IMEIValidator.isValid(imei)) {
            _isError.value = true
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _imeiInfo.value = decodeIMEIUseCase(imei)
            _isLoading.value = false
        }
    }
}