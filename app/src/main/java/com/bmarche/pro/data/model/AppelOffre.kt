package com.bmarche.pro.data.model

import kotlinx.serialization.Serializable

/**
 * Un appel d'offres (marché public). Le montant estimé est exprimé en dirhams (DH)
 * hors taxes, tel que publié par l'administration.
 */
@Serializable
data class AppelOffre(
    val id: String,
    val reference: String,
    val objet: String,
    val acheteur: String,
    val ville: String,
    val domaine: Domaine,
    val estimationDh: Double,
    val cautionProvisoireDh: Double,
    val dateLimiteEpoch: Long,
    val descriptif: String = "",
    /** Identifiants des sociétés historiquement actives sur ce type de marché. */
    val concurrentsProbables: List<String> = emptyList()
)
