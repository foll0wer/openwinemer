// WineDatabase.kt
package com.mouton.openwinemer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mouton.openwinemer.data.model.WineEntity

// @Database relie les entités à la base Room.
@Database(
    entities = [WineEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class WineDatabase : RoomDatabase() {
    abstract fun wineDao(): WineDao
}
