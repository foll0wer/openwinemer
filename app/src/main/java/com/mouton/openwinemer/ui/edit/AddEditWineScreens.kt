// AddEditWineScreens.kt
@file:OptIn(ExperimentalMaterial3Api::class)

package com.mouton.openwinemer.ui.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mouton.openwinemer.data.model.WineEntity
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@Composable
fun AddEditWineFlow(
    wineId: Long?,
    onFinished: () -> Unit,
    viewModel: AddEditWineViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    LaunchedEffect(wineId) {
        viewModel.loadWine(wineId)
    }

    val state by viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (wineId == null || wineId <= 0)
                            "Ajouter un vin"
                        else
                            "Modifier un vin"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onFinished) {
                        Text("<")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (state.currentPage > 0) {
                    OutlinedButton(onClick = { viewModel.previousPage() }) {
                        Text("Précédent")
                    }
                }
                if (state.currentPage < 5) {
                    Button(onClick = { viewModel.nextPage() }) {
                        Text("Suivant")
                    }
                } else {
                    Button(onClick = { viewModel.save(onFinished) }) {
                        Text("Enregistrer")
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
        ) {
            when (state.currentPage) {

                0 -> PageIdentification(
                wine = state.wine,
                onWineChange = { viewModel.updateWine(it) },
                modifier = Modifier
            )

            1 -> PageRegion(
                wine = state.wine,
                onWineChange = { viewModel.updateWine(it) },
                modifier = Modifier
            )

            2 -> PageGrapes(
                wine = state.wine,
                onWineChange = { viewModel.updateWine(it) },
                modifier = Modifier
            )

            3 -> PageTechnical(
                wine = state.wine,
                onWineChange = { viewModel.updateWine(it) },
                modifier = Modifier
            )

            4 -> PageTasting(
                wine = state.wine,
                onWineChange = { viewModel.updateWine(it) },
                modifier = Modifier
            )

            5 -> PageCommercial(
                wine = state.wine,
                onWineChange = { viewModel.updateWine(it) },
                modifier = Modifier
            )
            }
        }
    }
}

// Exemple d’une page (les autres suivent le même principe).
@Composable
fun PageIdentification(
    wine: WineEntity,
    onWineChange: (WineEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = wine.name ?: "",
            onValueChange = { onWineChange(wine.copy(name = it.ifBlank { null })) },
            label = { Text("Nom du vin") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = wine.producer ?: "",
            onValueChange = { onWineChange(wine.copy(producer = it.ifBlank { null })) },
            label = { Text("Producteur") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = wine.cuvee ?: "",
            onValueChange = { onWineChange(wine.copy(cuvee = it.ifBlank { null })) },
            label = { Text("Cuvée") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = wine.vintage?.toString() ?: "",
            onValueChange = {
                val v = it.toIntOrNull()
                onWineChange(wine.copy(vintage = v))
            },
            label = { Text("Millésime") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = wine.wineType ?: "",
            onValueChange = { onWineChange(wine.copy(wineType = it.ifBlank { null })) },
            label = { Text("Type de vin") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = wine.color ?: "",
            onValueChange = { onWineChange(wine.copy(color = it.ifBlank { null })) },
            label = { Text("Couleur") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
