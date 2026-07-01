package com.bmarche.pro.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * Système de couleurs « enterprise » — bleu de confiance (#0D6EFD) + vert de réussite
 * (#2ECC71), sur des surfaces claires et neutres. Palette tonale Material 3 complète,
 * pensée pour la lisibilité financière et un rendu premium et sobre.
 */
private val LightColors = lightColorScheme(
    primary = Color(0xFF0D6EFD),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD8E6FF),
    onPrimaryContainer = Color(0xFF001B45),
    secondary = Color(0xFF2ECC71),
    onSecondary = Color(0xFF00391C),
    secondaryContainer = Color(0xFFB7F5CF),
    onSecondaryContainer = Color(0xFF00210F),
    tertiary = Color(0xFF475569),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFDCE3EC),
    onTertiaryContainer = Color(0xFF0F172A),
    error = Color(0xFFEF4444),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF7F1D1D),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF111827),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF111827),
    surfaceVariant = Color(0xFFEEF2F7),
    onSurfaceVariant = Color(0xFF6B7280),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF8FAFC),
    surfaceContainer = Color(0xFFF1F5F9),
    surfaceContainerHigh = Color(0xFFE9EEF4),
    surfaceContainerHighest = Color(0xFFE2E8F0),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE5E7EB),
    inverseSurface = Color(0xFF1F2937),
    inverseOnSurface = Color(0xFFF9FAFB),
    inversePrimary = Color(0xFFAAC7FF),
    scrim = Color(0xFF000000)
)

/** Schéma sombre correspondant (bleu nuit sobre). */
private val DarkColors = darkColorScheme(
    primary = Color(0xFF5B9DFF),
    onPrimary = Color(0xFF002E6E),
    primaryContainer = Color(0xFF00458C),
    onPrimaryContainer = Color(0xFFD8E6FF),
    secondary = Color(0xFF6FE0A0),
    onSecondary = Color(0xFF00391C),
    secondaryContainer = Color(0xFF0C5A31),
    onSecondaryContainer = Color(0xFFB7F5CF),
    tertiary = Color(0xFFAEBCCF),
    onTertiary = Color(0xFF1C2A3A),
    tertiaryContainer = Color(0xFF334155),
    onTertiaryContainer = Color(0xFFDCE3EC),
    error = Color(0xFFFCA5A5),
    onError = Color(0xFF7F1D1D),
    errorContainer = Color(0xFF991B1B),
    onErrorContainer = Color(0xFFFEE2E2),
    background = Color(0xFF0B1220),
    onBackground = Color(0xFFE5E7EB),
    surface = Color(0xFF0F172A),
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
    surfaceContainerLowest = Color(0xFF0A0F1A),
    surfaceContainerLow = Color(0xFF111827),
    surfaceContainer = Color(0xFF1E293B),
    surfaceContainerHigh = Color(0xFF24324A),
    surfaceContainerHighest = Color(0xFF2C3B54),
    outline = Color(0xFF475569),
    outlineVariant = Color(0xFF334155),
    inverseSurface = Color(0xFFE5E7EB),
    inverseOnSurface = Color(0xFF1F2937),
    inversePrimary = Color(0xFF0D6EFD),
    scrim = Color(0xFF000000)
)

/**
 * Thème de l'application.
 *
 * @param dynamicColor désactivé par défaut : l'identité de marque (bleu/vert) prime,
 *   pour un rendu cohérent et professionnel sur tous les appareils.
 */
@Composable
fun BMarcheProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = BMarcheTypography,
        shapes = BMarcheShapes,
        content = content
    )
}
