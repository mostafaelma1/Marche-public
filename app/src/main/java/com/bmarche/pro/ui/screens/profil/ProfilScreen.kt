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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmarche.pro.data.model.Domaine
import com.bmarche.pro.notif.NotificationHelper
import com.bmarche.pro.share.WhatsApp
import com.bmarche.pro.ui.repositoryViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfilScreen(
    onOuvrirMaSociete: () -> Unit = {},
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val vm = repositoryViewModel { ProfilViewModel(it) }
    val context = LocalContext.current
    val demanderPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { accorde ->
        if (accorde) {
            NotificationHelper.notifierMarches(context, vm.marchesCorrespondants())
        } else {
            Toast.makeText(context, "Autorisez les notifications pour recevoir les alertes.", Toast.LENGTH_LONG).show()
        }
    }
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
        com.bmarche.pro.ui.components.HeroHeader(
            titre = "Mes alertes",
            sousTitre = "Configurez vos critères pour être alerté des marchés qui vous concernent."
        )

        Card(
            onClick = onOuvrirMaSociete,
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(Icons.Filled.Business, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Column(Modifier.weight(1f)) {
                    Text("Ma société & documents", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Infos entreprise → acte d'engagement, déclaration sur l'honneur, lettres.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

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

        OutlinedTextField(
            value = state.whatsapp,
            onValueChange = vm::onWhatsapp,
            label = { Text("Numéro WhatsApp (ex : 2126…)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Button(
            onClick = vm::enregistrer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.enregistre) "Enregistré ✓" else "Enregistrer mes alertes")
        }

        // --- Alertes gratuites : notification locale + WhatsApp ---
        Text("Alertes (gratuit)", style = MaterialTheme.typography.titleMedium)

        OutlinedButton(
            onClick = {
                val marches = vm.marchesCorrespondants()
                if (marches.isEmpty()) {
                    Toast.makeText(context, "Aucun marché ne correspond pour l'instant.", Toast.LENGTH_SHORT).show()
                } else if (NotificationHelper.peutNotifier(context)) {
                    NotificationHelper.notifierMarches(context, marches)
                } else {
                    demanderPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.NotificationsActive, contentDescription = null)
            Text("  Tester la notification d'alerte")
        }

        OutlinedButton(
            onClick = {
                val marches = vm.marchesCorrespondants()
                if (marches.isEmpty()) {
                    Toast.makeText(context, "Aucun marché ne correspond pour l'instant.", Toast.LENGTH_SHORT).show()
                    return@OutlinedButton
                }
                val recap = WhatsApp.texteRecap(marches)
                if (state.whatsapp.isNotBlank()) {
                    WhatsApp.envoyerVers(context, state.whatsapp, recap)
                } else {
                    WhatsApp.partager(context, recap)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Chat, contentDescription = null)
            Text("  Recevoir le récap sur WhatsApp")
        }

        Text(
            "Les notifications locales et le récap WhatsApp sont 100 % gratuits. " +
                "Une vérification automatique tourne en arrière-plan pour vous alerter des nouveaux marchés correspondant à votre profil.",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
