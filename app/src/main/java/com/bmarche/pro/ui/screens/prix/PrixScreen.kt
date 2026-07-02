package com.bmarche.pro.ui.screens.prix

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import com.bmarche.pro.ui.label
import com.bmarche.pro.data.model.Domaine
import com.bmarche.pro.domain.AnalysePrix
import com.bmarche.pro.domain.PositionPrix
import com.bmarche.pro.domain.PrixReferenceCalculator
import com.bmarche.pro.ui.Format
import com.bmarche.pro.ui.components.LigneInfo
import com.bmarche.pro.ui.repositoryViewModel
import com.bmarche.pro.ui.theme.PrixAgressif
import com.bmarche.pro.ui.theme.PrixCompetitif
import com.bmarche.pro.ui.theme.PrixDanger
import com.bmarche.pro.ui.theme.PrixHaut

@Composable
fun PrixScreen(
    aoId: String?,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val vm = repositoryViewModel { PrixViewModel(it, aoId) }
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
            titre = stringResource(com.bmarche.pro.R.string.prix_titre),
            sousTitre = stringResource(com.bmarche.pro.R.string.prix_sous_titre)
        )
        state.aoAssocie?.let {
            Text(
                "Marché : ${it.objet}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Text("Secteur d'activité", style = MaterialTheme.typography.titleMedium)
        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Domaine.entries.forEach { d ->
                FilterChip(
                    selected = state.domaine == d,
                    onClick = { vm.onDomaine(d) },
                    label = { Text(d.label()) }
                )
            }
        }

        OutlinedTextField(
            value = state.estimation,
            onValueChange = vm::onEstimation,
            label = { Text("Estimation administrative (DH)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = state.prixPropose,
            onValueChange = vm::onPrix,
            label = { Text("Votre prix proposé (DH)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        val (minPct, maxPct) = PrixReferenceCalculator.fourchette(state.domaine)
        Text(
            "Rabais habituel dans ce secteur : ${Format.pct(minPct)} à ${Format.pct(maxPct)}.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        state.analyse?.let { analyse ->
            ResultatPrix(analyse)
        } ?: Text(
            "Saisissez l'estimation et votre prix pour lancer l'analyse.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun ResultatPrix(analyse: AnalysePrix) {
    val couleur = analyse.position.couleur()
    Card(
        colors = CardDefaults.cardColors(containerColor = couleur.copy(alpha = 0.10f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                analyse.position.libelle(),
                style = MaterialTheme.typography.titleLarge,
                color = couleur,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Votre rabais : ${Format.pct(analyse.rabaisPct)}",
                style = MaterialTheme.typography.bodyLarge
            )
            LigneInfo("Prix de référence", Format.dh(analyse.prixReference))
            LigneInfo("Intervalle sûr", "${Format.dh(analyse.borneBasse)} – ${Format.dh(analyse.borneHaute)}")

            Text(
                "Chance d'être moins-disant : ${Format.pct(analyse.chanceMieuxDisant * 100)}",
                style = MaterialTheme.typography.bodyMedium
            )
            LinearProgressIndicator(
                progress = { analyse.chanceMieuxDisant.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = couleur
            )
            Text(
                analyse.position.conseil(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

private fun PositionPrix.couleur(): Color = when (this) {
    PositionPrix.TROP_HAUT -> PrixHaut
    PositionPrix.HAUT -> PrixHaut
    PositionPrix.COMPETITIF -> PrixCompetitif
    PositionPrix.AGRESSIF -> PrixAgressif
    PositionPrix.DANGEREUX -> PrixDanger
}

private fun PositionPrix.libelle(): String = when (this) {
    PositionPrix.TROP_HAUT -> "Prix au-dessus de l'estimation"
    PositionPrix.HAUT -> "Prix élevé"
    PositionPrix.COMPETITIF -> "Prix compétitif ✅"
    PositionPrix.AGRESSIF -> "Prix agressif ⚠️"
    PositionPrix.DANGEREUX -> "Offre anormalement basse ⛔"
}

private fun PositionPrix.conseil(): String = when (this) {
    PositionPrix.TROP_HAUT -> "Votre prix dépasse l'estimation : très faible chance de l'emporter. Baissez pour entrer dans la fourchette du secteur."
    PositionPrix.HAUT -> "Vous êtes encore au-dessus des rabais habituels. Un rabais un peu plus marqué améliorerait vos chances."
    PositionPrix.COMPETITIF -> "Vous êtes dans l'intervalle habituel du secteur : bon équilibre entre chance de gagner et marge."
    PositionPrix.AGRESSIF -> "Rabais supérieur à la moyenne : chances élevées, mais surveillez votre marge et vos coûts."
    PositionPrix.DANGEREUX -> "Rabais très élevé : risque d'offre anormalement basse pouvant être écartée. Justifiez ou remontez votre prix."
}
