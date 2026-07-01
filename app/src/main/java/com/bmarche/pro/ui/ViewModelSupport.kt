package com.bmarche.pro.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.bmarche.pro.BMarcheApplication
import com.bmarche.pro.data.repository.BMarcheRepository

/**
 * Fabrique générique de ViewModel qui fournit le [BMarcheRepository] partagé issu de
 * l'Application. Évite d'ajouter une dépendance d'injection dans cette version MVP.
 */
fun <VM : ViewModel> repositoryViewModelFactory(
    create: (BMarcheRepository) -> VM
): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val app = extras[APPLICATION_KEY] as BMarcheApplication
        return create(app.repository) as T
    }
}

@Composable
inline fun <reified VM : ViewModel> repositoryViewModel(
    noinline create: (BMarcheRepository) -> VM
): VM = viewModel(factory = repositoryViewModelFactory(create))
