// WineDao.kt
package com.mouton.openwinemer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mouton.openwinemer.data.model.WineEntity
import kotlinx.coroutines.flow.Flow
import com.mouton.openwinemer.data.model.PriceEntryEntity

/**
 * DAO = Data Access Object
 * C’est ici que Room génère automatiquement le code SQL.
 */
@Dao
interface WineDao {

    /** Retourne tous les vins sous forme de Flow (mise à jour automatique). */
    @Query("SELECT * FROM wines")
    fun getAllWines(): Flow<List<WineEntity>>

    /** Retourne un vin par son ID. */
    @Query("SELECT * FROM wines WHERE id = :id LIMIT 1")
    suspend fun getWineById(id: Long): WineEntity?

    /** Ajoute ou remplace un vin. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWine(wine: WineEntity)

    /** Met à jour un vin existant. */
    @Update
    suspend fun updateWine(wine: WineEntity)

    /** Supprime un vin. */
    @Delete
    suspend fun deleteWine(wine: WineEntity)

    /** Supprime un vin par ID. */
    @Query("DELETE FROM wines WHERE id = :id")
    suspend fun deleteWineById(id: Long)

    /** Supprime plusieurs vins d’un coup. */
    @Query("DELETE FROM wines WHERE id IN (:ids)")
    suspend fun deleteWinesByIds(ids: List<Long>)

    @Query("SELECT * FROM wines WHERE id IN (:ids)")
    suspend fun getWinesByIds(ids: List<Long>): List<WineEntity>

    @Query("DELETE FROM wines")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(wines: List<WineEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(wines: List<WineEntity>)

    @Query("SELECT MAX(id) FROM wines")
    suspend fun getLastId(): Long?

    /*
    @Insert
    suspend fun insertPriceEntry(entry: PriceEntryEntity)

    @Update
    suspend fun updatePriceEntry(entry: PriceEntryEntity)

    @Delete
    suspend fun deletePriceEntry(entry: PriceEntryEntity)
    */
}
