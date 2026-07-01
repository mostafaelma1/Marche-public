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
    val concurrentsProbables: List<String> = emptyList(),
    /**
     * Pièces composant le dossier de consultation (DCE) fourni par l'acheteur.
     * Si la liste contient plusieurs pièces, le dossier est téléchargé en ZIP ;
     * sinon une simple fiche PDF est générée.
     */
    val piecesDossier: List<String> = emptyList()
)
