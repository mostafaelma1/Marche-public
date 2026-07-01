package com.bmarche.pro.data.model

import kotlinx.serialization.Serializable

/**
 * Fiche historique d'une société concurrente, alimentée à partir des résultats
 * publiés des marchés. Sert à l'analyse de la concurrence.
 */
@Serializable
data class Societe(
    val id: String,
    val nom: String,
    val marchesGagnes: Int,
    val domaines: List<Domaine>,
    val villes: List<String>,
    /** Taux de rabais moyen appliqué par la société, en pourcentage (ex: 12.5 = -12,5%). */
    val tauxRabaisMoyen: Double,
    /** Administrations avec lesquelles la société travaille le plus souvent. */
    val acheteursFrequents: List<String>
)
