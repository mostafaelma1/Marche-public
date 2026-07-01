package com.bmarche.pro.ui.screens.checklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.data.model.ChecklistItem
import com.bmarche.pro.data.model.DossierType
import com.bmarche.pro.data.model.EtatPiece
import com.bmarche.pro.data.repository.BMarcheRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChecklistUiState(
    val ao: AppelOffre? = null,
    val pieces: List<ChecklistItem> = DossierType.piecesStandard,
    val etats: Map<String, EtatPiece> = emptyMap()
) {
    val nbPretes: Int get() = pieces.count { etats[it.cle] == EtatPiece.PRET }
    val progression: Float get() = if (pieces.isEmpty()) 0f else nbPretes.toFloat() / pieces.size
}

class ChecklistViewModel(
    private val repo: BMarcheRepository,
    private val aoId: String
) : ViewModel() {

    private val _state = MutableStateFlow(ChecklistUiState(ao = repo.appelOffre(aoId)))
    val state: StateFlow<ChecklistUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.checklist(aoId).collect { map -> _state.update { it.copy(etats = map) } }
        }
    }

    /** Fait avancer une pièce à l'état suivant (cycle À préparer → En cours → Prêt). */
    fun avancer(cle: String) {
        val actuel = _state.value.etats[cle] ?: EtatPiece.A_PREPARER
        val suivant = when (actuel) {
            EtatPiece.A_PREPARER -> EtatPiece.EN_COURS
            EtatPiece.EN_COURS -> EtatPiece.PRET
            EtatPiece.PRET -> EtatPiece.A_PREPARER
        }
        viewModelScope.launch { repo.majPiece(aoId, cle, suivant) }
    }

    fun definir(cle: String, etat: EtatPiece) {
        viewModelScope.launch { repo.majPiece(aoId, cle, etat) }
    }
}
