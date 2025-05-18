package com.tenshi18.imeiimsidecoder.db.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class APIIMEIResponse(
    @Json(name = "modelBrandName")
    val modelBrandName: String
)