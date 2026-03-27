package com.mouton.openwinemer.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mouton.openwinemer.data.model.PriceEntryEntity
import java.time.LocalDate
import androidx.compose.ui.res.stringResource
import com.mouton.openwinemer.R
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import java.time.Instant
import java.time.ZoneId
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceHistoryScreen(
    wineId: Long,
    navController: NavController,
    viewModel: WineDetailViewModel = hiltViewModel()
) {
    // Load the wine when entering the screen
    LaunchedEffect(wineId) {
        viewModel.loadWine(wineId)
    }

    val wine by viewModel.wine.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editingEntry by remember { mutableStateOf<PriceEntryEntity?>(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var entryToDelete by remember { mutableStateOf<PriceEntryEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(stringResource(R.string.price_history_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text(stringResource(R.string.back_button_symbol))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingEntry = null
                    showDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_price))
            }
        }
    ) { paddingValues ->

        val currentWine = wine ?: return@Scaffold

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(
                currentWine.prices.sortedByDescending { LocalDate.parse(it.date) }
            ) { entry ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("${entry.price} €", style = MaterialTheme.typography.bodyLarge)
                            Text(entry.date, style = MaterialTheme.typography.bodyMedium)
                            Text(entry.source, style = MaterialTheme.typography.bodySmall)
                        }

                        Row {
                            IconButton(onClick = {
                                editingEntry = entry
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit_price))
                            }

                            IconButton(onClick = {
                                entryToDelete = entry
                                showDeleteDialog = true
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_price))
                            }

                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        PriceEntryDialog(
            initial = editingEntry,
            onDismiss = { showDialog = false },
            onSave = { newEntry ->
                if (editingEntry == null) {
                    viewModel.addPriceEntry(wineId, newEntry)
                } else {
                    viewModel.updatePriceEntry(wineId, editingEntry!!, newEntry)
                }
                showDialog = false
            },
        )
        var showDatePicker by remember { mutableStateOf(false) }
    }

    if (showDeleteDialog && entryToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_price_title)) },
            text = { Text(stringResource(R.string.delete_price_message)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deletePriceEntry(wineId, entryToDelete!!)
                    showDeleteDialog = false
                    entryToDelete = null
                }) {
                    Text(stringResource(R.string.delete_button_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    entryToDelete = null
                }) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceEntryDialog(
    initial: PriceEntryEntity?,
    onDismiss: () -> Unit,
    onSave: (PriceEntryEntity) -> Unit
) {
    var price by remember { mutableStateOf(initial?.price?.toString() ?: "") }
    var date by remember { mutableStateOf(initial?.date ?: LocalDate.now().toString()) }
    var source by remember { mutableStateOf(initial?.source ?: "") }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val defaultSource = stringResource(R.string.price_source_user_input)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.price_entry_title)) },
        text = {
            Column {

                // PRICE (numeric only)
                OutlinedTextField(
                    value = price,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            price = newValue
                        }
                    },
                    label = { Text(stringResource(R.string.price_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(Modifier.height(8.dp))

                // DATE PICKER BUTTON
                Button(onClick = { showDatePicker = true }) {
                    Text("${stringResource(R.string.date_label)}: $date")
                }

                Spacer(Modifier.height(8.dp))

                // SOURCE
                OutlinedTextField(
                    value = source,
                    onValueChange = { source = it },
                    label = { Text(stringResource(R.string.source_label)) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val parsed = price.toDoubleOrNull() ?: return@TextButton
                val finalSource = if (source.isBlank()) defaultSource else source
                onSave(PriceEntryEntity(parsed, date, finalSource))
            }) {
                Text(stringResource(R.string.save_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        }
    )

    // DATE PICKER DIALOG
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selected = datePickerState.selectedDateMillis
                    if (selected != null) {
                        date = Instant.ofEpochMilli(selected)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                            .toString()
                    }
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.save_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

