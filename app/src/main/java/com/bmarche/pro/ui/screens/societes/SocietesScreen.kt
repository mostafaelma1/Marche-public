package com.bmarche.pro.ui.screens.societes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.bmarche.pro.ui.label
import com.bmarche.pro.BMarcheApplication
import com.bmarche.pro.data.model.Societe
import com.bmarche.pro.ui.Format
import com.bmarche.pro.ui.components.Badge
import androidx.compose.ui.platform.LocalContext

@Composable
fun SocietesScreen(
    onOuvrirSociete: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val app = LocalContext.current.applicationContext as BMarcheApplication
    val societes = app.repository.societes()

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp,
            top = 12.dp + contentPadding.calculateTopPadding(),
            bottom = 12.dp + contentPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            com.bmarche.pro.ui.components.HeroHeader(
                titre = stringResource(com.bmarche.pro.R.string.concurrence_titre),
                sousTitre = stringResource(com.bmarche.pro.R.string.concurrence_sous_titre)
            )
        }
        items(societes, key = { it.id }) { soc ->
            SocieteCard(soc, onClick = { onOuvrirSociete(soc.id) })
        }
    }
}

@Composable
private fun SocieteCard(soc: Societe, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Business, null, tint = MaterialTheme.colorScheme.primary)
                Text("  ${soc.nom}", style = MaterialTheme.typography.titleMedium)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Badge(
                    texte = "${soc.marchesGagnes} marchés",
                    couleurFond = MaterialTheme.colorScheme.primaryContainer,
                    couleurTexte = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Badge(
                    texte = "Rabais moyen ${Format.pct(soc.tauxRabaisMoyen)}",
                    couleurFond = MaterialTheme.colorScheme.secondaryContainer,
                    couleurTexte = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Text(
                soc.domaines.joinToString { it.label() },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
        }
    }
}
