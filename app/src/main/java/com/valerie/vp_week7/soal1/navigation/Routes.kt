package com.valerie.vp_week7.soal1.navigation

import kotlinx.serialization.Serializable

@Serializable
data object CardListRoute

@Serializable
data class CardDetailRoute(val cardId: String, val tcgPlayerId: String)
