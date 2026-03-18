// SettingsScreen.kt
@file:OptIn(ExperimentalMaterial3Api::class)

package com.mouton.openwinemer.ui.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

/**
 * Écran des paramètres :
 * - Export JSON chiffré (avec date/heure dans le nom)
 * - Import JSON chiffré
 * - Export CSV (avec date/heure)
 * - Export "Excel" (CSV compatible, avec date/heure)
 */
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    // Mot de passe optionnel pour chiffrer/déchiffrer les backups.
    var password by remember { mutableStateOf("") }

    // Snackbar pour afficher des messages en bas de l'écran.
    val snackbarHostState = remember { SnackbarHostState() }
    val message by viewModel.uiMessage

    // Sélecteur de dossier pour export JSON.
    val exportJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.exportBackup(uri, password.ifBlank { null })
            /*scope.launch {
                snackbarHostState.showSnackbar("Backup JSON exporté avec succès.")
            }*/
        }
    }

    // Sélecteur de fichier pour import JSON.
    val importJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.importBackup(uri, password.ifBlank { null })
            /*scope.launch {
                snackbarHostState.showSnackbar("Backup importé avec succès.")
            }*/
        }
    }

    // Sélecteur de dossier pour export CSV.
    val exportCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.exportCsv(uri)
            /*scope.launch {
                snackbarHostState.showSnackbar("CSV exporté avec succès.")
            }*/
        }
    }

    // Sélecteur de dossier pour export "Excel" (CSV compatible).
    val exportExcelLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.exportExcel(uri)
            /*scope.launch {
                snackbarHostState.showSnackbar("Export Excel (CSV) réussi.")
            }*/
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paramètres") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("<") }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Backup de la cave", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            // Champ pour saisir un mot de passe optionnel.
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe (optionnel)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Bouton pour exporter un backup JSON.
            Button(
                onClick = { exportJsonLauncher.launch(null) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Exporter backup JSON")
            }

            Spacer(Modifier.height(8.dp))

            // Bouton pour importer un backup JSON.
            Button(
                onClick = { importJsonLauncher.launch("*/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Importer backup JSON")
            }

            Spacer(Modifier.height(24.dp))

            // Bouton pour exporter un CSV.
            Button(
                onClick = { exportCsvLauncher.launch(null) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Exporter CSV")
            }

            Spacer(Modifier.height(8.dp))

            // Bouton pour exporter un "Excel" (CSV compatible).
            Button(
                onClick = { exportExcelLauncher.launch(null) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Exporter Excel (CSV)")
            }
        }
    }
    LaunchedEffect(message) {
        val msg = message
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }
}
