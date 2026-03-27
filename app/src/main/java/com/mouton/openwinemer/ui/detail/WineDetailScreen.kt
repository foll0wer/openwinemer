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
import androidx.compose.material.icons.filled.AttachMoney
// import for price list screen history and click
import java.time.LocalDate
import androidx.compose.foundation.clickable


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
        "stockQuantity", "generalDescription",
        "prices"
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

@Composable
fun WineDetailScreen(
    wineId: Long,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
    onDeleted: () -> Unit,
    onShowPriceHistory: (Long) -> Unit,
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
                    // State controlling the dropdown menu
                    var showEditMenu by remember { mutableStateOf(false) }

                    Box {
                        IconButton(onClick = { showEditMenu = true }) {
                            Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.edit_button))
                        }

                        DropdownMenu(
                            expanded = showEditMenu,
                            onDismissRequest = { showEditMenu = false }
                        ) {
                            // --- EDIT WINE ---
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.edit_wine)) },
                                leadingIcon = {
                                    Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.edit_button))
                                },
                                onClick = {
                                    showEditMenu = false
                                    onEdit(wineId)
                                }
                            )

                            // --- INSERT NEW PRICE ---
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.insert_new_price)) },
                                leadingIcon = {
                                    Icon(Icons.Filled.AttachMoney, contentDescription = null)
                                },
                                onClick = {
                                    showEditMenu = false
                                    // Navigate to price history screen
                                    onShowPriceHistory(wineId)
                                }
                            )
                        }
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
                Text("${stringResource(R.string.name)} : ${current.name ?: "-"}")
                Text("${stringResource(R.string.wine_producer)} : ${current.producer ?: "-"}")
                Text("${stringResource(R.string.region_label)} : ${current.region ?: "-"}")
                Text("${stringResource(R.string.color_label)} : ${current.color ?: "-"}")
                Text("${stringResource(R.string.year_label)} : ${current.vintage ?: "-"}")

                Spacer(Modifier.height(16.dp))

                Text("${stringResource(R.string.wine_stock)} : ${current.stockQuantity ?: 0}")

                Row {
                    Button(onClick = { viewModel.updateStock(-1) }) { Text("-") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { viewModel.updateStock(1) }) { Text("+") }
                }

                Spacer(Modifier.height(24.dp))

                // --- DESCRIPTION GÉNÉRALE ---
                Text("${stringResource(R.string.wine_general_desc)} : ${current.generalDescription ?: "-"}")

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

                // --- LATEST PRICE CARD ---
                val latestPrice = current.prices.maxByOrNull { LocalDate.parse(it.date) }

                if (latestPrice != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onShowPriceHistory(wineId) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.latest_price),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.height(4.dp))
                            Text("${latestPrice.price} €", style = MaterialTheme.typography.bodyLarge)
                            Text(latestPrice.date, style = MaterialTheme.typography.bodyMedium)
                            Text(latestPrice.source, style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }

                // --- PRICE TREND GRAPH ---
                if (current.prices.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.price_trend),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    PriceTrendGraph(current.prices)
                    Spacer(Modifier.height(24.dp))
                }
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
