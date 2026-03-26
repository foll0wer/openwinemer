// WineDatabase.kt
package com.mouton.openwinemer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mouton.openwinemer.data.model.WineEntity


/**
 * Room migration from version 1 to version 2.
 *
 * Purpose:
 * - Replace the old "price" column (REAL)
 *   with a new "price" column (TEXT) that stores
 *   a serialized list of Double values.
 *
 * Steps:
 * 1. Create a new table with the updated schema.
 * 2. Copy all existing data, converting the old price value to TEXT.
 * 3. Drop the old table.
 * 4. Rename the new table to the original name.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

        // 1. Create new table with price as TEXT
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

                price TEXT, -- new serialized list column

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
        // Convert old REAL price → TEXT
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
                price,
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

                -- Convert REAL → TEXT (list with a single value)
                CASE
                    WHEN price IS NULL THEN ''
                    ELSE CAST(price AS TEXT)
                END,

                availability, distributor, sku, barcode, stockQuantity, location,
                purchaseDate, purchasePrice, generalDescription
            FROM wines
        """)

        // 3. Drop old table
        database.execSQL("DROP TABLE wines")

        // 4. Rename new table to original name
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
