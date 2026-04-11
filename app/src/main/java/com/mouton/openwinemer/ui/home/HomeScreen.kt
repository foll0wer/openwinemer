// HomeScreen.kt
@file:OptIn(ExperimentalMaterial3Api::class)

package com.mouton.openwinemer.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.mouton.openwinemer.R
// import for tutorial bubble
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.mouton.openwinemer.ui.components.TutorialOverlay
import com.mouton.openwinemer.util.TutorialPrefs




// Écran d’accueil avec les options demandées.
@Composable
fun HomeScreen(
    onShowAll: () -> Unit,
    onShowByRegion: () -> Unit,
    onShowByColor: () -> Unit,
    onSettings: () -> Unit
) {
    // --- Tutorial state for Home screen ---
    val context = LocalContext.current
    var showHomeTutorial by remember { mutableStateOf(false) }

    // Show tutorial only once
    LaunchedEffect(Unit) {
        if (!TutorialPrefs.hasSeen(context, "tutorial_home")) {
            showHomeTutorial = true
        }
    }
    // Scaffold fournit la structure de base Material (barre, FAB, etc.).
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // Column organise les éléments verticalement.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Chaque bouton représente une entrée du menu.
                Button(
                    onClick = onShowAll,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.show_wine_list))
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onShowByRegion,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.show_list_by_reg))
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onShowByColor,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.show_list_by_col))
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onSettings,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.navbar_settings))
                }
            }
            // --- Tutorial overlay ---
            if (showHomeTutorial) {
                TutorialOverlay(
                    title = "Welcome",
                    description = "Use these buttons to browse your cellar by list, region, or color.",
                    onDismiss = {
                        showHomeTutorial = false
                        TutorialPrefs.setSeen(context, "tutorial_home")
                    }
                )
            }
        }
    }
}
