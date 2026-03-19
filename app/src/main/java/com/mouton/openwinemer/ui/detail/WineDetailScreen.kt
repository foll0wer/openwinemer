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
import androidx.compose.ui.res.stringResource
import com.mouton.openwinemer.R


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
                title = { Text("${wine?.name ?: stringResource(R.string.wine_details)}") },
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
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(stringResource(R.string.name, " : ", current.name ?: "-"))
                Text(stringResource(R.string.wine_producer, " : ", current.producer ?: "-"))
                Text(stringResource(R.string.region_label, " : ", current.region ?: "-"))
                Text(stringResource(R.string.color_label, " : ", current.color ?: "-"))
                Text(stringResource(R.string.year_label, " : ", current.vintage ?: "-"))

                Spacer(Modifier.height(16.dp))

                Text(stringResource(R.string.wine_stock, " : ", current.stockQuantity ?: 0))
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

                Text(stringResource(R.string.wine_general_desc, " : ", current.generalDescription ?: "-"))
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
