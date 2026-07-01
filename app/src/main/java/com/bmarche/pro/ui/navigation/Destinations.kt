package com.bmarche.pro.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

/** Onglets principaux affichés dans la barre de navigation. */
enum class TopDestination(
    val route: String,
    val labelFr: String,
    val icon: ImageVector
) {
    ACCUEIL("accueil", "Accueil", Icons.Filled.Home),
    FAVORIS("favoris", "Favoris", Icons.Filled.Favorite),
    SOCIETES("societes", "Concurrence", Icons.Filled.Business),
    PRIX("prix", "Prix", Icons.Filled.Calculate),
    PROFIL("profil", "Profil", Icons.Filled.Person)
}

/** Destinations secondaires (empilées au-dessus des onglets). */
object Routes {
    const val LISTE = "liste?region={region}"
    const val DETAIL = "detail/{aoId}"
    const val CHECKLIST = "checklist/{aoId}"
    const val PRIX_POUR = "prix?aoId={aoId}"
    const val DOCUMENTS = "documents/{aoId}"
    const val MA_SOCIETE = "ma_societe"

    fun liste(region: String? = null) = if (region == null) "liste" else "liste?region=$region"
    fun detail(aoId: String) = "detail/$aoId"
    fun checklist(aoId: String) = "checklist/$aoId"
    fun prixPour(aoId: String) = "prix?aoId=$aoId"
    fun documents(aoId: String) = "documents/$aoId"
}
