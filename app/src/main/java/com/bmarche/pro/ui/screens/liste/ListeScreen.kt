package com.bmarche.pro.ui.screens.liste

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmarche.pro.data.model.Domaine
import com.bmarche.pro.ui.components.AppelOffreCard
import com.bmarche.pro.ui.repositoryViewModel

@Composable
fun ListeScreen(
    onOuvrirDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val vm = repositoryViewModel { ListeViewModel(it) }
    val state by vm.state.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp,
            top = 12.dp + contentPadding.calculateTopPadding(),
            bottom = 12.dp + contentPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = state.recherche,
                onValueChange = vm::onRecherche,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                placeholder = { Text("Rechercher un marché, un acheteur…") }
            )
        }

        item {
            FiltresDomaine(selection = state.domaine, onSelection = vm::onDomaine)
        }

        if (state.villes.isNotEmpty()) {
            item {
                FiltresVille(
                    villes = state.villes,
                    selection = state.ville,
                    onSelection = vm::onVille
                )
            }
        }

        if (state.recommandes.isNotEmpty()) {
            item {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.NotificationsActive,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "  Recommandés pour vous (${state.recommandes.size})",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            items(state.recommandes, key = { "reco-${it.id}" }) { ao ->
                AppelOffreCard(
                    ao = ao,
                    estFavori = ao.id in state.favorisIds,
                    onClick = { onOuvrirDetail(ao.id) },
                    onToggleFavori = { vm.basculerFavori(ao.id) }
                )
            }
            item {
                Text(
                    text = "Tous les marchés (${state.resultats.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        items(state.resultats, key = { it.id }) { ao ->
            AppelOffreCard(
                ao = ao,
                estFavori = ao.id in state.favorisIds,
                onClick = { onOuvrirDetail(ao.id) },
                onToggleFavori = { vm.basculerFavori(ao.id) }
            )
        }

        if (state.resultats.isEmpty()) {
            item {
                Text(
                    text = "Aucun marché ne correspond à ces filtres.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun FiltresDomaine(selection: Domaine?, onSelection: (Domaine?) -> Unit) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selection == null,
            onClick = { onSelection(null) },
            label = { Text("Tous") }
        )
        Domaine.entries.forEach { d ->
            FilterChip(
                selected = selection == d,
                onClick = { onSelection(if (selection == d) null else d) },
                label = { Text(d.labelFr) }
            )
        }
    }
}

@Composable
private fun FiltresVille(villes: List<String>, selection: String?, onSelection: (String?) -> Unit) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selection == null,
            onClick = { onSelection(null) },
            label = { Text("Toutes les villes") }
        )
        villes.forEach { v ->
            FilterChip(
                selected = selection == v,
                onClick = { onSelection(if (selection == v) null else v) },
                label = { Text(v) }
            )
        }
    }
}
