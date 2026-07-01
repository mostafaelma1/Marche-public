package com.bmarche.pro.ui.screens.favoris

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.data.repository.BMarcheRepository
import com.bmarche.pro.ui.components.AppelOffreCard
import com.bmarche.pro.ui.components.EtatVide
import com.bmarche.pro.ui.repositoryViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavorisViewModel(private val repo: BMarcheRepository) : ViewModel() {
    val favoris: StateFlow<List<AppelOffre>> =
        repo.favoris().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun retirer(id: String) {
        viewModelScope.launch { repo.basculerFavori(id, actuellementFavori = true) }
    }
}

@Composable
fun FavorisScreen(
    onOuvrirDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val vm = repositoryViewModel { FavorisViewModel(it) }
    val favoris by vm.favoris.collectAsStateWithLifecycle()

    if (favoris.isEmpty()) {
        EtatVide(
            icone = Icons.Filled.FavoriteBorder,
            titre = "Aucun favori",
            sousTitre = "Ajoutez les marchés qui vous intéressent avec le cœur pour les suivre ici.",
            modifier = modifier,
            contentPadding = contentPadding
        )
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp,
            top = 12.dp + contentPadding.calculateTopPadding(),
            bottom = 12.dp + contentPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            com.bmarche.pro.ui.components.HeroHeader(
                titre = "Mes favoris",
                sousTitre = "Les marchés que vous suivez de près."
            )
        }
        items(favoris, key = { it.id }) { ao ->
            AppelOffreCard(
                ao = ao,
                estFavori = true,
                onClick = { onOuvrirDetail(ao.id) },
                onToggleFavori = { vm.retirer(ao.id) }
            )
        }
    }
}
