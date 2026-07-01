package com.bmarche.pro.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.data.model.DossierType
import com.bmarche.pro.data.model.EtatPiece
import com.bmarche.pro.data.model.Societe
import com.bmarche.pro.data.repository.BMarcheRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val ao: AppelOffre? = null,
    val concurrents: List<Societe> = emptyList(),
    val estFavori: Boolean = false,
    val piecesPretes: Int = 0,
    val piecesTotal: Int = DossierType.piecesStandard.size,
    /** Rabais moyen des concurrents probables, en % (null si inconnu). */
    val rabaisConcurrentsMoyen: Double? = null
)

class DetailViewModel(
    private val repo: BMarcheRepository,
    private val aoId: String
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState())
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    init {
        val ao = repo.appelOffre(aoId)
        val concurrents = ao?.let { repo.concurrents(it) } ?: emptyList()
        _state.update {
            it.copy(
                ao = ao,
                concurrents = concurrents,
                rabaisConcurrentsMoyen = concurrents.map { c -> c.tauxRabaisMoyen }
                    .takeIf { l -> l.isNotEmpty() }?.average()
            )
        }
        viewModelScope.launch {
            repo.estFavori(aoId).collect { fav -> _state.update { it.copy(estFavori = fav) } }
        }
        viewModelScope.launch {
            repo.checklist(aoId).collect { map ->
                val pretes = map.values.count { it == EtatPiece.PRET }
                _state.update { it.copy(piecesPretes = pretes) }
            }
        }
    }

    fun basculerFavori() {
        viewModelScope.launch { repo.basculerFavori(aoId, _state.value.estFavori) }
    }
}
