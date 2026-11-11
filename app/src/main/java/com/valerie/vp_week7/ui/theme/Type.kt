package com.valerie.vp_week7.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Base = FontFamily.Default

val Typography = Typography(
    // "Coldplay" di hero card
    displaySmall = TextStyle(fontFamily = Base, fontWeight = FontWeight.Bold, fontSize = 30.sp, lineHeight = 36.sp, letterSpacing = (-0.5).sp),
    // Judul album di detail
    headlineSmall = TextStyle(fontFamily = Base, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 30.sp, letterSpacing = (-0.3).sp),
    // "Music Artist", "Diskografi Album", "Detail Album"
    titleLarge = TextStyle(fontFamily = Base, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = (-0.3).sp),
    // "Tentang Artis", "Deskripsi Album"
    titleMedium = TextStyle(fontFamily = Base, fontWeight = FontWeight.Bold, fontSize = 17.sp, lineHeight = 24.sp),
    // Judul album di grid / judul lagu
    titleSmall = TextStyle(fontFamily = Base, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 20.sp, letterSpacing = (-0.1).sp),
    bodyLarge = TextStyle(fontFamily = Base, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.3.sp),
    // Biografi & deskripsi
    bodyMedium = TextStyle(fontFamily = Base, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 22.sp, letterSpacing = 0.1.sp),
    bodySmall = TextStyle(fontFamily = Base, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = 0.2.sp),
    labelLarge = TextStyle(fontFamily = Base, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.3.sp),
    labelMedium = TextStyle(fontFamily = Base, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = 0.4.sp),
    labelSmall = TextStyle(fontFamily = Base, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.3.sp),
)
