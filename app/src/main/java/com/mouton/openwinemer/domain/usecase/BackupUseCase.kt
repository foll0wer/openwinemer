// BackupUseCase.kt
package com.mouton.openwinemer.domain.usecase

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mouton.openwinemer.data.local.WineDao
import com.mouton.openwinemer.data.model.WineEntity
import com.mouton.openwinemer.util.BackupCrypto
import kotlinx.coroutines.flow.first
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.res.stringResource
import com.mouton.openwinemer.R
import com.mouton.openwinemer.util.CsvExporter

/**
 * Cette classe gère :
 * - l'export JSON (avec ou sans mot de passe)
 * - l'import JSON (avec ou sans mot de passe)
 * - l'export CSV (lisible par Excel)
 * - l'export "Excel" (en réalité un CSV compatible Excel)
 */
class BackupUseCase(
    private val context: Context,
    private val wineDao: WineDao,
    private val gson: Gson = Gson()
) {

    /**
     * Génère une chaîne de caractères représentant la date et l'heure actuelles.
     * Exemple : "2026-03-12_22-45-30"
     */
    private fun currentTimestamp(): String {
        val format = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
        return format.format(Date())
    }

    // ------------------------------------------------------------
    // 1) EXPORT JSON CHIFFRÉ DANS UN DOSSIER CHOISI PAR L’UTILISATEUR
    // ------------------------------------------------------------
    suspend fun exportBackupToFolder(treeUri: Uri, password: String?) {
        // On récupère tous les vins depuis la base de données.
        val wines = wineDao.getAllWines().first()

        // On convertit la liste de vins en JSON.
        val json = gson.toJson(wines)

        // Si un mot de passe est fourni, on chiffre le JSON.
        val content = if (password.isNullOrBlank()) json else BackupCrypto.encrypt(json, password)

        // On accède au dossier choisi par l'utilisateur.
        val root = DocumentFile.fromTreeUri(context, treeUri)
            ?: throw IllegalStateException(context.getString(R.string.cant_access_folder))

        // On crée un fichier avec la date et l'heure dans le nom.
        val fileName = "openwinemer_backup_${currentTimestamp()}.json"
        val file = root.createFile("application/json", fileName)
            ?: throw IllegalStateException(context.getString(R.string.cant_create_here))

        // On écrit le contenu dans le fichier.
        context.contentResolver.openOutputStream(file.uri)?.use { out ->
            out.write(content.toByteArray())
        }
    }

    // ------------------------------------------------------------
    // 2) IMPORT JSON (CHIFFRÉ OU NON) DEPUIS UN URI
    // ------------------------------------------------------------
    suspend fun importBackupFromUri(uri: Uri, password: String?, replace: Boolean) {
        // 1) Lire le fichier
        val content = context.contentResolver.openInputStream(uri)
            ?.readBytes()
            ?.decodeToString()
            ?: throw IllegalStateException(context.getString(R.string.cant_read_file))

        // 2) Déchiffrement éventuel
        val jsonString = if (password.isNullOrBlank()) {
            content
        } else {
            try {
                BackupCrypto.decrypt(content, password)
            } catch (e: Exception) {
                throw IllegalStateException(context.getString(R.string.wrong_pwd_or_file))
            }
        }

        // 3) Parser JSON
        val type = TypeToken.getParameterized(List::class.java, WineEntity::class.java).type
        val wines: List<WineEntity> = try {
            gson.fromJson(jsonString, type)
        } catch (e: Exception) {
            throw IllegalStateException(context.getString(R.string.invalid_save_file))
        }

        // 4) Remplacer ou fusionner
        if (replace) {
            // 🔥 MODE REMPLACER : on efface tout et on laisse Room recréer les IDs
            wineDao.clearAll()
            val winesWithoutId = wines.map { it.copy(id = 0) }
            wineDao.insertAll(winesWithoutId)
        } else {
            // 🔥 MODE FUSIONNER : on récupère le dernier ID existant
            val lastId = wineDao.getLastId() ?: 0L
            // On renumérote les vins importés pour éviter les collisions
            var nextId = lastId + 1
            val winesWithNewIds = wines.map { wine ->
                wine.copy(id = nextId++)
            }
            wineDao.insertOrUpdate(winesWithNewIds)
        }
    }



    // ------------------------------------------------------------
    // 3) EXPORT CSV (lisible par Excel)
    // ------------------------------------------------------------
    suspend fun exportCsvToFolder(treeUri: Uri) {
        val wines = wineDao.getAllWines().first()

        // 1) Générer un fichier CSV dans le stockage interne
        val tempFile = CsvExporter.exportToCsv(
            context = context,
            wines = wines,
            fileName = "openwinemer_export_${currentTimestamp()}.csv"
        )

        // 2) Accéder au dossier choisi par l'utilisateur
        val root = DocumentFile.fromTreeUri(context, treeUri)
            ?: throw IllegalStateException(context.getString(R.string.cant_access_folder))

        // 3) Créer le fichier dans ce dossier
        val file = root.createFile("text/csv", tempFile.name)
            ?: throw IllegalStateException(context.getString(R.string.cant_make_csv))

        // 4) Copier le contenu du fichier temporaire vers l'URI SAF
        context.contentResolver.openOutputStream(file.uri)?.use { out ->
            tempFile.inputStream().use { input ->
                input.copyTo(out)
            }
        }
    }

    suspend fun old_exportCsvToFolder(treeUri: Uri) {
        val wines = wineDao.getAllWines().first()

        val root = DocumentFile.fromTreeUri(context, treeUri)
            ?: throw IllegalStateException(context.getString(R.string.cant_access_folder))

        // Nom de fichier avec date et heure.
        val fileName = "openwinemer_export_${currentTimestamp()}.csv"
        val file = root.createFile("text/csv", fileName)
            ?: throw IllegalStateException(context.getString(R.string.cant_make_csv))

        context.contentResolver.openOutputStream(file.uri)?.use { out ->
            val writer = OutputStreamWriter(out)

            // En-têtes CSV (colonnes).
            writer.write("id,name,producer,region,color,vintage,stock\n")

            // Chaque vin devient une ligne CSV.
            wines.forEach { wine ->
                writer.write(
                    "${wine.id}," +
                            "\"${wine.name ?: ""}\"," +
                            "\"${wine.producer ?: ""}\"," +
                            "\"${wine.region ?: ""}\"," +
                            "\"${wine.color ?: ""}\"," +
                            "${wine.vintage ?: ""}," +
                            "${wine.stockQuantity ?: 0}\n"
                )
            }

            writer.flush()
        }
    }

    // ------------------------------------------------------------
    // 4) EXPORT "EXCEL" (CSV COMPATIBLE EXCEL)
    // ------------------------------------------------------------
    suspend fun exportExcelToFolder(treeUri: Uri) {
        // Excel lit très bien les fichiers CSV.
        // On réutilise donc la même logique que pour le CSV.
        exportCsvToFolder(treeUri)
    }
}
