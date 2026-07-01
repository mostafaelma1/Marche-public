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

// Sémantique du positionnement de prix / échéances (feux tricolores sobres).
val PrixDanger = Color(0xFFEF4444)
val PrixAgressif = Color(0xFFF97316)
val PrixCompetitif = Color(0xFF059669)
val PrixHaut = Color(0xFFCA8A04)

/**
 * Accents catégoriels sobres (famille bleu / teal / indigo / slate) — professionnels,
 * harmonieux, sans couleurs criardes, pour les pastilles de région.
 */
val RegionColors = listOf(
    Color(0xFF0D6EFD), Color(0xFF2563EB), Color(0xFF0EA5E9), Color(0xFF0891B2),
    Color(0xFF14B8A6), Color(0xFF10B981), Color(0xFF6366F1), Color(0xFF7C3AED),
    Color(0xFF1D4ED8), Color(0xFF0F766E), Color(0xFF4F46E5), Color(0xFF475569)
)

/** Accent associé à chaque domaine (chips, liserés) — palette maîtrisée. */
val DomaineColors = listOf(
    Color(0xFF0D6EFD), // restauration
    Color(0xFF0EA5E9), // nettoyage
    Color(0xFFF97316), // travaux
    Color(0xFF6366F1), // fournitures
    Color(0xFF0891B2), // transport
    Color(0xFF7C3AED), // gardiennage
    Color(0xFF14B8A6), // informatique
    Color(0xFF10B981)  // espaces verts
)
