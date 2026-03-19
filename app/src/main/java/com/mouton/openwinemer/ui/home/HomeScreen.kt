// HomeScreen.kt
@file:OptIn(ExperimentalMaterial3Api::class)

package com.mouton.openwinemer.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Écran d’accueil avec les options demandées.
@Composable
fun HomeScreen(
    onShowAll: () -> Unit,
    onShowByRegion: () -> Unit,
    onShowByColor: () -> Unit,
    onSettings: () -> Unit
) {
    // Scaffold fournit la structure de base Material (barre, FAB, etc.).
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        }
    ) { padding ->
        // Column organise les éléments verticalement.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
    }
}
