package com.bmarche.pro.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bmarche.pro.data.model.Domaine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Préférences de l'utilisateur : secteurs suivis, villes, budget, pour les alertes ciblées. */
data class ProfilAlerte(
    val domaines: Set<Domaine> = emptySet(),
    val villes: Set<String> = emptySet(),
    val budgetMin: Double = 0.0,
    val budgetMax: Double = 0.0,
    val motsCles: String = ""
) {
    /** Un profil est "actif" dès qu'un critère d'alerte est renseigné. */
    val estConfigure: Boolean
        get() = domaines.isNotEmpty() || villes.isNotEmpty() || budgetMin > 0 || budgetMax > 0 || motsCles.isNotBlank()
}

private val Context.dataStore by preferencesDataStore(name = "profil")

class ProfilStore(private val context: Context) {

    private object Keys {
        val DOMAINES = stringSetPreferencesKey("domaines")
        val VILLES = stringSetPreferencesKey("villes")
        val BUDGET_MIN = doublePreferencesKey("budget_min")
        val BUDGET_MAX = doublePreferencesKey("budget_max")
        val MOTS_CLES = stringPreferencesKey("mots_cles")
    }

    val profil: Flow<ProfilAlerte> = context.dataStore.data.map { prefs ->
        ProfilAlerte(
            domaines = prefs[Keys.DOMAINES].orEmpty().mapNotNull { Domaine.fromName(it) }.toSet(),
            villes = prefs[Keys.VILLES].orEmpty(),
            budgetMin = prefs[Keys.BUDGET_MIN] ?: 0.0,
            budgetMax = prefs[Keys.BUDGET_MAX] ?: 0.0,
            motsCles = prefs[Keys.MOTS_CLES].orEmpty()
        )
    }

    suspend fun enregistrer(profil: ProfilAlerte) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DOMAINES] = profil.domaines.map { it.name }.toSet()
            prefs[Keys.VILLES] = profil.villes
            prefs[Keys.BUDGET_MIN] = profil.budgetMin
            prefs[Keys.BUDGET_MAX] = profil.budgetMax
            prefs[Keys.MOTS_CLES] = profil.motsCles
        }
    }
}
