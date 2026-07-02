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
    val labelAr: String,
    val icon: ImageVector
) {
    ACCUEIL("accueil", "Accueil", "الرئيسية", Icons.Filled.Home),
    FAVORIS("favoris", "Favoris", "المفضلة", Icons.Filled.Favorite),
    SOCIETES("societes", "Concurrence", "المنافسة", Icons.Filled.Business),
    PRIX("prix", "Prix", "الثمن", Icons.Filled.Calculate),
    PROFIL("profil", "Profil", "ملفي", Icons.Filled.Person);

    fun label(): String = if (java.util.Locale.getDefault().language == "ar") labelAr else labelFr
}

/** Destinations secondaires (empilées au-dessus des onglets). */
object Routes {
    const val LISTE = "liste?region={region}&type={type}"
    const val DETAIL = "detail/{aoId}"
    const val CHECKLIST = "checklist/{aoId}"
    const val PRIX_POUR = "prix?aoId={aoId}"
    const val DOCUMENTS = "documents/{aoId}"
    const val MA_SOCIETE = "ma_societe"

    fun liste(region: String? = null, type: String? = null): String {
        val params = buildList {
            if (region != null) add("region=$region")
            if (type != null) add("type=$type")
        }
        return if (params.isEmpty()) "liste" else "liste?" + params.joinToString("&")
    }
    fun detail(aoId: String) = "detail/$aoId"
    fun checklist(aoId: String) = "checklist/$aoId"
    fun prixPour(aoId: String) = "prix?aoId=$aoId"
    fun documents(aoId: String) = "documents/$aoId"
}
