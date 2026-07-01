package com.bmarche.pro.ui.theme

import androidx.compose.ui.graphics.Color

// Palette de marque — vert institutionnel marocain + accent terracotta.
val VertMarche = Color(0xFF0E6B4F)
val VertMarcheClair = Color(0xFF4C9C7E)
val VertMarcheFonce = Color(0xFF073C2C)
val VertMarcheContainer = Color(0xFFCDEFDE)
val Terracotta = Color(0xFFC2410C)
val TerracottaContainer = Color(0xFFFFE0D2)
val SableClair = Color(0xFFF5F6F3)
val SurfaceVariantClair = Color(0xFFE7ECE7)
val OutlineClair = Color(0xFFD5DDD6)
val AnthraciteText = Color(0xFF16201B)

// Sémantique du positionnement de prix / échéances.
val PrixDanger = Color(0xFFB3261E)
val PrixAgressif = Color(0xFFC2410C)
val PrixCompetitif = Color(0xFF0E6B4F)
val PrixHaut = Color(0xFF8A6D00)

/** Couleur d'accent associée à chaque région (pastilles de la page d'accueil). */
val RegionColors = listOf(
    Color(0xFF0E6B4F), Color(0xFF2563EB), Color(0xFFC2410C), Color(0xFF7C3AED),
    Color(0xFF0891B2), Color(0xFFDB2777), Color(0xFF16A34A), Color(0xFFCA8A04),
    Color(0xFF4F46E5), Color(0xFF0D9488), Color(0xFFB91C1C), Color(0xFF9333EA)
)

/** Couleur d'accent associée à chaque domaine (pastilles, liserés de carte). */
val DomaineColors = listOf(
    Color(0xFF0E6B4F), // restauration
    Color(0xFF2563EB), // nettoyage
    Color(0xFFC2410C), // travaux
    Color(0xFF7C3AED), // fournitures
    Color(0xFF0891B2), // transport
    Color(0xFF9333EA), // gardiennage
    Color(0xFF0D9488), // informatique
    Color(0xFF16A34A)  // espaces verts
)
