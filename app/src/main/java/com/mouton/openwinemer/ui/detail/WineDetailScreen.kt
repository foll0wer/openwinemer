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
// import pour la fonction qui gère les champs optionnels
import com.mouton.openwinemer.data.model.WineEntity
// imports pour l'affichage du texte
import androidx.compose.ui.res.stringResource
import com.mouton.openwinemer.R
// import pour le scroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll



/**
 * Écran affichant les détails d'un vin.
 * - Affiche les infos principales
 * - Permet de modifier le stock (+ / -)
 * - Permet d'éditer le vin
 * - Permet de supprimer le vin (avec confirmation)
 */

// fonction utile pour ajouter les champs supplémentaires
private fun buildDynamicFields(wine: WineEntity): List<Pair<String, String>> {
    val fields = mutableListOf<Pair<String, String>>()

    fun add(label: String, value: Any?) {
        if (value != null && value.toString().isNotBlank()) {
            fields.add(label to value.toString())
        }
    }

    add("Cuvée", wine.cuvee)
    add("Type", wine.wineType)
    add("Pays", wine.country)
    add("Sous-région", wine.subRegion)
    add("Appellation", wine.appellation)
    add("Classement", wine.classification)
    add("Cépage principal", wine.mainGrape)
    add("Assemblage", wine.blend)
    add("Cépages (%)", wine.grapePercentages)
    add("Alcool", wine.alcohol)
    add("Sucre résiduel", wine.residualSugar)
    add("Acidité", wine.acidity)
    add("pH", wine.ph)
    add("Volume (ml)", wine.volumeMl)
    add("Bouchon", wine.closureType)
    add("Température de service", wine.servingTemp)
    add("Vinification", wine.vinificationMethod)
    add("Fermentation", wine.fermentationType)
    add("Durée d’élevage", wine.ageingDuration)
    add("Type de fût", wine.barrelType)
    add("Temps en barrique", wine.barrelTime)
    add("Aspect visuel", wine.visualAspect)
    add("Arômes", wine.aromas)
    add("Saveurs", wine.flavors)
    add("Structure", wine.structure)
    add("Finale", wine.finish)
    add("Note globale", wine.globalRating)
    add("Plats recommandés", wine.recommendedDishes)
    add("Cuisine", wine.cuisineType)
    add("Occasions", wine.occasions)
    add("Potentiel de garde", wine.ageingPotential)
    add("À boire idéalement", wine.optimalDrinkDate)
    add("État étiquette", wine.labelCondition)
    add("Récompenses", wine.awards)
    add("Critiques", wine.reviews)
    add("Prix", wine.price)
    add("Disponibilité", wine.availability)
    add("Distributeur", wine.distributor)
    add("SKU", wine.sku)
    add("Code-barres", wine.barcode)
    add("Emplacement", wine.location)
    add("Date d’achat", wine.purchaseDate)
    add("Prix d’achat", wine.purchasePrice)

    return fields
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
                // --- CHAMPS PRINCIPAUX (ceux que tu avais déjà) ---
                DetailRow("Nom", current.name)
                DetailRow("Producteur", current.producer)
                DetailRow("Région", current.region)
                DetailRow("Couleur", current.color)
                DetailRow("Année", current.vintage?.toString())

                Spacer(Modifier.height(16.dp))

                // --- STOCK ---
                DetailRow("Stock", (current.stockQuantity ?: 0).toString())

                Row {
                    Button(onClick = { viewModel.updateStock(-1) }) { Text("-") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { viewModel.updateStock(1) }) { Text("+") }
                }

                Spacer(Modifier.height(24.dp))

                // --- DESCRIPTION GÉNÉRALE ---
                DetailRow("Description", current.generalDescription)

                Spacer(Modifier.height(24.dp))

                // --- CHAMPS DYNAMIQUES (tous les autres renseignés) ---
                val dynamicFields = buildDynamicFields(current)

                if (dynamicFields.isNotEmpty()) {
                    Text(
                        "Informations supplémentaires",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))

                    dynamicFields.forEach { (label, value) ->
                        DetailRow(label, value)
                    }
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
private fun DetailRow(label: String, value: String?) {
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
        Divider(Modifier.padding(top = 6.dp))
    }
}

