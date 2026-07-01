package com.bmarche.pro.ui.screens.prix

import androidx.lifecycle.ViewModel
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.data.model.Domaine
import com.bmarche.pro.data.repository.BMarcheRepository
import com.bmarche.pro.domain.AnalysePrix
import com.bmarche.pro.domain.PrixReferenceCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PrixUiState(
    val estimation: String = "",
    val prixPropose: String = "",
    val domaine: Domaine = Domaine.RESTAURATION,
    val aoAssocie: AppelOffre? = null,
    val analyse: AnalysePrix? = null,
    val erreur: String? = null
)

class PrixViewModel(repo: BMarcheRepository, aoId: String?) : ViewModel() {

    private val _state = MutableStateFlow(PrixUiState())
    val state: StateFlow<PrixUiState> = _state.asStateFlow()

    init {
        val ao = aoId?.let { repo.appelOffre(it) }
        if (ao != null) {
            _state.update {
                it.copy(
                    aoAssocie = ao,
                    domaine = ao.domaine,
                    estimation = ao.estimationDh.toLong().toString()
                )
            }
        }
    }

    fun onEstimation(v: String) { _state.update { it.copy(estimation = v.filtreMontant()) }; recalculer() }
    fun onPrix(v: String) { _state.update { it.copy(prixPropose = v.filtreMontant()) }; recalculer() }
    fun onDomaine(d: Domaine) { _state.update { it.copy(domaine = d) }; recalculer() }

    private fun recalculer() {
        val s = _state.value
        val estimation = s.estimation.toDoubleOrNull()
        val prix = s.prixPropose.toDoubleOrNull()
        if (estimation == null || estimation <= 0.0 || prix == null || prix <= 0.0) {
            _state.update { it.copy(analyse = null, erreur = null) }
            return
        }
        val analyse = PrixReferenceCalculator.analyser(estimation, prix, s.domaine)
        _state.update { it.copy(analyse = analyse, erreur = null) }
    }

    private fun String.filtreMontant(): String = filter { it.isDigit() }
}
