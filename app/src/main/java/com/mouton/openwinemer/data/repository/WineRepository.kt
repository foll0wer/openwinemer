// WineRepository.kt
package com.mouton.openwinemer.data.repository

import com.mouton.openwinemer.data.local.WineDao
import com.mouton.openwinemer.data.model.WineEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
// pour le partage d'un vin
import kotlinx.serialization.Serializable
// pour le partage de plusieurs vins
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import com.mouton.openwinemer.data.model.PriceEntryEntity


/**
 * Le Repository sert d’intermédiaire entre la base Room (WineDao)
 * et les ViewModels.
 */

private val jsonParser = Json {
    prettyPrint = true
    encodeDefaults = true
}


@Singleton
@Serializable
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

    /** partage json depuis la vue d'un vin */
    fun exportWineAsJson(wine: WineEntity): String {
        return kotlinx.serialization.json.Json {
            prettyPrint = true
            encodeDefaults = true
        }.encodeToString(WineEntity.serializer(), wine)
    }

    suspend fun getWinesByIds(ids: Set<Long>): List<WineEntity> {
        return wineDao.getWinesByIds(ids.toList())
    }

    fun exportWinesAsJson(wines: List<WineEntity>): String {
        return kotlinx.serialization.json.Json {
            prettyPrint = true
            encodeDefaults = true
        }.encodeToString(
            kotlinx.serialization.builtins.ListSerializer(WineEntity.serializer()),
            wines
        )
    }

}
