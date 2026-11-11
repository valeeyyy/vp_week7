package com.valerie.vp_week7.data.remote

import com.valerie.vp_week7.BuildConfig
import com.valerie.vp_week7.data.repository.MusicRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Tempat membuat objek-objek singleton (Retrofit, Service, Repository) secara manual.
 * Pada project besar bagian ini biasanya diganti Hilt/Koin.
 */
object ServiceLocator {

    private val BASE_URL = "https://www.theaudiodb.com/api/v1/json/${BuildConfig.AUDIODB_API_KEY}/"

    private val json = Json {
        ignoreUnknownKeys = true   // abaikan field yang tidak dipakai
        coerceInputValues = true   // null pada field non-null -> pakai default
        explicitNulls = false
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
                else HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }

    private val service: AudioDbService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(AudioDbService::class.java)
    }

    val musicRepository: MusicRepository by lazy { MusicRepository(service) }
}
