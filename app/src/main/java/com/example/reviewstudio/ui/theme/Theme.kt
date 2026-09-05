package com.example.reviewstudio.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TikTokPink,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3B101E),
    onPrimaryContainer = Color(0xFFFFD8E0),
    secondary = ShopeeOrange,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF381A12),
    onSecondaryContainer = Color(0xFFFFDBCF),
    tertiary = CyberCyan,
    onTertiary = Color.Black,
    background = DarkBg,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = DarkBorder
)

@Composable
fun AIReviewStudioTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
