// WineDatabase.kt
package com.mouton.openwinemer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mouton.openwinemer.data.model.WineEntity


/**
 * Migration from version 1 to version 2.
 *
 * Version 1 stored a single price (REAL).
 * Version 2 stores a JSON-serialized list of PriceEntryEntity objects.
 *
 * This migration:
 * 1. Creates a new table with the new "prices" TEXT column.
 * 2. Converts the old price value into a JSON list with today's date.
 * 3. Copies all other fields unchanged.
 * 4. Replaces the old table.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

        // 1. Create new table with updated schema
        database.execSQL("""
            CREATE TABLE wines_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT,
                producer TEXT,
                cuvee TEXT,
                vintage INTEGER,
                wineType TEXT,
                color TEXT,

                country TEXT,
                region TEXT,
                subRegion TEXT,
                appellation TEXT,
                classification TEXT,

                mainGrape TEXT,
                blend TEXT,
                grapePercentages TEXT,

                alcohol REAL,
                residualSugar REAL,
                acidity REAL,
                ph REAL,
                volumeMl INTEGER,
                closureType TEXT,
                servingTemp TEXT,

                vinificationMethod TEXT,
                fermentationType TEXT,
                ageingDuration TEXT,
                barrelType TEXT,
                barrelTime TEXT,

                visualAspect TEXT,
                aromas TEXT,
                flavors TEXT,
                structure TEXT,
                finish TEXT,
                globalRating REAL,

                recommendedDishes TEXT,
                cuisineType TEXT,
                occasions TEXT,

                ageingPotential TEXT,
                optimalDrinkDate TEXT,
                labelCondition TEXT,
                awards TEXT,
                reviews TEXT,

                -- NEW FIELD: JSON list of PriceEntryEntity
                prices TEXT,

                availability TEXT,
                distributor TEXT,
                sku TEXT,
                barcode TEXT,
                stockQuantity INTEGER,
                location TEXT,
                purchaseDate TEXT,
                purchasePrice REAL,
                generalDescription TEXT
            )
        """)

        // 2. Copy data from old table → new table
        // Convert old price into a JSON list with today's date
        database.execSQL("""
            INSERT INTO wines_new (
                id, name, producer, cuvee, vintage, wineType, color,
                country, region, subRegion, appellation, classification,
                mainGrape, blend, grapePercentages,
                alcohol, residualSugar, acidity, ph, volumeMl, closureType, servingTemp,
                vinificationMethod, fermentationType, ageingDuration, barrelType, barrelTime,
                visualAspect, aromas, flavors, structure, finish, globalRating,
                recommendedDishes, cuisineType, occasions,
                ageingPotential, optimalDrinkDate, labelCondition, awards, reviews,

                prices,

                availability, distributor, sku, barcode, stockQuantity, location,
                purchaseDate, purchasePrice, generalDescription
            )
            SELECT
                id, name, producer, cuvee, vintage, wineType, color,
                country, region, subRegion, appellation, classification,
                mainGrape, blend, grapePercentages,
                alcohol, residualSugar, acidity, ph, volumeMl, closureType, servingTemp,
                vinificationMethod, fermentationType, ageingDuration, barrelType, barrelTime,
                visualAspect, aromas, flavors, structure, finish, globalRating,
                recommendedDishes, cuisineType, occasions,
                ageingPotential, optimalDrinkDate, labelCondition, awards, reviews,

                CASE
                    WHEN price IS NULL THEN '[]'
                    ELSE '[{"price": ' || price || ', "date": "' || date('now') || '"}]'
                END,

                availability, distributor, sku, barcode, stockQuantity, location,
                purchaseDate, purchasePrice, generalDescription
            FROM wines
        """)

        // 3. Remove old table
        database.execSQL("DROP TABLE wines")

        // 4. Rename new table
        database.execSQL("ALTER TABLE wines_new RENAME TO wines")
    }
}




// @Database relie les entités à la base Room.
@Database(
    entities = [WineEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class WineDatabase : RoomDatabase() {
    abstract fun wineDao(): WineDao
}
