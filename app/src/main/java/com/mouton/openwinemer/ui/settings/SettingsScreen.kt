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
import androidx.compose.ui.res.stringResource
import com.mouton.openwinemer.R
import androidx.compose.ui.platform.LocalContext
// import for tutorial bubble
import com.mouton.openwinemer.ui.components.TutorialOverlay
import com.mouton.openwinemer.util.TutorialPrefs
import androidx.compose.ui.platform.LocalContext


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
    /* var password by remember { mutableStateOf("") }
    // old password used now in setingsviewmodel
     */

    // Pour gérer le dialogue d'import JSON
    var showImportDialog by remember { mutableStateOf(false) }
    var pendingImportUri by remember { mutableStateOf<Uri?>(null) }

    // Snackbar pour afficher des messages en bas de l'écran.
    val snackbarHostState = remember { SnackbarHostState() }
    val message by viewModel.uiMessage

    val context = LocalContext.current

    // --- Tutorial state for Export section ---
    var showExportTutorial by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!TutorialPrefs.hasSeen(context, "tutorial_export")) {
            showExportTutorial = true
        }
    }

    // Sélecteur de dossier pour export JSON.
    val exportJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.exportBackup(uri, context)
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
            // On stocke l’URI et on ouvre le dialogue
            pendingImportUri = uri
            showImportDialog = true
            /* old import function
            viewModel.importBackup(uri, password.ifBlank { null })
            */
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
                title = { Text(stringResource(R.string.settings_screen)) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("<") }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    stringResource(R.string.cave_backup_page),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))

                // Champ pour saisir un mot de passe optionnel.
                OutlinedTextField(
                    value = viewModel.password.value,
                    onValueChange = { viewModel.setPassword(it) },
                    label = { Text(stringResource(R.string.password_prompt)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // Bouton pour exporter un backup JSON.
                Button(
                    onClick = { exportJsonLauncher.launch(null) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.json_export_button))
                }

                Spacer(Modifier.height(8.dp))

                // Bouton pour importer un backup JSON.
                Button(
                    onClick = { importJsonLauncher.launch("*/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.json_import_button))
                }

                Spacer(Modifier.height(24.dp))

                // Bouton pour exporter un CSV.
                Button(
                    onClick = { exportCsvLauncher.launch(null) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.csv_export_button))
                }

                Spacer(Modifier.height(8.dp))

                // Bouton pour exporter un "Excel" (CSV compatible).
                Button(
                    onClick = { exportExcelLauncher.launch(null) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.excel_export_button))
                }
            }
        }
        // --- Tutorial overlay ---
        if (showExportTutorial) {
            TutorialOverlay(
                title = "Export your cellar",
                description = "Use these buttons to export your cellar as JSON, CSV, or Excel.",
                onDismiss = {
                    showExportTutorial = false
                    TutorialPrefs.setSeen(context, "tutorial_export")
                }
            )
        }
}
    // Dialogue de choix pour l'import JSON
    if (showImportDialog && pendingImportUri != null) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text(stringResource(R.string.json_import_button)) },
            text = { Text(stringResource(R.string.import_choice)) },

            // Bouton : Remplacer
            confirmButton = {
                TextButton(onClick = {
                    showImportDialog = false
                    viewModel.importJsonReplace(
                        uri = pendingImportUri!!,
                        context = context
                    ) {
                        // Optionnel : snackbar
                    }
                }) {
                    Text(stringResource(R.string.replace_all))
                }
            },

            // Bouton : Fusionner
            dismissButton = {
                TextButton(onClick = {
                    showImportDialog = false
                    viewModel.importJsonMerge(
                        uri = pendingImportUri!!,
                        context = context
                    ) {
                        // Optionnel : snackbar
                    }
                }) {
                    Text(stringResource(R.string.merge_all))
                }
            }
        )
    }

    LaunchedEffect(message) {
        val msg = message
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }
}
