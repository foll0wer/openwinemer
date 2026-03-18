// WineRepository.kt
package com.mouton.openwinemer.data.repository

import com.mouton.openwinemer.data.local.WineDao
import com.mouton.openwinemer.data.model.WineEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Le Repository sert d’intermédiaire entre la base Room (WineDao)
 * et les ViewModels.
 */
@Singleton
class WineRepository @Inject constructor(
    private val wineDao: WineDao
) {

    /** Retourne tous les vins sous forme de Flow. */
    fun getAllWinesFlow(): Flow<List<WineEntity>> = wineDao.getAllWines()

    /** Retourne un vin par son ID. */
    suspend fun getWineById(id: Long): WineEntity? = wineDao.getWineById(id)

    /** Ajoute un vin (utilisé par AddEditWineViewModel). */
    suspend fun addWine(wine: WineEntity) {
        wineDao.insertWine(wine)
    }

    /** Met à jour un vin existant. */
    suspend fun updateWine(wine: WineEntity) {
        wineDao.updateWine(wine)
    }

    /** Supprime un vin. */
    suspend fun deleteWine(wine: WineEntity) {
        wineDao.deleteWine(wine)
    }

    /** Supprime un vin par ID (utilisé pour la sélection multiple). */
    suspend fun deleteWineById(id: Long) {
        wineDao.deleteWineById(id)
    }

    /** Supprime plusieurs vins d’un coup. */
    suspend fun deleteWinesByIds(ids: Set<Long>) {
        wineDao.deleteWinesByIds(ids.toList())
    }
}
