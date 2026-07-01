package com.bmarche.pro.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmarche.pro.dossier.DossierGenerator
import com.bmarche.pro.share.WhatsApp
import com.bmarche.pro.ui.Format
import com.bmarche.pro.ui.components.Badge
import com.bmarche.pro.ui.components.LigneInfo
import com.bmarche.pro.ui.repositoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    aoId: String,
    onRetour: () -> Unit,
    onOuvrirChecklist: (String) -> Unit,
    onOuvrirPrix: (String) -> Unit,
    onOuvrirDocuments: (String) -> Unit,
    onOuvrirSociete: (String) -> Unit
) {
    val vm = repositoryViewModel { DetailViewModel(it, aoId) }
    val state by vm.state.collectAsStateWithLifecycle()
    val ao = state.ao
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ao?.reference ?: "Détail", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onRetour) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    if (ao != null) {
                        IconButton(onClick = vm::basculerFavori) {
                            Icon(
                                if (state.estFavori) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Favori",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (ao == null) {
            Text(
                "Marché introuvable.",
                modifier = Modifier.padding(padding).padding(24.dp)
            )
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(ao.objet, style = MaterialTheme.typography.headlineSmall)
            Badge(
                texte = ao.domaine.labelFr,
                couleurFond = MaterialTheme.colorScheme.primaryContainer,
                couleurTexte = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    LigneInfo("Acheteur", ao.acheteur)
                    LigneInfo("Ville", ao.ville)
                    LigneInfo("Estimation", Format.dh(ao.estimationDh))
                    LigneInfo("Caution provisoire", Format.dh(ao.cautionProvisoireDh))
                    LigneInfo("Date limite", Format.date(ao.dateLimiteEpoch))
                    LigneInfo("Échéance", "J-${Format.joursRestants(ao.dateLimiteEpoch)}")
                }
            }

            if (ao.descriptif.isNotBlank()) {
                Text("Objet du marché", style = MaterialTheme.typography.titleMedium)
                Text(ao.descriptif, style = MaterialTheme.typography.bodyLarge)
            }

            // Raccourcis vers les outils.
            FilledTonalButton(
                onClick = { onOuvrirPrix(ao.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Calculate, contentDescription = null)
                Text("  Analyser mon prix")
            }
            OutlinedButton(
                onClick = { onOuvrirChecklist(ao.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Checklist, contentDescription = null)
                Text("  Dossier administratif — ${state.piecesPretes}/${state.piecesTotal} prêt(s)")
            }
            FilledTonalButton(
                onClick = { DossierGenerator.telecharger(context, ao, state.etats) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Download, contentDescription = null)
                Text("  Télécharger le dossier (${DossierGenerator.formatLabel(ao)})")
            }
            FilledTonalButton(
                onClick = { onOuvrirDocuments(ao.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Description, contentDescription = null)
                Text("  Générer les documents (engagement, déclaration…)")
            }
            OutlinedButton(
                onClick = { WhatsApp.partager(context, WhatsApp.texteMarche(ao)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Chat, contentDescription = null)
                Text("  Partager sur WhatsApp")
            }

            // Analyse concurrence.
            Text("Analyse de la concurrence", style = MaterialTheme.typography.titleMedium)
            if (state.concurrents.isEmpty()) {
                Text(
                    "Pas encore de concurrent identifié sur ce type de marché.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            } else {
                state.rabaisConcurrentsMoyen?.let { moy ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(
                            "Rabais moyen des concurrents probables : ${Format.pct(moy)}",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
                state.concurrents.forEach { soc ->
                    Card(
                        onClick = { onOuvrirSociete(soc.id) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Row {
                                    Icon(
                                        Icons.Filled.Business,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Text("  ${soc.nom}", fontWeight = FontWeight.SemiBold)
                                }
                                Text(
                                    "${soc.marchesGagnes} marchés · rabais moyen ${Format.pct(soc.tauxRabaisMoyen)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
