package com.tenshi18.imeiimsidecoder.history.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tenshi18.imeiimsidecoder.history.domain.model.HistoryItem
import com.tenshi18.imeiimsidecoder.history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    val historyFlow: StateFlow<List<HistoryItem>> = historyRepository.historyFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addItem(item: HistoryItem) {
        viewModelScope.launch {
            historyRepository.addHistoryItem(item)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyRepository.clearHistory()
        }
    }
}