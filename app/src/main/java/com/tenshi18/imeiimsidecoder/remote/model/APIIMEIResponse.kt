package com.tenshi18.imeiimsidecoder.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class APIIMEIResponse(
    val status: String,
    val result: String,
    val imei: String,
    @Json(name = "count_free_checks_today")
    val countFreeChecksToday: Int,
    val readPerformance: String,
    @Json(name = "object")
    val objectInfo: APIIMEIObject
)

@JsonClass(generateAdapter = true)
data class APIIMEIObject(
    val brand: String,
    @Json(name = "name")
    val modelName: String,
    val model: String
)