package com.tenshi18.imeiimsidecoder.history.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tenshi18.imeiimsidecoder.history.domain.model.HistoryItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import android.net.Uri
import kotlinx.coroutines.flow.first
import java.io.OutputStreamWriter
import java.io.IOException
import com.tenshi18.imeiimsidecoder.history.utils.HistoryCsvParser

private val HISTORY_JSON_KEY = stringPreferencesKey("history_json")

private const val HISTORY_DATASTORE_NAME = "history_datastore"

val Context.historyDataStore by preferencesDataStore(HISTORY_DATASTORE_NAME)

class HistoryLocalDataSource @Inject constructor(
    val context: Context,
    moshi: Moshi
) {

    private val listType = Types.newParameterizedType(List::class.java, HistoryItem::class.java)
    private val listAdapter = moshi.adapter<List<HistoryItem>>(listType)

    val historyFlow: Flow<List<HistoryItem>> = context.historyDataStore.data.map { preferences ->
        val json = preferences[HISTORY_JSON_KEY] ?: "[]"
        listAdapter.fromJson(json) ?: emptyList()
    }

    suspend fun addHistoryItem(item: HistoryItem) {
        context.historyDataStore.edit { preferences ->
            val oldJson = preferences[HISTORY_JSON_KEY] ?: "[]"
            val oldList = listAdapter.fromJson(oldJson) ?: emptyList()
            val newList = oldList + item
            preferences[HISTORY_JSON_KEY] = listAdapter.toJson(newList)
        }
    }

    suspend fun clearHistory() {
        context.historyDataStore.edit { preferences ->
            preferences[HISTORY_JSON_KEY] = "[]"
        }
    }

    fun exportHistoryToCsv(context: Context, uri: Uri, queryHistory: List<HistoryItem>) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    writer.appendLine("timestamp,type,value,decoded")
                    queryHistory.forEach { item ->
                        val type = escapeCsvValue(item.type)
                        val value = escapeCsvValue(item.value)
                        val decoded = escapeCsvValue(item.decoded)
                        writer.appendLine("${item.timestamp},$type,$value,$decoded")
                    }
                    writer.flush()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun importHistoryFromCsv(context: Context, uri: Uri): List<HistoryItem> {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                HistoryCsvParser.parse(inputStream)
            } ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getAllHistoryItems(): List<HistoryItem> {
        val preferences = context.historyDataStore.data.first()
        val json = preferences[HISTORY_JSON_KEY] ?: "[]"
        return listAdapter.fromJson(json) ?: emptyList()
    }

    suspend fun saveImportedHistoryItems(items: List<HistoryItem>) {
        context.historyDataStore.edit { preferences ->
            val oldJson = preferences[HISTORY_JSON_KEY] ?: "[]"
            val oldList = listAdapter.fromJson(oldJson) ?: emptyList()
            val combinedList = (oldList + items).distinct()
            preferences[HISTORY_JSON_KEY] = listAdapter.toJson(combinedList)
        }
    }

    private fun escapeCsvValue(value: String): String {
        val needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n")
        val escapedValue = value.replace("\"", "\"\"")
        return if (needsQuotes) "\"$escapedValue\"" else escapedValue
    }
}