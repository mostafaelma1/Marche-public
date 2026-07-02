package com.bmarche.pro.ui.screens.societes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bmarche.pro.ui.label
import com.bmarche.pro.BMarcheApplication
import com.bmarche.pro.ui.Format
import com.bmarche.pro.ui.components.LigneInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocieteDetailScreen(
    societeId: String,
    onRetour: () -> Unit
) {
    val app = LocalContext.current.applicationContext as BMarcheApplication
    val soc = app.repository.societe(societeId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(soc?.nom ?: "Société", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onRetour) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        if (soc == null) {
            Text("Société introuvable.", modifier = Modifier.padding(padding).padding(24.dp))
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
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    LigneInfo("Marchés remportés", soc.marchesGagnes.toString())
                    LigneInfo("Rabais moyen", Format.pct(soc.tauxRabaisMoyen))
                    LigneInfo("Villes", soc.villes.joinToString())
                }
            }

            Text("Secteurs d'activité", style = MaterialTheme.typography.titleMedium)
            Text(
                soc.domaines.joinToString { it.label() },
                style = MaterialTheme.typography.bodyLarge
            )

            Text("Administrations fréquentes", style = MaterialTheme.typography.titleMedium)
            soc.acheteursFrequents.forEach { acheteur ->
                Text("• $acheteur", style = MaterialTheme.typography.bodyLarge)
            }

            Text(
                "Ces données sont issues de l'historique des résultats publiés et servent à estimer le niveau de concurrence attendu.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
