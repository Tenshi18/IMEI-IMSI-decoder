package com.tenshi18.imeiimsidecoder.history.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tenshi18.imeiimsidecoder.history.domain.model.HistoryItem
import com.tenshi18.imeiimsidecoder.history.presentation.viewmodels.HistoryViewModel
import java.text.DateFormat
import java.util.Date

@Composable
fun HistoryScreen(historyViewModel: HistoryViewModel) {

    val historyList by historyViewModel.historyFlow.collectAsState()

    if (historyList.isEmpty()) {
        Box(
            contentAlignment = androidx.compose.ui.Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ){
            Text("История пуста")
        }
    } else {
        Column {
            LazyColumn {
                items(historyList) { item ->
                    HistoryItemRow(item)
                }
            }
        }
    }
}

@Composable
fun HistoryItemRow(item: HistoryItem) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Тип: ${item.type}")
        Text(text = "Значение: ${item.value}")
        Text(text = "Результат: ${item.decoded}")
        Text(text = "Время: ${DateFormat.getDateTimeInstance().format(Date(item.timestamp))}")
    }
}