package com.bmarche.pro.ui.screens.accueil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.bmarche.pro.R
import com.bmarche.pro.ui.label
import com.bmarche.pro.BMarcheApplication
import com.bmarche.pro.data.model.Region
import com.bmarche.pro.data.model.TypePublication
import com.bmarche.pro.data.repository.FiltreAppelOffre
import com.bmarche.pro.ui.Format

/**
 * Page d'accueil — style « enterprise » sobre : une seule couleur d'accent (bleu),
 * surfaces blanches à liseré fin. Les catégories de publications sont accessibles
 * via le menu latéral, l'accueil reste dédié aux régions.
 */
@Composable
fun AccueilScreen(
    onOuvrirRegion: (Region) -> Unit,
    onOuvrirTous: () -> Unit,
    onOuvrirMenu: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val app = LocalContext.current.applicationContext as BMarcheApplication
    val total = app.repository.totalMarches()
    val comptes = app.repository.comptesParRegion()
    val urgents = app.repository
        .appelsOffres(FiltreAppelOffre(type = TypePublication.MARCHE_PUBLIC))
        .count { Format.joursRestants(it.dateLimiteEpoch) <= 7 }
    val regions = Region.entries.sortedByDescending { comptes[it] ?: 0 }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp,
            top = 8.dp + contentPadding.calculateTopPadding(),
            bottom = 24.dp + contentPadding.calculateBottomPadding()
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // En-tête sobre + accès au menu latéral.
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(R.drawable.logo_bmarche),
                    contentDescription = null,
                    modifier = Modifier.size(38.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.accueil_titre),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        stringResource(R.string.accueil_sous_titre),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onOuvrirMenu) {
                    Icon(
                        Icons.Filled.Menu,
                        contentDescription = stringResource(R.string.menu),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Barre de recherche (ouvre la liste globale).
        item(span = { GridItemSpan(maxLineSpan) }) {
            Surface(
                onClick = onOuvrirTous,
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        stringResource(R.string.recherche_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Indicateurs clés, compacts.
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("$total", stringResource(R.string.stat_marches_actifs), Modifier.weight(1f))
                StatCard("$urgents", stringResource(R.string.stat_cloture_7j), Modifier.weight(1f))
                StatCard("${Region.entries.size}", stringResource(R.string.stat_regions), Modifier.weight(1f))
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) { SectionLabel(stringResource(R.string.section_regions)) }

        items(regions, key = { it.name }) { region ->
            RegionCard(
                region = region,
                compte = comptes[region] ?: 0,
                onClick = { onOuvrirRegion(region) }
            )
        }
    }
}

/** Libellé de section discret, en petites capitales espacées. */
@Composable
private fun SectionLabel(texte: String, topPadding: androidx.compose.ui.unit.Dp = 4.dp) {
    Text(
        texte,
        modifier = Modifier.padding(top = topPadding),
        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

/** Indicateur compact : chiffre + libellé, sur carte blanche à liseré fin. */
@Composable
private fun StatCard(valeur: String, libelle: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Text(
                valeur,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                libelle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** Carte région : nom + nombre de marchés. Sobre, sans icône ni couleur criarde. */
@Composable
private fun RegionCard(region: Region, compte: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(Modifier.padding(14.dp).fillMaxWidth()) {
            Text(
                region.label(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "$compte",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    if (compte == 1) stringResource(R.string.n_marche) else stringResource(R.string.n_marches),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
