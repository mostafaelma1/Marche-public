package com.bmarche.pro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.ui.Format
import com.bmarche.pro.ui.couleur
import com.bmarche.pro.ui.icone
import com.bmarche.pro.ui.theme.PrixAgressif
import com.bmarche.pro.ui.theme.PrixCompetitif
import com.bmarche.pro.ui.theme.PrixDanger

@Composable
fun AppelOffreCard(
    ao: AppelOffre,
    estFavori: Boolean,
    onClick: () -> Unit,
    onToggleFavori: () -> Unit,
    modifier: Modifier = Modifier
) {
    val jours = Format.joursRestants(ao.dateLimiteEpoch)
    val accent = ao.domaine.couleur()

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Pastille domaine.
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accent.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(ao.domaine.icone(), contentDescription = null, tint = accent)
                }
                Column(Modifier.weight(1f)) {
                    Text(
                        text = ao.reference,
                        style = MaterialTheme.typography.labelMedium,
                        color = accent,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = ao.objet,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2
                    )
                }
                IconButton(onClick = onToggleFavori) {
                    Icon(
                        imageVector = if (estFavori) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (estFavori) "Retirer des favoris" else "Ajouter aux favoris",
                        tint = if (estFavori) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = ao.acheteur,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Badge(
                    texte = ao.domaine.labelFr,
                    couleurFond = accent.copy(alpha = 0.12f),
                    couleurTexte = accent
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.LocationOn, contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = ao.ville,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Estimation",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = Format.dh(ao.estimationDh),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                val couleurEcheance = when {
                    jours <= 3 -> PrixDanger
                    jours <= 7 -> PrixAgressif
                    else -> PrixCompetitif
                }
                Badge(
                    texte = if (jours == 0L) "Dernier jour" else "J-$jours",
                    couleurFond = couleurEcheance.copy(alpha = 0.12f),
                    couleurTexte = couleurEcheance
                )
            }
        }
    }
}
