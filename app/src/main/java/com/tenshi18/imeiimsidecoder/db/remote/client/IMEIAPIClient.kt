package com.tenshi18.imeiimsidecoder.db.remote.client

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tenshi18.imeiimsidecoder.db.remote.service.IMEIAPIService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object IMEIAPIClient{
    private const val BASE_URL = "https://alpha.imeicheck.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val IMEIService: IMEIAPIService = retrofit.create(IMEIAPIService::class.java)
}