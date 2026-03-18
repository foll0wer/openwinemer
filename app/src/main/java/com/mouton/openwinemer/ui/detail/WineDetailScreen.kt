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

/**
 * Écran affichant les détails d'un vin.
 * - Affiche les infos principales
 * - Permet de modifier le stock (+ / -)
 * - Permet d'éditer le vin
 * - Permet de supprimer le vin (avec confirmation)
 */
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
                title = { Text(wine?.name ?: "Détail du vin") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("<") }
                },
                actions = {
                    IconButton(onClick = { onEdit(wineId) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Modifier")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Supprimer")
                    }
                }
            )
        }
    ) { padding ->
        wine?.let { current ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text("Nom : ${current.name ?: "-"}")
                Text("Producteur : ${current.producer ?: "-"}")
                Text("Région : ${current.region ?: "-"}")
                Text("Couleur : ${current.color ?: "-"}")
                Text("Millésime : ${current.vintage ?: "-"}")

                Spacer(Modifier.height(16.dp))

                Text("Stock actuel : ${current.stockQuantity ?: 0}")
                Row {
                    Button(onClick = { viewModel.updateStock(-1) }) {
                        Text("-")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { viewModel.updateStock(1) }) {
                        Text("+")
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text("Description : ${current.generalDescription ?: "-"}")
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
            title = { Text("Supprimer ce vin ?") },
            text = { Text("Cette action est irréversible.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    scope.launch {
                        viewModel.deleteCurrentWine {
                            onDeleted()
                        }
                    }
                }) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}
