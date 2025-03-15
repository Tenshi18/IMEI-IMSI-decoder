package com.tenshi18.imeiimsidecoder.history.data.repository

import com.tenshi18.imeiimsidecoder.history.data.local.HistoryLocalDataSource
import com.tenshi18.imeiimsidecoder.history.domain.model.HistoryItem
import com.tenshi18.imeiimsidecoder.history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow

class HistoryRepositoryImpl(
    private val localDataSource: HistoryLocalDataSource
) : HistoryRepository {

    override val historyFlow: Flow<List<HistoryItem>>
        get() = localDataSource.historyFlow

    override suspend fun addHistoryItem(item: HistoryItem) {
        localDataSource.addHistoryItem(item)
    }

    override suspend fun clearHistory() {
        localDataSource.clearHistory()
    }
}