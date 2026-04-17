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
// Material You list items + icons
import androidx.compose.material3.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
// For clickable
import androidx.compose.foundation.clickable
// For opening URLs
import androidx.compose.ui.platform.LocalUriHandler
// For logs
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import com.mouton.openwinemer.util.LogReader
// For sending email
import android.content.Intent
// scroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


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

    // --- New UI state for modern settings screen ---
    var showDeleteDialog by remember { mutableStateOf(false) }
    // Second confirmation dialog (typing OPENWINEMER)
    var showFinalDeleteDialog by remember { mutableStateOf(false) }
    // Text typed by the user in the final confirmation dialog
    var deleteConfirmText by remember { mutableStateOf("") }
    var showLogsSheet by remember { mutableStateOf(false) }
    val logsSheetState = rememberModalBottomSheetState()
    val uriHandler = LocalUriHandler.current

    // --- Privacy policy dialog state ---
    var showPrivacyDialog by remember { mutableStateOf(false) }

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
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                // --- PASSWORD FIELD (used for JSON import/export encryption) ---
                OutlinedTextField(
                    value = viewModel.password.value,
                    onValueChange = { viewModel.setPassword(it) },
                    label = { Text(stringResource(R.string.password_prompt)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Spacer(Modifier.height(8.dp))

                // --- BACKUP JSON ---
                ListItem(
                    headlineContent = { Text(stringResource(R.string.json_export_button)) },
                    supportingContent = { Text(stringResource(R.string.settings_json_export_desc)) },
                    leadingContent = { Icon(Icons.Default.Backup, contentDescription = null) },
                    modifier = Modifier.clickable { exportJsonLauncher.launch(null) }
                )
                HorizontalDivider()

                // --- RESTORE JSON ---
                ListItem(
                    headlineContent = { Text(stringResource(R.string.json_import_button)) },
                    supportingContent = { Text(stringResource(R.string.settings_json_import_desc)) },
                    leadingContent = { Icon(Icons.Default.Restore, contentDescription = null) },
                    modifier = Modifier.clickable { importJsonLauncher.launch("*/*") }
                )
                HorizontalDivider()

                // --- CSV EXPORT ---
                ListItem(
                    headlineContent = { Text(stringResource(R.string.csv_export_button)) },
                    supportingContent = { Text(stringResource(R.string.settings_csv_export_title)) },
                    leadingContent = { Icon(Icons.Default.Backup, contentDescription = null) },
                    modifier = Modifier.clickable { exportCsvLauncher.launch(null) }
                )
                HorizontalDivider()

                // --- EXCEL EXPORT ---
                ListItem(
                    headlineContent = { Text(stringResource(R.string.excel_export_button)) },
                    supportingContent = { Text(stringResource(R.string.settings_excel_export_desc)) },
                    leadingContent = { Icon(Icons.Default.Backup, contentDescription = null) },
                    modifier = Modifier.clickable { exportExcelLauncher.launch(null) }
                )
                HorizontalDivider()

                // --- DELETE ALL DATA ---
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_delete_title)) },
                    supportingContent = { Text(stringResource(R.string.settings_delete_desc)) },
                    leadingContent = { Icon(Icons.Default.Delete, contentDescription = null) },
                    modifier = Modifier.clickable { showDeleteDialog = true }
                )
                HorizontalDivider()

                // --- PRIVACY POLICY ---
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_privacy_title)) },
                    supportingContent = { Text(stringResource(R.string.settings_privacy_desc)) },
                    leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                    modifier = Modifier.clickable {
                        // Open the privacy popup instead of a website
                        showPrivacyDialog = true
                    }
                )
                HorizontalDivider()

                // --- CREDITS ---
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_credits_title)) },
                    supportingContent = { Text(stringResource(R.string.settings_credits_desc)) },
                    leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                    modifier = Modifier.clickable {
                        uriHandler.openUri("https://github.com/foll0wer/openwinemer")
                    }
                )
                HorizontalDivider()

                // --- FEEDBACK ---
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_feedback_title)) },
                    supportingContent = { Text(stringResource(R.string.settings_feedback_desc)) },
                    leadingContent = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "message/rfc822"
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("151058944+foll0wer@users.noreply.github.com"))
                            putExtra(Intent.EXTRA_SUBJECT, "OpenWinemer Feedback")
                        }
                        context.startActivity(intent)
                    }
                )
                HorizontalDivider()

                // --- LOGS ---
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_logs_title)) },
                    supportingContent = { Text(stringResource(R.string.settings_logs_desc)) },
                    leadingContent = { Icon(Icons.Filled.List, contentDescription = null) },
                    modifier = Modifier.clickable { showLogsSheet = true }
                )
                HorizontalDivider()
            }
        }
        // --- Tutorial overlay ---
        if (showExportTutorial) {
            TutorialOverlay(
                title = stringResource(R.string.settings_screen_export_tutorial_title),
                description = stringResource(R.string.settings_screen_export_tutorial_desc),
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

    // --- FIRST DELETE DIALOG (simple yes/no) ---
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_all_title)) },
            text = { Text(stringResource(R.string.delete_all_message)) },
            // User clicks DELETE → opens second dialog
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    showFinalDeleteDialog = true   // <-- open second dialog
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // --- SECOND DELETE DIALOG (requires typing OPENWINEMER) ---
    if (showFinalDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showFinalDeleteDialog = false
                deleteConfirmText = ""
            },
            title = { Text(stringResource(R.string.final_delete_title)) },
            text = {
                Column {
                    Text(stringResource(R.string.final_delete_instruction))
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.final_delete_keyword),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(12.dp))
                    // User must type the confirmation word
                    OutlinedTextField(
                        value = deleteConfirmText,
                        onValueChange = { deleteConfirmText = it },
                        label = { Text(stringResource(R.string.final_delete_type_here)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            // DELETE button only enabled if typed correctly
            confirmButton = {
                TextButton(
                    onClick = {
                        showFinalDeleteDialog = false
                        deleteConfirmText = ""
                        viewModel.deleteAllData()   // <-- calls ViewModel function
                    },
                    enabled = deleteConfirmText == "OPENWINEMER"
                ) {
                    Text(stringResource(R.string.delete_button))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showFinalDeleteDialog = false
                        deleteConfirmText = ""
                    }
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }

    // --- Privacy policy popup dialog ---
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },

            // Title of the dialog
            title = { Text(stringResource(R.string.privacy_title)) },

            // Main message explaining offline behavior
            text = { Text(stringResource(R.string.privacy_message)) },

            // Single OK button
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text(stringResource(R.string.privacy_ok))
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

    // --- Logs bottom sheet ---
    if (showLogsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showLogsSheet = false },
            sheetState = logsSheetState
        ) {
            val logs = remember { LogReader.readLogs() }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.logs_title), style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))
                Text(logs, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(24.dp))
            }
        }
    }

}
