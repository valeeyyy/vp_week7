package com.valerie.movie_db.navigation

import kotlinx.serialization.Serializable

@Serializable
data object DashboardRoute

@Serializable
data class DetailRoute(val movieId: Int)