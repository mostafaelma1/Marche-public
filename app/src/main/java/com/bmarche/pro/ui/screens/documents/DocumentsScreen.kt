package com.bmarche.pro.ui.screens.documents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmarche.pro.documents.DocumentsGenerator
import com.bmarche.pro.documents.TypeDocument
import com.bmarche.pro.ui.components.SectionTitle
import com.bmarche.pro.ui.repositoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsScreen(
    aoId: String,
    onRetour: () -> Unit,
    onOuvrirMaSociete: () -> Unit
) {
    val vm = repositoryViewModel { DocumentsViewModel(it, aoId) }
    val state by vm.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val ao = state.ao

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Générer les documents") },
                navigationIcon = {
                    IconButton(onClick = onRetour) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        if (ao == null) {
            Text("Marché introuvable.", modifier = Modifier.padding(padding).padding(24.dp))
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(ao.objet, style = MaterialTheme.typography.titleMedium)
            Text(
                "Réf. ${ao.reference} — ${ao.acheteur}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!state.societeConfiguree) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                            Text(
                                "  Renseignez d'abord votre société",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Text(
                            "Les documents seront générés avec des champs vides tant que les informations de votre société ne sont pas saisies.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Button(onClick = onOuvrirMaSociete) { Text("Renseigner ma société") }
                    }
                }
            }

            SectionTitle("Paramètres de l'offre")
            OutlinedTextField(
                value = state.montant,
                onValueChange = vm::onMontant,
                label = { Text("Montant de votre offre (DH TTC)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = state.montantEnLettres,
                onValueChange = vm::onMontantLettres,
                label = { Text("Montant en lettres (optionnel)") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = state.delaiValidite,
                    onValueChange = vm::onDelai,
                    label = { Text("Validité (jours)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = state.lieu,
                    onValueChange = vm::onLieu,
                    label = { Text("Fait à") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            SectionTitle("Documents à générer")
            TypeDocument.entries.forEach { type ->
                OutlinedButton(
                    onClick = { DocumentsGenerator.telecharger(context, type, ao, state.societe, state.params()) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Description, contentDescription = null)
                    Text("  ${type.titre}")
                }
            }

            FilledTonalButton(
                onClick = { DocumentsGenerator.telechargerTout(context, ao, state.societe, state.params()) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Download, contentDescription = null)
                Text("  Tout télécharger (ZIP)")
            }

            Text(
                "Documents indicatifs générés à partir de vos informations — vérifiez-les et signez-les avant le dépôt.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
