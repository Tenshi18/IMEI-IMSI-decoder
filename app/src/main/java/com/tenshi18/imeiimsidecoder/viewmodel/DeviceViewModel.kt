package com.tenshi18.imeiimsidecoder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tenshi18.imeiimsidecoder.data.db.DatabaseProviderTac
import com.tenshi18.imeiimsidecoder.data.db.Tac
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class DeviceViewModel(application: Application) : AndroidViewModel(application) {

    // Получаем DAO из предзагруженной базы TAC
    private val tacDao = DatabaseProviderTac.getDatabase(application).tacDao()

    // Состояние: список всех записей TAC
    private val _tacList = mutableStateOf<List<Tac>>(emptyList())
    val tacList: State<List<Tac>> = _tacList

    init {
        loadTacs()
    }

    private fun loadTacs() {
        viewModelScope.launch {
            _tacList.value = tacDao.getAllTacs()
        }
    }

    // Функция поиска записи по TAC
    fun getTacById(tac: Int, onResult: (Tac?) -> Unit) {
        viewModelScope.launch {
            onResult(tacDao.getTac(tac))
        }
    }
}
