package com.bmarche.pro.ui.screens.masociete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmarche.pro.data.repository.BMarcheRepository
import com.bmarche.pro.data.repository.MaSociete
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MaSocieteViewModel(private val repo: BMarcheRepository) : ViewModel() {

    private val _societe = MutableStateFlow(MaSociete())
    val societe: StateFlow<MaSociete> = _societe.asStateFlow()

    private val _enregistre = MutableStateFlow(false)
    val enregistre: StateFlow<Boolean> = _enregistre.asStateFlow()

    init {
        viewModelScope.launch {
            repo.societeStore.societe.collect { s -> _societe.value = s }
        }
    }

    fun maj(transform: (MaSociete) -> MaSociete) {
        _societe.update(transform)
        _enregistre.value = false
    }

    fun enregistrer() {
        viewModelScope.launch {
            repo.societeStore.enregistrer(_societe.value)
            _enregistre.value = true
        }
    }
}
