package com.tenshi18.imeiimsidecoder.remote.client

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tenshi18.imeiimsidecoder.remote.service.IMEIAPIService
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

object IMEIAPIClient {

    private const val BASE_URL = "https://alpha.imeicheck.com/"
    private const val CACHE_DIR_NAME = "http_cache"
    private const val CACHE_SIZE_BYTES = (10 * 1024 * 1024).toLong() // 10 MiB

    // Генерация сервиса с кэшем
    fun createService(context: Context): IMEIAPIService {
        // 1) Настраиваем кэш
        val cacheDir = File(context.cacheDir, CACHE_DIR_NAME)
        val cache = Cache(cacheDir, CACHE_SIZE_BYTES)

        // 2) Строим OkHttpClient
        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            // a) При отсутствии сети читаем только из кэша до 7 дней
            .addInterceptor { chain ->
                var request = chain.request()
                if (!isNetworkAvailable(context)) {
                    request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached")
                        .build()
                }
                chain.proceed(request)
            }
            // b) При наличии сети кэшируем ответ до переполнения кэша
            .addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                response.newBuilder()
                    .header("Cache-Control", "public, max-age=31536000") // 1 год в секундах
                    .build()
            }
            .build()

        // 3) Строим Retrofit
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(IMEIAPIService::class.java)
    }

    // Простая проверка наличия любого сетевого подключения
    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(nw) ?: return false
        return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}
