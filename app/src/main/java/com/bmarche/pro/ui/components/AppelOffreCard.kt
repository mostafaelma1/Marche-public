package com.bmarche.pro.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.ui.Format
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
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = ao.reference,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = ao.objet,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                IconButton(onClick = onToggleFavori) {
                    Icon(
                        imageVector = if (estFavori) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (estFavori) "Retirer des favoris" else "Ajouter aux favoris",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Text(
                text = ao.acheteur,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Badge(
                    texte = ao.domaine.labelFr,
                    couleurFond = MaterialTheme.colorScheme.primaryContainer,
                    couleurTexte = MaterialTheme.colorScheme.onPrimaryContainer
                )
                IconLabel(icon = { Icon(Icons.Filled.LocationOn, null, tint = it) }, texte = ao.ville)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Format.dh(ao.estimationDh),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
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

@Composable
private fun IconLabel(icon: @Composable (androidx.compose.ui.graphics.Color) -> Unit, texte: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        icon(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Text(
            text = texte,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
        )
    }
}
