package com.valerie.vp_week7.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Aplikasi selalu memakai tema gelap (sesuai desain), tanpa dynamic color.
private val MusicColorScheme = darkColorScheme(
    primary = GreenAccent,
    onPrimary = OnGreen,
    primaryContainer = GreenContainer,
    onPrimaryContainer = GreenAccent,
    secondary = GreenAccent,
    background = Background,
    onBackground = TextPrimary,
    surface = Background,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceDark,
    onSurfaceVariant = TextSecondary,
    surfaceContainer = SurfaceDark,
    surfaceContainerHigh = SurfaceElevated,
    outline = OutlineDark,
    outlineVariant = DividerDark,
    error = ErrorRed,
)

@Composable
fun Vp_week7Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MusicColorScheme,
        typography = Typography,
        content = content,
    )
}
