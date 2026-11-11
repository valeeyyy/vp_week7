package com.valerie.vp_week7.soal1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val PokeColorScheme = darkColorScheme(
    primary = PokeRed,
    onPrimary = PokeTextPrimary,
    secondary = PriceGreen,
    tertiary = PriceYellow,
    background = PokeBackground,
    onBackground = PokeTextPrimary,
    surface = PokeBackground,
    onSurface = PokeTextPrimary,
    surfaceVariant = PokeSurface,
    onSurfaceVariant = PokeTextSecondary,
    surfaceContainer = PokeSurface,
    surfaceContainerHigh = PokeSurfaceHigh,
    outline = PokeOutline,
    outlineVariant = PokeDivider,
    error = PokeRed,
)

@Composable
fun PokeTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PokeColorScheme,
        typography = PokeTypography,
        content = content,
    )
}
