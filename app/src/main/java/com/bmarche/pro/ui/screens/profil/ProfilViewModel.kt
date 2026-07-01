package com.bmarche.pro.ui.screens.profil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmarche.pro.data.model.Domaine
import com.bmarche.pro.data.repository.BMarcheRepository
import com.bmarche.pro.data.repository.ProfilAlerte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfilUiState(
    val domaines: Set<Domaine> = emptySet(),
    val villes: Set<String> = emptySet(),
    val villesDisponibles: List<String> = emptyList(),
    val budgetMin: String = "",
    val budgetMax: String = "",
    val motsCles: String = "",
    val enregistre: Boolean = false,
    val nbRecommandes: Int = 0
)

class ProfilViewModel(private val repo: BMarcheRepository) : ViewModel() {

    private val _state = MutableStateFlow(ProfilUiState(villesDisponibles = repo.villesDisponibles()))
    val state: StateFlow<ProfilUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.profilStore.profil.collect { p ->
                _state.update {
                    it.copy(
                        domaines = p.domaines,
                        villes = p.villes,
                        budgetMin = if (p.budgetMin > 0) p.budgetMin.toLong().toString() else "",
                        budgetMax = if (p.budgetMax > 0) p.budgetMax.toLong().toString() else "",
                        motsCles = p.motsCles,
                        nbRecommandes = repo.marchesRecommandes(p).size
                    )
                }
            }
        }
    }

    fun basculerDomaine(d: Domaine) {
        _state.update {
            val nouveaux = if (d in it.domaines) it.domaines - d else it.domaines + d
            it.copy(domaines = nouveaux, enregistre = false)
        }
    }

    fun basculerVille(v: String) {
        _state.update {
            val nouvelles = if (v in it.villes) it.villes - v else it.villes + v
            it.copy(villes = nouvelles, enregistre = false)
        }
    }

    fun onBudgetMin(v: String) { _state.update { it.copy(budgetMin = v.filter(Char::isDigit), enregistre = false) } }
    fun onBudgetMax(v: String) { _state.update { it.copy(budgetMax = v.filter(Char::isDigit), enregistre = false) } }
    fun onMotsCles(v: String) { _state.update { it.copy(motsCles = v, enregistre = false) } }

    fun enregistrer() {
        val s = _state.value
        val profil = ProfilAlerte(
            domaines = s.domaines,
            villes = s.villes,
            budgetMin = s.budgetMin.toDoubleOrNull() ?: 0.0,
            budgetMax = s.budgetMax.toDoubleOrNull() ?: 0.0,
            motsCles = s.motsCles
        )
        viewModelScope.launch {
            repo.profilStore.enregistrer(profil)
            _state.update { it.copy(enregistre = true, nbRecommandes = repo.marchesRecommandes(profil).size) }
        }
    }
}
