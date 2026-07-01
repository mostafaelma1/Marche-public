package com.bmarche.pro.ui.screens.profil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmarche.pro.data.model.Domaine
import com.bmarche.pro.ui.repositoryViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfilScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val vm = repositoryViewModel { ProfilViewModel(it) }
    val state by vm.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = 16.dp, end = 16.dp,
                top = 12.dp + contentPadding.calculateTopPadding(),
                bottom = 16.dp + contentPadding.calculateBottomPadding()
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Mes alertes", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Configurez vos critères pour recevoir en priorité les marchés qui vous concernent.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(Modifier.padding(16.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.NotificationsActive,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "  ${state.nbRecommandes} marché(s) correspondent actuellement à votre profil.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Text("Secteurs d'activité", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Domaine.entries.forEach { d ->
                FilterChip(
                    selected = d in state.domaines,
                    onClick = { vm.basculerDomaine(d) },
                    label = { Text(d.labelFr) }
                )
            }
        }

        Text("Villes", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            state.villesDisponibles.forEach { v ->
                FilterChip(
                    selected = v in state.villes,
                    onClick = { vm.basculerVille(v) },
                    label = { Text(v) }
                )
            }
        }

        Text("Budget (DH)", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = state.budgetMin,
                onValueChange = vm::onBudgetMin,
                label = { Text("Minimum") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = state.budgetMax,
                onValueChange = vm::onBudgetMax,
                label = { Text("Maximum") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        OutlinedTextField(
            value = state.motsCles,
            onValueChange = vm::onMotsCles,
            label = { Text("Mots-clés (séparés par des virgules)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = vm::enregistrer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.enregistre) "Enregistré ✓" else "Enregistrer mes alertes")
        }

        Text(
            "À venir : réception des alertes par notification et WhatsApp dès qu'un nouveau marché correspond à ces critères.",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
