package com.tenshi18.imeiimsidecoder.history.presentation.screens

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryItemRow(item: HistoryItem) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val resultString = remember(item) {
        buildString {
            append("Время: ${DateFormat.getDateTimeInstance().format(Date(item.timestamp))}\n")
            append("Тип: ${item.type}\n")
            append("Значение: ${item.value}\n")
            append("Результат:\n${item.decoded}")
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .combinedClickable(
            onClick = {},
            onLongClick = {
                clipboardManager.setText(AnnotatedString(resultString))
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                    Toast.makeText(context, "Скопировано", Toast.LENGTH_SHORT).show()
                }
            }
        )
    ) {
        resultString.lines().forEach { line ->
            Text(text = line)
        }
    }
}