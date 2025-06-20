package com.tenshi18.imeiimsidecoder.history.data.repository

import com.tenshi18.imeiimsidecoder.history.data.local.HistoryLocalDataSource
import com.tenshi18.imeiimsidecoder.history.domain.model.HistoryItem
import com.tenshi18.imeiimsidecoder.history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import android.content.Context
import android.net.Uri

class HistoryRepositoryImpl @Inject constructor(
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

    override suspend fun exportHistoryToCsv(context: Context, uri: Uri, queryHistory: List<HistoryItem>) {
        localDataSource.exportHistoryToCsv(context, uri, queryHistory)
    }

    override suspend fun importHistoryFromCsv(context: Context, uri: Uri): List<HistoryItem> {
        return localDataSource.importHistoryFromCsv(context, uri)
    }

    override suspend fun getAllHistoryItems(): List<HistoryItem> {
        return localDataSource.getAllHistoryItems()
    }

    override suspend fun saveImportedHistoryItems(items: List<HistoryItem>) {
        localDataSource.saveImportedHistoryItems(items)
    }
}