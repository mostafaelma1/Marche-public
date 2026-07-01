package com.bmarche.pro.data.repository

import android.content.Context
import com.bmarche.pro.data.local.BMarcheDatabase
import com.bmarche.pro.data.local.ChecklistEntity
import com.bmarche.pro.data.local.FavoriEntity
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.data.model.Domaine
import com.bmarche.pro.data.model.EtatPiece
import com.bmarche.pro.data.model.Region
import com.bmarche.pro.data.model.Societe
import com.bmarche.pro.data.model.TypePublication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Critères de filtrage de la liste des appels d'offres. */
data class FiltreAppelOffre(
    val recherche: String = "",
    val domaine: Domaine? = null,
    val ville: String? = null,
    val region: Region? = null,
    val type: TypePublication? = null,
    val budgetMax: Double? = null
)

/**
 * Point d'accès unique aux données. Aujourd'hui la source des marchés/sociétés est
 * [SampleData] ; l'état utilisateur (favoris, checklist) est persisté via Room, et le
 * profil d'alertes via DataStore.
 */
class BMarcheRepository(context: Context) {

    private val dao = BMarcheDatabase.get(context).dao()
    val profilStore = ProfilStore(context)
    val societeStore = SocieteStore(context)

    // --- Appels d'offres --------------------------------------------------

    fun appelsOffres(filtre: FiltreAppelOffre = FiltreAppelOffre()): List<AppelOffre> {
        val recherche = filtre.recherche.trim().lowercase()
        return SampleData.appelsOffres.filter { ao ->
            (filtre.domaine == null || ao.domaine == filtre.domaine) &&
                (filtre.ville == null || ao.ville.equals(filtre.ville, ignoreCase = true)) &&
                (filtre.region == null || ao.region == filtre.region) &&
                (filtre.type == null || ao.type == filtre.type) &&
                (filtre.budgetMax == null || ao.estimationDh <= filtre.budgetMax) &&
                (recherche.isBlank() ||
                    ao.objet.lowercase().contains(recherche) ||
                    ao.acheteur.lowercase().contains(recherche) ||
                    ao.reference.lowercase().contains(recherche))
        }.sortedBy { it.dateLimiteEpoch }
    }

    fun appelOffre(id: String): AppelOffre? = SampleData.appelOffreById(id)

    fun villesDisponibles(): List<String> =
        SampleData.appelsOffres.map { it.ville }.distinct().sorted()

    /** Nombre total de marchés publics disponibles (hors autres catégories). */
    fun totalMarches(): Int =
        SampleData.appelsOffres.count { it.type == TypePublication.MARCHE_PUBLIC }

    /** Nombre de marchés publics par région (toutes les régions, y compris à zéro). */
    fun comptesParRegion(): Map<Region, Int> {
        val comptes = SampleData.appelsOffres
            .filter { it.type == TypePublication.MARCHE_PUBLIC }
            .groupingBy { it.region }.eachCount()
        return Region.entries.associateWith { comptes[it] ?: 0 }
    }

    /** Nombre de publications par catégorie (toutes les catégories, y compris à zéro). */
    fun comptesParType(): Map<TypePublication, Int> {
        val comptes = SampleData.appelsOffres.groupingBy { it.type }.eachCount()
        return TypePublication.entries.associateWith { comptes[it] ?: 0 }
    }

    /** Marchés correspondant au profil d'alerte (pour l'écran d'accueil / notifications). */
    fun marchesRecommandes(profil: ProfilAlerte): List<AppelOffre> {
        if (!profil.estConfigure) return emptyList()
        val motsCles = profil.motsCles.split(",", " ")
            .map { it.trim().lowercase() }.filter { it.isNotBlank() }
        return SampleData.appelsOffres.filter { ao ->
            (profil.domaines.isEmpty() || ao.domaine in profil.domaines) &&
                (profil.villes.isEmpty() || ao.ville in profil.villes) &&
                (profil.budgetMin <= 0 || ao.estimationDh >= profil.budgetMin) &&
                (profil.budgetMax <= 0 || ao.estimationDh <= profil.budgetMax) &&
                (motsCles.isEmpty() || motsCles.any { mc ->
                    ao.objet.lowercase().contains(mc) || ao.descriptif.lowercase().contains(mc)
                })
        }.sortedBy { it.dateLimiteEpoch }
    }

    // --- Sociétés / concurrence ------------------------------------------

    fun societe(id: String): Societe? = SampleData.societeById(id)

    fun societes(): List<Societe> = SampleData.societes.sortedByDescending { it.marchesGagnes }

    fun concurrents(ao: AppelOffre): List<Societe> =
        ao.concurrentsProbables.mapNotNull { SampleData.societeById(it) }

    // --- Favoris ---------------------------------------------------------

    fun favorisIds(): Flow<List<String>> = dao.observeFavoris()

    fun favoris(): Flow<List<AppelOffre>> =
        dao.observeFavoris().map { ids -> ids.mapNotNull { SampleData.appelOffreById(it) } }

    fun estFavori(id: String): Flow<Boolean> = dao.observeEstFavori(id)

    suspend fun basculerFavori(id: String, actuellementFavori: Boolean) {
        if (actuellementFavori) dao.retirerFavori(id)
        else dao.ajouterFavori(FavoriEntity(id))
    }

    // --- Checklist dossier ----------------------------------------------

    fun checklist(appelOffreId: String): Flow<Map<String, EtatPiece>> =
        dao.observeChecklist(appelOffreId).map { list ->
            list.associate { it.pieceCle to (EtatPiece.valueOf(it.etat)) }
        }

    suspend fun majPiece(appelOffreId: String, pieceCle: String, etat: EtatPiece) {
        dao.majPiece(ChecklistEntity(appelOffreId, pieceCle, etat.name))
    }
}
