package com.bmarche.pro.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BMarcheDao {

    @Query("SELECT appelOffreId FROM favoris ORDER BY ajouteLe DESC")
    fun observeFavoris(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favoris WHERE appelOffreId = :id)")
    fun observeEstFavori(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun ajouterFavori(favori: FavoriEntity)

    @Query("DELETE FROM favoris WHERE appelOffreId = :id")
    suspend fun retirerFavori(id: String)

    @Query("SELECT * FROM checklist WHERE appelOffreId = :appelOffreId")
    fun observeChecklist(appelOffreId: String): Flow<List<ChecklistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun majPiece(entity: ChecklistEntity)

    @Delete
    suspend fun supprimerPiece(entity: ChecklistEntity)
}
