package com.valerie.vp_week7.soal1.model

import java.util.Locale

data class PokemonCard(
    val id: String,
    val tcgPlayerId: String,
    val name: String,
    val setName: String,
    val cardNumber: String,
    val rarity: String,
    val hp: String?,
    val type: String,
    val artist: String,
    val imageUrl: String?,
    val imageLargeUrl: String?,
    val marketPrice: Double?,
    val lowPrice: Double?,
    val listings: Int?,
    val sellers: Int?,
    val primaryPrinting: String,
)

fun Double?.toUsd(): String = this?.let { String.format(Locale.US, "$%,.2f", it) } ?: "—"
