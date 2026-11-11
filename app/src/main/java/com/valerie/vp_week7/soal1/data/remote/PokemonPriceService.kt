package com.valerie.vp_week7.soal1.data.remote

import com.valerie.vp_week7.soal1.data.dto.CardListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonPriceService {

    @GET("cards")
    suspend fun searchCards(
        @Query("search") query: String,
        @Query("limit") limit: Int = 20,
        @Query("language") language: String = "english",
    ): CardListResponseDto

    @GET("cards")
    suspend fun getCardByTcgPlayerId(
        @Query("tcgPlayerId") tcgPlayerId: String,
        @Query("language") language: String = "english",
    ): CardListResponseDto
}
