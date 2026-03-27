// CsvExporter.kt
package com.mouton.openwinemer.util

import android.content.Context
import com.mouton.openwinemer.data.model.WineEntity
import java.io.File

object CsvExporter {

    // Cette fonction crée un fichier CSV dans le stockage interne de l’app.
    fun exportToCsv(
        context: Context,
        wines: List<WineEntity>,
        fileName: String = "openwinemer_export.csv"
    ): File {
        val file = File(context.filesDir, fileName)

        fun Any?.safe() = this?.toString() ?: ""
        file.bufferedWriter().use { out ->
            // Ligne d’en-tête (noms des colonnes).
            out.write(
                "id;name;producer;cuvee;vintage;wineType;color;" +
                        "country;region;subRegion;appellation;classification;" +
                        "mainGrape;blend;grapePercentages;" +
                        "alcohol;residualSugar;acidity;ph;volumeMl;closureType;servingTemp;" +
                        "vinificationMethod;fermentationType;ageingDuration;barrelType;barrelTime;" +
                        "visualAspect;aromas;flavors;structure;finish;globalRating;" +
                        "recommendedDishes;cuisineType;occasions;" +
                        "ageingPotential;optimalDrinkDate;labelCondition;" +
                        "awards;reviews;price;availability;distributor;sku;barcode;" +
                        "stockQuantity;location;purchaseDate;purchasePrice;generalDescription\n"
            )
            wines.forEach { w ->
                out.write(
                    listOf(
                        w.id.safe(),
                        w.name.safe(),
                        w.producer.safe(),
                        w.cuvee.safe(),
                        w.vintage.safe(),
                        w.wineType.safe(),
                        w.color.safe(),
                        w.country.safe(),
                        w.region.safe(),
                        w.subRegion.safe(),
                        w.appellation.safe(),
                        w.classifications.safe(),
                        w.mainGrape.safe(),
                        w.blend.safe(),
                        w.grapePercentages.safe(),
                        w.alcohol.safe(),
                        w.residualSugar.safe(),
                        w.acidity.safe(),
                        w.ph.safe(),
                        w.volumeMl.safe(),
                        w.corkType.safe(),
                        w.servingTemp.safe(),
                        w.vinificationMethod.safe(),
                        w.fermentationType.safe(),
                        w.ageingDuration.safe(),
                        w.barrelType.safe(),
                        w.barrelTime.safe(),
                        w.visualAspect.safe(),
                        w.aromas.safe(),
                        w.flavors.safe(),
                        w.structure.safe(),
                        w.finish.safe(),
                        w.globalRating.safe(),
                        w.recommendedDishes.safe(),
                        w.cuisineType.safe(),
                        w.occasions.safe(),
                        w.ageingPotential.safe(),
                        w.optimalDrinkDate.safe(),
                        w.labelCondition.safe(),
                        w.awards.safe(),
                        w.reviews.safe(),
                        w.prices.safe(),
                        w.availability.safe(),
                        w.distributor.safe(),
                        w.sku.safe(),
                        w.barcode.safe(),
                        w.stockQuantity.safe(),
                        w.location.safe(),
                        w.purchaseDate.safe(),
                        w.purchasePrice.safe(),
                        w.generalDescription.safe()
                    ).joinToString(";") + "\n"
                )
            }
        }

        return file
    }
}
