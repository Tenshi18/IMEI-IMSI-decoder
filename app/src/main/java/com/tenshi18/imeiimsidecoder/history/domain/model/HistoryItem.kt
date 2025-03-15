package com.tenshi18.imeiimsidecoder.history.domain.model

data class HistoryItem(
    val type: String,
    val value: String,
    val decoded: String,
    val timestamp: Long
)
