package com.bmarche.pro.ui.screens.liste

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.data.model.Domaine
import com.bmarche.pro.data.repository.BMarcheRepository
import com.bmarche.pro.data.repository.FiltreAppelOffre
import com.bmarche.pro.data.repository.ProfilAlerte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ListeUiState(
    val recherche: String = "",
    val domaine: Domaine? = null,
    val ville: String? = null,
    val resultats: List<AppelOffre> = emptyList(),
    val villes: List<String> = emptyList(),
    val recommandes: List<AppelOffre> = emptyList(),
    val favorisIds: Set<String> = emptySet()
)

class ListeViewModel(private val repo: BMarcheRepository) : ViewModel() {

    private val _state = MutableStateFlow(ListeUiState(villes = repo.villesDisponibles()))
    val state: StateFlow<ListeUiState> = _state.asStateFlow()

    init {
        recalculer()
        viewModelScope.launch {
            repo.favorisIds().collect { ids -> _state.update { it.copy(favorisIds = ids.toSet()) } }
        }
        viewModelScope.launch {
            repo.profilStore.profil.collect { profil -> majRecommandes(profil) }
        }
    }

    fun onRecherche(v: String) { _state.update { it.copy(recherche = v) }; recalculer() }

    fun onDomaine(d: Domaine?) { _state.update { it.copy(domaine = d) }; recalculer() }

    fun onVille(v: String?) { _state.update { it.copy(ville = v) }; recalculer() }

    fun basculerFavori(id: String) {
        viewModelScope.launch {
            repo.basculerFavori(id, actuellementFavori = id in _state.value.favorisIds)
        }
    }

    private fun majRecommandes(profil: ProfilAlerte) {
        _state.update { it.copy(recommandes = repo.marchesRecommandes(profil)) }
    }

    private fun recalculer() {
        val s = _state.value
        val filtre = FiltreAppelOffre(recherche = s.recherche, domaine = s.domaine, ville = s.ville)
        _state.update { it.copy(resultats = repo.appelsOffres(filtre)) }
    }
}
