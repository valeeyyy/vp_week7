package com.valerie.vp_week7.soal1.data.remote

import com.valerie.vp_week7.BuildConfig
import com.valerie.vp_week7.soal1.data.repository.CardRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object ServiceLocator {

    private const val BASE_URL = "https://www.pokemonpricetracker.com/api/v2/"

    private const val API_KEY = "pokeprice_free_cd7ee20c362fceb926e6bcce6d996d4c21dc4147a315efe1"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(API_KEY))

            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
                else HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }

    private val service: PokemonPriceService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PokemonPriceService::class.java)
    }

    val cardRepository: CardRepository by lazy { CardRepository(service) }
}
