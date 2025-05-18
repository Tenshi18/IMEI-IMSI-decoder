package com.tenshi18.imeiimsidecoder.remote.service

import retrofit2.http.GET
import retrofit2.http.Query
import com.tenshi18.imeiimsidecoder.remote.model.APIIMEIResponse

interface IMEIAPIService {
    @GET("api/modelBrandName")
    suspend fun getModelBrandName(
        @Query("imei") imei: String,
        @Query("format") format: String = "json"
    ): APIIMEIResponse
}