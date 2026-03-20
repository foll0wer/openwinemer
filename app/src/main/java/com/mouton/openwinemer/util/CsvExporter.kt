// CsvExporter.kt
package com.mouton.openwinemer.util

import android.content.Context
import com.mouton.openwinemer.data.model.WineEntity
import java.io.File

object CsvExporter {

    // Récupère les propriétés dans l'ordre EXACT de déclaration dans la data class
    private fun orderedFields(clazz: Class<*>): List<java.lang.reflect.Field> {
        return clazz.declaredFields
            .filter { !it.name.contains("$") } // ignore les champs internes Kotlin
    }

    // Génère l'en-tête CSV automatiquement
    private fun csvHeader(clazz: Class<*>): String {
        return orderedFields(clazz)
            .joinToString(";") { it.name }
    }

    // Génère une ligne CSV pour un objet
    private fun csvLine(obj: Any): String {
        val clazz = obj::class.java
        return orderedFields(clazz)
            .joinToString(";") { field ->
                field.isAccessible = true
                val value = field.get(obj)
                value?.toString() ?: ""
            }
    }

    fun exportToCsv(
        context: Context,
        wines: List<WineEntity>,
        fileName: String = "openwinemer_export.csv"
    ): File {

        val file = File(context.filesDir, fileName)

        file.bufferedWriter().use { out ->

            // 1) Écrire l'en-tête automatiquement
            out.write(csvHeader(WineEntity::class.java) + "\n")

            // 2) Écrire chaque ligne automatiquement
            wines.forEach { wine ->
                out.write(csvLine(wine) + "\n")
            }
        }

        return file
    }
}
