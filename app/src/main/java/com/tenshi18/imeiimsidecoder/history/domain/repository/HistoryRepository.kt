package com.tenshi18.imeiimsidecoder.history.domain.repository

import com.tenshi18.imeiimsidecoder.history.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    val historyFlow: Flow<List<HistoryItem>>
    suspend fun addHistoryItem(item: HistoryItem)
    suspend fun clearHistory()
}