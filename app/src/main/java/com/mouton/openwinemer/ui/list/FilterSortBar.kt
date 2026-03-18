// FilterSortBar.kt
package com.mouton.openwinemer.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mouton.openwinemer.util.FilterOptions
import com.mouton.openwinemer.util.SortField
import com.mouton.openwinemer.util.SortOption
import com.mouton.openwinemer.util.SortOrder

@Composable
fun FilterSortBar(
    currentSort: SortOption,
    currentFilters: FilterOptions,
    onSortChange: (SortOption) -> Unit,
    onFilterChange: (FilterOptions) -> Unit
) {
    // États locaux pour les champs de filtre (dans un vrai projet, tu peux les lier directement au ViewModel).
    var region by remember { mutableStateOf(currentFilters.region ?: "") }
    var color by remember { mutableStateOf(currentFilters.color ?: "") }
    var vintageText by remember { mutableStateOf(currentFilters.vintage?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Ligne de tri
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Menu déroulant pour choisir le champ de tri.
            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(onClick = { expanded = true }) {
                    Text("Trier par: ${currentSort.field.name}")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    SortField.values().forEach { field ->
                        DropdownMenuItem(
                            text = { Text(field.name) },
                            onClick = {
                                expanded = false
                                onSortChange(currentSort.copy(field = field))
                            }
                        )
                    }
                }
            }

            // Bouton pour inverser l’ordre.
            IconButton(onClick = {
                val newOrder = if (currentSort.order == SortOrder.ASCENDING)
                    SortOrder.DESCENDING else SortOrder.ASCENDING
                onSortChange(currentSort.copy(order = newOrder))
            }) {
                Text(if (currentSort.order == SortOrder.ASCENDING) "↑" else "↓")
            }
        }

        Spacer(Modifier.height(8.dp))

        // Filtres simples (région, couleur, millésime).
        OutlinedTextField(
            value = region,
            onValueChange = {
                region = it
                onFilterChange(
                    currentFilters.copy(region = it.ifBlank { null })
                )
            },
            label = { Text("Région") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(4.dp))

        OutlinedTextField(
            value = color,
            onValueChange = {
                color = it
                onFilterChange(
                    currentFilters.copy(color = it.ifBlank { null })
                )
            },
            label = { Text("Couleur") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(4.dp))

        OutlinedTextField(
            value = vintageText,
            onValueChange = {
                vintageText = it
                val vintage = it.toIntOrNull()
                onFilterChange(
                    currentFilters.copy(vintage = vintage)
                )
            },
            label = { Text("Millésime") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
