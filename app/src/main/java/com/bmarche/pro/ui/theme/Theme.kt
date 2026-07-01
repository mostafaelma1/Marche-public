package com.bmarche.pro.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = VertMarche,
    onPrimary = Color.White,
    primaryContainer = VertMarcheContainer,
    onPrimaryContainer = Color(0xFF00210F),
    secondary = Terracotta,
    onSecondary = Color.White,
    secondaryContainer = TerracottaContainer,
    onSecondaryContainer = Color(0xFF3B0900),
    background = SableClair,
    onBackground = AnthraciteText,
    surface = Color.White,
    onSurface = AnthraciteText
)

private val DarkColors = darkColorScheme(
    primary = VertMarcheClair,
    onPrimary = Color(0xFF003823),
    primaryContainer = Color(0xFF0A4A37),
    onPrimaryContainer = VertMarcheContainer,
    secondary = Color(0xFFFFB59A),
    secondaryContainer = Color(0xFF8A2C00),
    background = Color(0xFF14140F),
    surface = Color(0xFF1F1F1A)
)

@Composable
fun BMarcheProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = BMarcheTypography,
        content = content
    )
}
