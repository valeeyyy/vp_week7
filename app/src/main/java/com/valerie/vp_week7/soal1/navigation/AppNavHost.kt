package com.valerie.vp_week7.soal1.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.valerie.vp_week7.soal1.ui.screen.CardDetailScreen
import com.valerie.vp_week7.soal1.ui.screen.CardListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = CardListRoute) {
        composable<CardListRoute>(
            exitTransition = { fadeOut(tween(250)) },
            popEnterTransition = { fadeIn(tween(250)) },
        ) {
            CardListScreen(
                onCardClick = { card -> navController.navigate(CardDetailRoute(card.id, card.tcgPlayerId)) },
            )
        }

        composable<CardDetailRoute>(
            enterTransition = { slideInVertically(tween(320)) { it } },
            popExitTransition = { slideOutVertically(tween(280)) { it } },
        ) {
            CardDetailScreen(
                onClose = { navController.navigateUp() },
            )
        }
    }
}
