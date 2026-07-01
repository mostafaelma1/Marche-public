package com.bmarche.pro.ui.screens.checklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmarche.pro.data.model.ChecklistItem
import com.bmarche.pro.data.model.EtatPiece
import com.bmarche.pro.ui.repositoryViewModel
import com.bmarche.pro.ui.theme.PrixCompetitif
import com.bmarche.pro.ui.theme.PrixHaut

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen(
    aoId: String,
    onRetour: () -> Unit
) {
    val vm = repositoryViewModel { ChecklistViewModel(it, aoId) }
    val state by vm.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dossier administratif") },
                navigationIcon = {
                    IconButton(onClick = onRetour) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                state.ao?.let {
                    Text(it.objet, style = MaterialTheme.typography.titleMedium)
                }
                Text(
                    "${state.nbPretes} / ${state.pieces.size} pièces prêtes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                LinearProgressIndicator(
                    progress = { state.progression },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }

            items(state.pieces, key = { it.cle }) { piece ->
                val etat = state.etats[piece.cle] ?: EtatPiece.A_PREPARER
                PieceRow(
                    piece = piece,
                    etat = etat,
                    onClick = { vm.avancer(piece.cle) }
                )
            }

            item {
                Text(
                    "Astuce : touchez une pièce pour la faire passer de « À préparer » à « En cours » puis « Prêt ».",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun PieceRow(piece: ChecklistItem, etat: EtatPiece, onClick: () -> Unit) {
    val (icone, couleur) = when (etat) {
        EtatPiece.A_PREPARER -> Icons.Filled.RadioButtonUnchecked to MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        EtatPiece.EN_COURS -> Icons.Filled.Timelapse to PrixHaut
        EtatPiece.PRET -> Icons.Filled.CheckCircle to PrixCompetitif
    }
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icone, contentDescription = null, tint = couleur)
            Column(Modifier.weight(1f)) {
                Text(piece.libelleFr, style = MaterialTheme.typography.bodyLarge)
                Text(
                    etat.labelFr,
                    style = MaterialTheme.typography.labelMedium,
                    color = couleur,
                    fontWeight = FontWeight.Medium
                )
            }
            if (!piece.obligatoire) {
                Text(
                    "optionnel",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}
