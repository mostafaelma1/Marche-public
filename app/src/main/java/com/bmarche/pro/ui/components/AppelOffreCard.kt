package com.bmarche.pro.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.bmarche.pro.R
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.ui.Format
import com.bmarche.pro.ui.icone
import com.bmarche.pro.ui.theme.PrixAgressif
import com.bmarche.pro.ui.theme.PrixCompetitif
import com.bmarche.pro.ui.theme.PrixDanger

/**
 * Carte de marché — style sobre : surface blanche à liseré fin, un seul accent (bleu),
 * l'échéance restant le seul signal coloré (vert / orange / rouge).
 */
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
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        ao.domaine.icone(),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column(Modifier.weight(1f)) {
                    Text(
                        text = ao.reference,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = ao.objet,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2
                    )
                }
                IconButton(onClick = onToggleFavori) {
                    Icon(
                        imageVector = if (estFavori) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (estFavori) stringResource(R.string.fav_retirer) else stringResource(R.string.fav_ajouter),
                        tint = if (estFavori) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.outline
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ao.acheteur,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
                Icon(
                    Icons.Filled.LocationOn, contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = ao.ville,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        stringResource(R.string.estimation),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = Format.dh(ao.estimationDh),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
                val couleurEcheance = when {
                    jours <= 3 -> PrixDanger
                    jours <= 7 -> PrixAgressif
                    else -> PrixCompetitif
                }
                Badge(
                    texte = if (jours == 0L) stringResource(R.string.dernier_jour) else stringResource(R.string.jours_restants, jours),
                    couleurFond = couleurEcheance.copy(alpha = 0.10f),
                    couleurTexte = couleurEcheance
                )
            }
        }
    }
}
