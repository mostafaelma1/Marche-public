package com.bmarche.pro.ui.theme

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
    tertiary = VertMarcheClair,
    background = SableClair,
    onBackground = AnthraciteText,
    surface = Color.White,
    onSurface = AnthraciteText,
    surfaceVariant = SurfaceVariantClair,
    onSurfaceVariant = Color(0xFF44504A),
    outline = OutlineClair,
    outlineVariant = Color(0xFFE1E7E2)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF6FD0AA),
    onPrimary = Color(0xFF003823),
    primaryContainer = Color(0xFF0A5238),
    onPrimaryContainer = VertMarcheContainer,
    secondary = Color(0xFFFFB59A),
    onSecondary = Color(0xFF5A1B00),
    secondaryContainer = Color(0xFF8A2C00),
    onSecondaryContainer = TerracottaContainer,
    background = Color(0xFF11150F),
    onBackground = Color(0xFFE2E3DD),
    surface = Color(0xFF1B211C),
    onSurface = Color(0xFFE2E3DD),
    surfaceVariant = Color(0xFF3A433C),
    onSurfaceVariant = Color(0xFFBFC9C0),
    outline = Color(0xFF5A655C)
)

@Composable
fun BMarcheProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = BMarcheTypography,
        shapes = BMarcheShapes,
        content = content
    )
}
