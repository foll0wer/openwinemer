package com.mouton.openwinemer.data.local

import androidx.room.TypeConverter
import com.mouton.openwinemer.data.model.PriceEntryEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Converts lists of PriceEntryEntity to/from JSON for Room storage.
 */
class Converters {

    @TypeConverter
    fun fromPriceEntryList(list: List<PriceEntryEntity>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toPriceEntryList(data: String): List<PriceEntryEntity> {
        if (data.isEmpty()) return emptyList()
        return Json.decodeFromString(data)
    }
}
