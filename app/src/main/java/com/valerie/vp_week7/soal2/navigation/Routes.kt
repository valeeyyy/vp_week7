package com.valerie.vp_week7.soal2.navigation

import kotlinx.serialization.Serializable

@Serializable
data object ArtistRoute

@Serializable
data class AlbumDetailRoute(val albumId: String)
