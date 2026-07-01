package com.bmarche.pro.ui.screens.masociete

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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmarche.pro.data.repository.MaSociete
import com.bmarche.pro.ui.components.SectionTitle
import com.bmarche.pro.ui.repositoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaSocieteScreen(onRetour: () -> Unit) {
    val vm = repositoryViewModel { MaSocieteViewModel(it) }
    val s by vm.societe.collectAsStateWithLifecycle()
    val enregistre by vm.enregistre.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ma société") },
                navigationIcon = {
                    IconButton(onClick = onRetour) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Ces informations pré-remplissent automatiquement vos documents (acte d'engagement, déclaration sur l'honneur, lettres).",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
            )

            SectionTitle("Identité")
            Champ("Raison sociale", s.raisonSociale) { v -> vm.maj { it.copy(raisonSociale = v) } }
            Champ("Forme juridique (SARL, SA…)", s.formeJuridique) { v -> vm.maj { it.copy(formeJuridique = v) } }
            Champ("Capital social (DH)", s.capital, KeyboardType.Number) { v -> vm.maj { it.copy(capital = v) } }
            Champ("Adresse du siège", s.adresse) { v -> vm.maj { it.copy(adresse = v) } }
            Champ("Ville", s.ville) { v -> vm.maj { it.copy(ville = v) } }

            SectionTitle("Identifiants légaux")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Champ("N° RC", s.rcNumero, modifier = Modifier.weight(1f)) { v -> vm.maj { it.copy(rcNumero = v) } }
                Champ("Tribunal (ville RC)", s.rcVille, modifier = Modifier.weight(1f)) { v -> vm.maj { it.copy(rcVille = v) } }
            }
            Champ("ICE", s.ice, KeyboardType.Number) { v -> vm.maj { it.copy(ice = v) } }
            Champ("Identifiant fiscal (IF)", s.identifiantFiscal, KeyboardType.Number) { v -> vm.maj { it.copy(identifiantFiscal = v) } }
            Champ("Taxe professionnelle (patente)", s.patente) { v -> vm.maj { it.copy(patente = v) } }
            Champ("N° CNSS", s.cnss, KeyboardType.Number) { v -> vm.maj { it.copy(cnss = v) } }

            SectionTitle("Représentant légal")
            Champ("Nom et prénom", s.representantNom) { v -> vm.maj { it.copy(representantNom = v) } }
            Champ("Qualité (Gérant, PDG…)", s.representantQualite) { v -> vm.maj { it.copy(representantQualite = v) } }
            Champ("Téléphone", s.telephone, KeyboardType.Phone) { v -> vm.maj { it.copy(telephone = v) } }
            Champ("E-mail", s.email, KeyboardType.Email) { v -> vm.maj { it.copy(email = v) } }

            SectionTitle("Coordonnées bancaires")
            Champ("Banque", s.banque) { v -> vm.maj { it.copy(banque = v) } }
            Champ("RIB", s.rib, KeyboardType.Number) { v -> vm.maj { it.copy(rib = v) } }

            Button(onClick = vm::enregistrer, modifier = Modifier.fillMaxWidth()) {
                Text(if (enregistre) "Enregistré ✓" else "Enregistrer")
            }
        }
    }
}

@Composable
private fun Champ(
    label: String,
    valeur: String,
    clavier: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valeur,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = clavier)
    )
}
