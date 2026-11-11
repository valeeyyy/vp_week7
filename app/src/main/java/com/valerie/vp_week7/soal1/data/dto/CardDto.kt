package com.valerie.vp_week7.soal1.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CardListResponseDto(
    val data: List<CardDto> = emptyList(),
)

@Serializable
data class CardDto(
    val id: String,
    val tcgPlayerId: String? = null,
    val name: String,
    val setName: String? = null,
    val cardNumber: String? = null,
    val rarity: String? = null,
    val hp: String? = null,
    val pokemonType: String? = null,
    val energyType: List<String>? = null,
    val artist: String? = null,
    val imageUrl: String? = null,
    val imageCdnUrl400: String? = null,
    val imageCdnUrl800: String? = null,
    val prices: PricesDto? = null,
)

@Serializable
data class PricesDto(
    val market: Double? = null,
    val low: Double? = null,
    val sellers: Int? = null,
    val listings: Int? = null,
    val primaryPrinting: String? = null,
)
