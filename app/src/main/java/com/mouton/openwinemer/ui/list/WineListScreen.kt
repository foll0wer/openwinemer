@file:OptIn(ExperimentalMaterial3Api::class,ExperimentalFoundationApi::class)

package com.mouton.openwinemer.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mouton.openwinemer.data.model.WineEntity
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.res.stringResource
import com.mouton.openwinemer.R

/**
 * Écran listant les vins avec :
 * - tri
 * - filtres
 * - recherche
 * - sélection multiple
 */
@Composable
fun WineListScreen(
    mode: String,
    onWineClick: (Long) -> Unit,
    onAddWine: () -> Unit,
    viewModel: WineListViewModel = hiltViewModel()
) {
    val wines by viewModel.wines.collectAsState()
    var selectedIds by remember { mutableStateOf(setOf<Long>()) }
    val scope = rememberCoroutineScope()

    // États pour afficher les boîtes de dialogue
    var showSortDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    // Appliquer le mode (ALL / REGION / COLOR)
    LaunchedEffect(mode) {
        viewModel.setMode(mode)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        if (selectedIds.isEmpty()) Text(stringResource(R.string.wines))
                        else Text(stringResource(selectedIds.size, R.string.selected_amount))
                    },
                    actions = {
                        if (selectedIds.isNotEmpty()) {
                            TextButton(onClick = {
                                selectedIds = wines.map { it.id }.toSet()
                            }) { Text(stringResource(R.string.select_all)) }

                            TextButton(onClick = {
                                scope.launch {
                                    viewModel.deleteWines(selectedIds)
                                    selectedIds = emptySet()
                                }
                            }) { Text(stringResource(R.string.delete_all)) }
                        }
                    }
                )

                // Barre de recherche
                val search by viewModel.search.collectAsState()

                OutlinedTextField(
                    value = search,
                    onValueChange = { viewModel.setSearch(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text(stringResource(R.string.search_bar)) }
                )


                // Boutons tri + filtre
                Row(Modifier.padding(8.dp)) {
                    Button(onClick = { showSortDialog = true }) {
                        Text(stringResource(R.string.sort_dialog))
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { showFilterDialog = true }) {
                        Text(stringResource(R.string.filter_dialog))
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddWine) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(wines) { wine ->
                val isSelected = selectedIds.contains(wine.id)

                WineListItemRow(
                    wine = wine,
                    isSelected = isSelected,
                    onClick = {
                        if (selectedIds.isNotEmpty()) {
                            selectedIds = if (isSelected) selectedIds - wine.id else selectedIds + wine.id
                        } else {
                            onWineClick(wine.id)
                        }
                    },
                    onLongClick = {
                        selectedIds = selectedIds + wine.id
                    }
                )
            }
        }
    }

    // Boîte de dialogue de tri
    if (showSortDialog) {
        SortDialog(
            onDismiss = { showSortDialog = false },
            onSortSelected = { mode ->
                viewModel.setSortMode(mode)
                showSortDialog = false
            }
        )
    }

    // Boîte de dialogue de filtres
    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            onApply = { region, color ->
                viewModel.setRegionFilter(region)
                viewModel.setColorFilter(color)
                showFilterDialog = false
            }
        )
    }
}

@Composable
fun WineListItemRow(
    wine: WineEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Color(0xFFE0E0E0) else Color.Transparent)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(16.dp)
    ) {
        Column {
            Text(wine.name ?: stringResource(R.string.unknown_wine_name), style = MaterialTheme.typography.titleMedium)
            Text(wine.producer ?: stringResource(R.string.unknown_wine_producer))
            Text(stringResource(wine.region ?: R.string.unknown_wine_region, " • ", wine.color ?: R.string.unknown_wine_color))}
        }
    }
}

/** Boîte de dialogue de tri */
@Composable
fun SortDialog(
    onDismiss: () -> Unit,
    onSortSelected: (SortMode) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.sort_by_button) },
        text = {
            Column {
                SortMode.values().forEach { mode ->
                    TextButton(onClick = { onSortSelected(mode) }) {
                        Text(mode.name)
                    }
                }
            }
        },
        confirmButton = {}
    )
}

/** Boîte de dialogue de filtres */
@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onApply: (String?, String?) -> Unit
) {
    var region by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.filter_list)) },
        text = {
            Column {
                OutlinedTextField(
                    value = region,
                    onValueChange = { region = it },
                    label = { Text(stringResource(R.string.region_label)) }
                )
                OutlinedTextField(
                    value = color,
                    onValueChange = { color = it },
                    label = { Text(stringResource(R.string.color_label)) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onApply(region.ifBlank { null }, color.ifBlank { null })
            }) {
                Text(stringResource(R.string.apply))
            }
        }
    )
}
