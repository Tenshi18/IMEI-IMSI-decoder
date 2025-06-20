package com.tenshi18.imeiimsidecoder.history.domain.repository

import android.content.Context
import android.net.Uri
import com.tenshi18.imeiimsidecoder.history.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    val historyFlow: Flow<List<HistoryItem>>
    suspend fun addHistoryItem(item: HistoryItem)
    suspend fun clearHistory()

    suspend fun exportHistoryToCsv(context: Context, uri: Uri, queryHistory: List<HistoryItem>)
    suspend fun importHistoryFromCsv(context: Context, uri: Uri): List<HistoryItem>
    suspend fun getAllHistoryItems(): List<HistoryItem>
    suspend fun saveImportedHistoryItems(items: List<HistoryItem>)
}