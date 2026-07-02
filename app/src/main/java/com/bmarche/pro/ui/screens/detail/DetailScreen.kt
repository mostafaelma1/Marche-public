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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Print
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.compose.ui.res.stringResource
import com.bmarche.pro.R
import com.bmarche.pro.ui.label
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
                stringResource(R.string.detail_introuvable),
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
                texte = ao.domaine.label(),
                couleurFond = MaterialTheme.colorScheme.primaryContainer,
                couleurTexte = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    LigneInfo(stringResource(R.string.detail_acheteur), ao.acheteur)
                    LigneInfo(stringResource(R.string.detail_region), ao.region.label())
                    LigneInfo(stringResource(R.string.detail_ville), ao.ville)
                    LigneInfo(stringResource(R.string.estimation), Format.dh(ao.estimationDh))
                    LigneInfo(stringResource(R.string.detail_caution), Format.dh(ao.cautionProvisoireDh))
                    LigneInfo(stringResource(R.string.detail_date_limite), Format.date(ao.dateLimiteEpoch))
                    LigneInfo(stringResource(R.string.detail_echeance), stringResource(R.string.jours_restants, Format.joursRestants(ao.dateLimiteEpoch)))
                }
            }

            if (ao.descriptif.isNotBlank()) {
                Text(stringResource(R.string.detail_objet), style = MaterialTheme.typography.titleMedium)
                Text(ao.descriptif, style = MaterialTheme.typography.bodyLarge)
            }

            // Coordonnées du maître d'ouvrage (comme sur l'avis).
            if (ao.email.isNotBlank() || ao.telephone.isNotBlank() || ao.telecopieur.isNotBlank()) {
                Text(stringResource(R.string.detail_contact), style = MaterialTheme.typography.titleMedium)
                if (ao.email.isNotBlank()) ContactRow(Icons.Filled.Email, stringResource(R.string.contact_email), ao.email)
                if (ao.telephone.isNotBlank()) ContactRow(Icons.Filled.Call, stringResource(R.string.contact_tel), ao.telephone)
                if (ao.telecopieur.isNotBlank()) ContactRow(Icons.Filled.Print, stringResource(R.string.contact_fax), ao.telecopieur)
            }

            // Raccourcis vers les outils.
            FilledTonalButton(
                onClick = { onOuvrirPrix(ao.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Calculate, contentDescription = null)
                Text("  " + stringResource(R.string.btn_analyser_prix))
            }
            OutlinedButton(
                onClick = { onOuvrirChecklist(ao.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Checklist, contentDescription = null)
                Text("  " + stringResource(R.string.btn_dossier_admin, state.piecesPretes, state.piecesTotal))
            }
            FilledTonalButton(
                onClick = { DossierGenerator.telecharger(context, ao, state.etats) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Download, contentDescription = null)
                Text("  " + stringResource(R.string.btn_telecharger_dossier, DossierGenerator.formatLabel(ao)))
            }
            FilledTonalButton(
                onClick = { onOuvrirDocuments(ao.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Description, contentDescription = null)
                Text("  " + stringResource(R.string.btn_generer_docs))
            }
            OutlinedButton(
                onClick = { WhatsApp.partager(context, WhatsApp.texteMarche(ao)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Chat, contentDescription = null)
                Text("  " + stringResource(R.string.btn_partager_whatsapp))
            }

            // Analyse concurrence.
            Text(stringResource(R.string.detail_concurrence), style = MaterialTheme.typography.titleMedium)
            if (state.concurrents.isEmpty()) {
                Text(
                    stringResource(R.string.detail_aucun_concurrent),
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
                            stringResource(R.string.detail_rabais_moyen, Format.pct(moy)),
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
                                    stringResource(R.string.concurrent_stats, soc.marchesGagnes, Format.pct(soc.tauxRabaisMoyen)),
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

@Composable
private fun ContactRow(icone: ImageVector, label: String, valeur: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier.padding(14.dp).fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column {
                Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(valeur, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
