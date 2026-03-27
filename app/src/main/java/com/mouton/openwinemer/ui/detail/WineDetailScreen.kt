// WineDetailScreen.kt
@file:OptIn(ExperimentalMaterial3Api::class)

package com.mouton.openwinemer.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.HorizontalDivider
// import pour la fonction qui gère les champs optionnels
import com.mouton.openwinemer.data.model.WineEntity
// imports pour l'affichage du texte
import androidx.compose.ui.res.stringResource
import com.mouton.openwinemer.R
// import pour le scroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
// import pour le remplissage auto des champs
import kotlin.reflect.full.memberProperties
import androidx.compose.ui.platform.LocalContext // aussi pour le partage
// import pour le partage
import android.content.Intent
import androidx.compose.material.icons.filled.Share
import androidx.core.content.FileProvider
import java.io.File

/**
 * Écran affichant les détails d'un vin.
 * - Affiche les infos principales
 * - Permet de modifier le stock (+ / -)
 * - Permet d'éditer le vin
 * - Permet de supprimer le vin (avec confirmation)
 */

/**
 * Génère automatiquement les champs supplémentaires d'un vin,
 * en utilisant les traductions si disponibles.
 */
@Composable
private fun autoTranslatedFields(wine: WineEntity): List<Pair<String, String>> {
    val context = LocalContext.current

    // Champs déjà affichés manuellement
    val excluded = setOf(
        "name", "producer", "region", "color", "vintage",
        "stockQuantity", "generalDescription"
    )

    return WineEntity::class.memberProperties
        .filter { it.name !in excluded }
        .mapNotNull { prop ->
            val value = prop.get(wine) ?: return@mapNotNull null
            val textValue = value.toString().takeIf { it.isNotBlank() } ?: return@mapNotNull null

            // Nom de la clé dans strings.xml
            val key = "wine_${prop.name.replace(Regex("([A-Z])"), "_$1").lowercase()}"

            // Essayer de trouver la ressource string correspondante
            val resId = context.resources.getIdentifier(key, "string", context.packageName)

            val label =
                if (resId != 0)
                    stringResource(resId) // traduction trouvée
                else
                // fallback : transformer "mainGrape" → "Main grape"
                    prop.name.replace(Regex("([a-z])([A-Z])"), "$1 $2")
                        .replaceFirstChar { it.uppercase() }

            label to textValue
        }
}


// fonction utile pour ajouter les champs supplémentaires
@Composable
private fun buildDynamicFields(wine: WineEntity): List<Pair<String, String>> {
    val fields = mutableListOf<Pair<String, String>>()

    @Composable
    fun add(labelRes: Int, value: Any?) {
        if (value != null && value.toString().isNotBlank()) {
            fields.add(stringResource(labelRes) to value.toString())
        }
    }

    add(R.string.wine_cuvee, wine.cuvee)
    add(R.string.wine_type, wine.wineType)
    add(R.string.wine_country, wine.country)
    add(R.string.wine_sub_region, wine.subRegion)
    add(R.string.wine_appellation, wine.appellation)
    add(R.string.wine_classifications, wine.classifications)
    add(R.string.wine_main_grape, wine.mainGrape)
    add(R.string.wine_blend, wine.blend)
    add(R.string.wine_grape_percentages, wine.grapePercentages)
    add(R.string.wine_alcohol, wine.alcohol)
    add(R.string.wine_residual_sugar, wine.residualSugar)
    add(R.string.wine_acidity, wine.acidity)
    add(R.string.wine_ph, wine.ph)
    add(R.string.wine_volume_ml, wine.volumeMl)
    add(R.string.wine_cork_type, wine.corkType)
    add(R.string.wine_serving_temp, wine.servingTemp)
    add(R.string.wine_vinification_method, wine.vinificationMethod)
    add(R.string.wine_fermentation_type, wine.fermentationType)
    add(R.string.wine_ageing_duration, wine.ageingDuration)
    add(R.string.wine_barrel_type, wine.barrelType)
    add(R.string.wine_barrel_time, wine.barrelTime)
    add(R.string.wine_visual_aspect, wine.visualAspect)
    add(R.string.wine_aromas, wine.aromas)
    add(R.string.wine_flavors, wine.flavors)
    add(R.string.wine_structure, wine.structure)
    add(R.string.wine_finish, wine.finish)
    add(R.string.wine_global_rating, wine.globalRating)
    add(R.string.wine_recommended_dishes, wine.recommendedDishes)
    add(R.string.wine_cuisine_type, wine.cuisineType)
    add(R.string.wine_occasions, wine.occasions)
    add(R.string.wine_aging_potential, wine.ageingPotential)
    add(R.string.wine_optimal_date, wine.optimalDrinkDate)
    add(R.string.wine_label_state, wine.labelCondition)
    add(R.string.wine_awards, wine.awards)
    add(R.string.wine_critics, wine.reviews)
    add(R.string.wine_price, wine.prices)
    add(R.string.wine_availability, wine.availability)
    add(R.string.wine_distributor, wine.distributor)
    add(R.string.wine_sku, wine.sku)
    add(R.string.wine_barcode, wine.barcode)
    add(R.string.wine_storage_location, wine.location)
    add(R.string.wine_buy_date, wine.purchaseDate)
    add(R.string.wine_buy_price, wine.purchasePrice)

    return fields
}


/**
 * Génère automatiquement une liste (label, valeur) pour tous les champs non nuls
 * de WineEntity, sauf ceux déjà affichés manuellement.
 */
private fun autoFields(wine: WineEntity): List<Pair<String, String>> {

    // Champs que tu affiches déjà dans l'écran
    val excluded = setOf(
        "name", "producer", "region", "color", "vintage",
        "stockQuantity", "generalDescription"
    )

    return WineEntity::class.memberProperties
        .filter { it.name !in excluded }              // ignorer les champs déjà affichés
        .mapNotNull { prop ->
            val value = prop.get(wine) ?: return@mapNotNull null
            val text = value.toString().takeIf { it.isNotBlank() } ?: return@mapNotNull null

            // Convertir "mainGrape" → "Main grape"
            val label = prop.name
                .replace(Regex("([a-z])([A-Z])"), "$1 $2")
                .replaceFirstChar { it.uppercase() }

            label to text
        }
}


@Composable
fun WineDetailScreen(
    wineId: Long,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
    onDeleted: () -> Unit,
    viewModel: WineDetailViewModel = hiltViewModel()
) {
    val wine by viewModel.wine.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Charger le vin au lancement
    LaunchedEffect(wineId) {
        viewModel.loadWine(wineId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text((wine?.name ?: stringResource(R.string.wine_details))) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("<") }
                },
                actions = {
                    IconButton(onClick = { onEdit(wineId) }) {
                        Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.edit_button))
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete_button))
                    }
                    IconButton(onClick = {
                        val json = viewModel.exportCurrentWineAsJson() ?: return@IconButton

                        // 1) Créer un fichier temporaire
                        val file = File(context.cacheDir, "wine_export.json")
                        file.writeText(json)

                        // 2) Obtenir un URI sécurisé
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            file
                        )

                        // 3) Intent de partage
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/json"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }

                        val shareIntent = Intent.createChooser(
                            sendIntent,
                            context.getString(R.string.share_wine_title)
                        )

                        context.startActivity(shareIntent)
                    }) {
                        Icon(Icons.Filled.Share, contentDescription = stringResource(R.string.share_button))
                    }

                }
            )
        }
    ) { padding ->
        wine?.let { current ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // --- CHAMPS PRINCIPAUX (ceux à toujours afficher) ---
                //"${wine?.name ?: stringResource(R.string.wine_details)}"
                Text(stringResource(R.string.name) + " : " + (current.name ?: "-"))
                Text(stringResource(R.string.wine_producer, " : ", current.producer ?: "-"))
                Text(stringResource(R.string.region_label, " : ", current.region ?: "-"))
                Text(stringResource(R.string.color_label, " : ", current.color ?: "-"))
                Text(stringResource(R.string.year_label, " : ", current.vintage ?: "-"))

                Spacer(Modifier.height(16.dp))

                Text(stringResource(R.string.wine_stock, " : ", current.stockQuantity ?: 0))

                Row {
                    Button(onClick = { viewModel.updateStock(-1) }) { Text("-") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { viewModel.updateStock(1) }) { Text("+") }
                }

                Spacer(Modifier.height(24.dp))

                // --- DESCRIPTION GÉNÉRALE ---
                Text(stringResource(R.string.wine_general_desc, " : ", current.generalDescription ?: "-"))

                Spacer(Modifier.height(24.dp))

                // --- CHAMPS AUTOMATIQUES MULTILINGUES ---
                val extraFields = autoTranslatedFields(current)

                if (extraFields.isNotEmpty()) {
                    Spacer(Modifier.height(24.dp))
                    Text(
                        stringResource(R.string.additional_information),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))

                    extraFields.forEach { (label, value) ->
                        DetailRow(label, value)
                    }
                }

                /*
                // --- CHAMPS DYNAMIQUES (tous les autres renseignés) ---
                val dynamicFields = buildDynamicFields(current)

                if (dynamicFields.isNotEmpty()) {
                    Text(
                        stringResource(R.string.additional_information),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))

                    dynamicFields.forEach { (label, value) ->
                        DetailRow(label, value)
                    }
                }
                */
            }
        } ?: Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_prompt)) },
            text = { Text(stringResource(R.string.cannot_undo)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    scope.launch {
                        viewModel.deleteCurrentWine {
                            onDeleted()
                        }
                    }
                }) {
                    Text(stringResource(R.string.delete_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
        HorizontalDivider(Modifier.padding(top = 6.dp))
    }
}


@Composable
private fun old_DetailRow(label: String, value: String?) {
    if (value.isNullOrBlank()) return

    Column(Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
        HorizontalDivider(Modifier.padding(top = 6.dp))
    }
}

