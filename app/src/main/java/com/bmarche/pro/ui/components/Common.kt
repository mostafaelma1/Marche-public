package com.bmarche.pro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** Petite étiquette colorée (badge) pour statut, domaine, échéance, etc. */
@Composable
fun Badge(
    texte: String,
    couleurFond: Color,
    couleurTexte: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(couleurFond)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = texte,
            style = MaterialTheme.typography.labelMedium,
            color = couleurTexte
        )
    }
}

/** Ligne clé/valeur utilisée dans les fiches détail. */
@Composable
fun LigneInfo(label: String, valeur: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = valeur,
            style = MaterialTheme.typography.bodyMedium.copy(),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End
        )
    }
}

/** État vide générique avec icône, titre et sous-titre. */
@Composable
fun EtatVide(
    icone: ImageVector,
    titre: String,
    sousTitre: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(titre, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            Text(
                sousTitre,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}
