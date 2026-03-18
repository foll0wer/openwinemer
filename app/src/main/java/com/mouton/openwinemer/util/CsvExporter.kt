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
        file.bufferedWriter().use { out ->
            // Ligne d’en-tête (noms des colonnes).
            out.write(
                "id;name;producer;cuvee;vintage;wineType;color;country;region;stockQuantity\n"
            )
            wines.forEach { w ->
                out.write(
                    "${w.id};${w.name ?: ""};${w.producer ?: ""};${w.cuvee ?: ""};" +
                            "${w.vintage ?: ""};${w.wineType ?: ""};${w.color ?: ""};" +
                            "${w.country ?: ""};${w.region ?: ""};${w.stockQuantity ?: ""}\n"
                )
            }
        }
        return file
    }
}
