package com.valerie.vp_week7.soal1.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $apiKey")
            .build()
        return chain.proceed(request)
    }
}
