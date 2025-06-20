package com.tenshi18.imeiimsidecoder.history.presentation.viewmodels

import android.content.Context
import android.net.Uri
// import androidx.compose.foundation.layout.add // Не используется здесь
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tenshi18.imeiimsidecoder.history.domain.model.HistoryItem
import com.tenshi18.imeiimsidecoder.history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.IOException // Для обработки ошибок ввода-вывода
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    val historyFlow: StateFlow<List<HistoryItem>> = historyRepository.historyFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun clearHistory() {
        viewModelScope.launch {
            historyRepository.clearHistory()
        }
    }

    // Функция для сохранения импортированных элементов (если нужно)
    fun saveImportedHistoryItems(items: List<HistoryItem>) {
        viewModelScope.launch {
            items.forEach { historyRepository.addHistoryItem(it) }
        }
    }

    fun exportHistoryToCsv(context: Context, uri: Uri, queryHistory: List<HistoryItem>) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    // Заголовок CSV
                    writer.appendLine("timestamp,type,value,decoded") // Используем appendLine для добавления новой строки

                    // Записываем каждый элемент истории
                    queryHistory.forEach { item ->
                        // Экранируем значения, если они могут содержать запятые или кавычки.
                        // Простой пример: заменяем кавычки на двойные кавычки и оборачиваем значение в кавычки, если оно содержит запятую или кавычку.
                        val type = escapeCsvValue(item.type)
                        val value = escapeCsvValue(item.value)
                        val decoded = escapeCsvValue(item.decoded) // Обрабатываем nullable decoded

                        writer.appendLine("${item.timestamp},$type,$value,$decoded")
                    }
                    writer.flush() // Вызываем flush один раз в конце
                }
            }
            println("История запросов экспортирована в $uri")
        } catch (e: IOException) { // Более конкретный тип исключения
            e.printStackTrace()
            // Здесь можно добавить логику для отображения ошибки пользователю
            println("Ошибка экспорта истории: ${e.message}")
        } catch (e: Exception) { // Общий обработчик для других неожиданных ошибок
            e.printStackTrace()
            println("Неизвестная ошибка при экспорте истории: ${e.message}")
        }
    }

    // Вспомогательная функция для экранирования значений CSV
    private fun escapeCsvValue(value: String): String {
        val needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n")
        val escapedValue = value.replace("\"", "\"\"") // Заменяем " на ""
        return if (needsQuotes) "\"$escapedValue\"" else escapedValue
    }

    fun importHistoryFromCsv(context: Context, uri: Uri): List<HistoryItem> {
        val importedItems = mutableListOf<HistoryItem>()
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    // Пропускаем заголовок
                    reader.readLine()

                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        // Используем более надежное разделение для CSV, которое учитывает значения в кавычках
                        val tokens = line!!.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
                            .map { it.trim().removeSurrounding("\"").replace("\"\"", "\"") } // Удаляем внешние кавычки и восстанавливаем внутренние

                        if (tokens.size >= 4) { // Убедимся, что у нас достаточно столбцов (timestamp, type, value, decoded)
                            try {
                                val timestamp = tokens[0].toLong()
                                val type = tokens[1]
                                val value = tokens[2]
                                val decoded = tokens.getOrNull(3) ?: "" // decoded может отсутствовать или быть пустым

                                importedItems.add(HistoryItem(timestamp, type, value, decoded))
                            } catch (e: NumberFormatException) {
                                // Ошибка парсинга Long для timestamp
                                e.printStackTrace()
                                println("Ошибка парсинга строки CSV (timestamp): $line")
                                // Можно пропустить эту строку или предпринять другие действия
                            } catch (e: IndexOutOfBoundsException) {
                                // Недостаточно токенов после разделения строки
                                e.printStackTrace()
                                println("Ошибка парсинга строки CSV (недостаточно полей): $line")
                            }
                        } else {
                            println("Пропущена строка CSV с недостаточным количеством полей: $line")
                        }
                    }
                }
            }
            println("Импортировано ${importedItems.size} элементов из $uri")
        } catch (e: IOException) {
            e.printStackTrace()
            println("Ошибка импорта истории: ${e.message}")
            // Здесь можно добавить логику для отображения ошибки пользователю
        } catch (e: Exception) {
            e.printStackTrace()
            println("Неизвестная ошибка при импорте истории: ${e.message}")
        }
        return importedItems
    }
}