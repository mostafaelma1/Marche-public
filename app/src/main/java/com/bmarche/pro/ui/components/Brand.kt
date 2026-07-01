package com.bmarche.pro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Dégradé de marque dérivé de la couleur primaire du thème (compatible Material You). */
@Composable
fun brandGradient(): Brush {
    val primary = MaterialTheme.colorScheme.primary
    return Brush.linearGradient(listOf(primary, lerp(primary, Color.Black, 0.32f)))
}

/** Bandeau de marque dégradé, utilisé en tête des écrans principaux. */
@Composable
fun HeroHeader(
    titre: String,
    sousTitre: String,
    modifier: Modifier = Modifier,
    contenu: (@Composable () -> Unit)? = null
) {
    val onColor = MaterialTheme.colorScheme.onPrimary
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(brandGradient())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = titre,
            style = MaterialTheme.typography.headlineSmall,
            color = onColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = sousTitre,
            style = MaterialTheme.typography.bodyMedium,
            color = onColor.copy(alpha = 0.85f)
        )
        contenu?.let {
            Row(
                modifier = Modifier.padding(top = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) { it() }
        }
    }
}

/** Petite statistique affichée dans le bandeau (chiffre + libellé). */
@Composable
fun StatPill(valeur: String, libelle: String, modifier: Modifier = Modifier) {
    val onColor = MaterialTheme.colorScheme.onPrimary
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(onColor.copy(alpha = 0.18f))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(valeur, style = MaterialTheme.typography.titleMedium, color = onColor, fontWeight = FontWeight.Bold)
        Text(libelle, style = MaterialTheme.typography.labelMedium, color = onColor.copy(alpha = 0.85f))
    }
}

/** Titre de section avec petit liseré coloré. */
@Composable
fun SectionTitle(texte: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            Modifier
                .width(4.dp)
                .height(18.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.primary)
        )
        Text(texte, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    }
}
