package com.mouton.openwinemer.data.local

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromList(list: List<Double>): String {
        return list.joinToString(separator = ",")
    }

    @TypeConverter
    fun toList(data: String): List<Double> {
        if (data.isEmpty()) return emptyList()
        return data.split(",").map { it.toDouble() }
    }
}
