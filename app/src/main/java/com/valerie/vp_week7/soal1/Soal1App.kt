package com.valerie.vp_week7.soal1

import androidx.compose.runtime.Composable
import com.valerie.vp_week7.soal1.navigation.AppNavHost
import com.valerie.vp_week7.soal1.ui.theme.PokeTrackerTheme

@Composable
fun Soal1App() {
    PokeTrackerTheme {
        AppNavHost()
    }
}
