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
import androidx.compose.ui.res.stringResource
import com.mouton.openwinemer.R
//for tutorial bubble
import androidx.compose.ui.platform.LocalContext
import com.mouton.openwinemer.ui.components.TutorialOverlay
import com.mouton.openwinemer.util.TutorialPrefs


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

    // --- Tutorial state for Add/Edit screen ---
    val context = LocalContext.current
    var showAddTutorial by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!TutorialPrefs.hasSeen(context, "tutorial_add_wine")) {
            showAddTutorial = true
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (wineId == null || wineId <= 0)
                            stringResource(R.string.add_wine)
                        else
                            stringResource(R.string.edit_wine)
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
                        Text(stringResource(R.string.previous))
                    }
                }
                if (state.currentPage < 5) {
                    Button(onClick = { viewModel.nextPage() }) {
                        Text(stringResource(R.string.next))
                    }
                } else {
                    Button(onClick = { viewModel.save(onFinished) }) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
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
            // --- Tutorial overlay ---
            if (showAddTutorial) {
                TutorialOverlay(
                    title = "Add a wine",
                    description = "Fill in the wine information. You can navigate pages using Next/Previous.",
                    onDismiss = {
                        showAddTutorial = false
                        TutorialPrefs.setSeen(context, "tutorial_add_wine")
                    }
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
            label = { Text(stringResource(R.string.wine_name)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = wine.producer ?: "",
            onValueChange = { onWineChange(wine.copy(producer = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_producer)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = wine.cuvee ?: "",
            onValueChange = { onWineChange(wine.copy(cuvee = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_batch)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = wine.vintage?.toString() ?: "",
            onValueChange = {
                val v = it.toIntOrNull()
                onWineChange(wine.copy(vintage = v))
            },
            label = { Text(stringResource(R.string.year_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = wine.wineType ?: "",
            onValueChange = { onWineChange(wine.copy(wineType = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_type)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = wine.color ?: "",
            onValueChange = { onWineChange(wine.copy(color = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.color_label)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
