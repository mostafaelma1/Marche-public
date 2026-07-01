package com.bmarche.pro.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Marché mis en favori par l'utilisateur. */
@Entity(tableName = "favoris")
data class FavoriEntity(
    @PrimaryKey val appelOffreId: String,
    val ajouteLe: Long = System.currentTimeMillis()
)

/**
 * État d'avancement d'une pièce du dossier, propre à un marché donné.
 * Clé composite (appelOffreId + pieceCle).
 */
@Entity(tableName = "checklist", primaryKeys = ["appelOffreId", "pieceCle"])
data class ChecklistEntity(
    val appelOffreId: String,
    val pieceCle: String,
    /** Nom de [com.bmarche.pro.data.model.EtatPiece]. */
    val etat: String
)
