package com.bmarche.pro.ui.screens.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.data.repository.BMarcheRepository
import com.bmarche.pro.data.repository.MaSociete
import com.bmarche.pro.documents.ParamsDocument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DocumentsUiState(
    val ao: AppelOffre? = null,
    val societe: MaSociete = MaSociete(),
    val montant: String = "",
    val montantEnLettres: String = "",
    val delaiValidite: String = "90",
    val lieu: String = ""
) {
    val societeConfiguree: Boolean get() = societe.estRempli
    fun params() = ParamsDocument(
        montantOffre = montant,
        montantEnLettres = montantEnLettres,
        delaiValiditeJours = delaiValidite.toIntOrNull() ?: 90,
        lieu = lieu
    )
}

class DocumentsViewModel(repo: BMarcheRepository, aoId: String) : ViewModel() {

    private val _state = MutableStateFlow(DocumentsUiState(ao = repo.appelOffre(aoId)))
    val state: StateFlow<DocumentsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.societeStore.societe.collect { s ->
                _state.update { it.copy(societe = s, lieu = if (it.lieu.isBlank()) s.ville else it.lieu) }
            }
        }
    }

    fun onMontant(v: String) = _state.update { it.copy(montant = v.filter { c -> c.isDigit() }) }
    fun onMontantLettres(v: String) = _state.update { it.copy(montantEnLettres = v) }
    fun onDelai(v: String) = _state.update { it.copy(delaiValidite = v.filter { c -> c.isDigit() }) }
    fun onLieu(v: String) = _state.update { it.copy(lieu = v) }
}
