// SettingsViewModel.kt
package com.mouton.openwinemer.ui.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mouton.openwinemer.domain.usecase.BackupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import android.content.Context
import com.mouton.openwinemer.R


/**
 * ViewModel de l'écran des paramètres.
 * Il sert d'intermédiaire entre l'UI et BackupUseCase.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val backupUseCase: BackupUseCase
    // private val repository: WineRepository
) : ViewModel() {

    private val _uiMessage = mutableStateOf<String?>(null)
    val uiMessage: State<String?> get() = _uiMessage

    private val _password = mutableStateOf("")
    val password: State<String> = _password
    fun setPassword(value: String) {
        _password.value = value
    }

    /** Export JSON chiffré */
    /* old json export function
    fun exportBackup(uri: Uri, password: String?) {
        viewModelScope.launch {
            backupUseCase.exportBackupToFolder(uri, password)
        }
    }
    */

    /** Ancien import JSON chiffré ou non
    fun importBackup(uri: Uri, password: String?) {
        viewModelScope.launch {
            try {
                backupUseCase.importBackupFromUri(uri, password)
                _uiMessage.value = "Import réussi"
            } catch (e: IllegalStateException) {
                _uiMessage.value = e.message ?: "Erreur lors de l'import"
            } catch (e: Exception) {
                _uiMessage.value = "Erreur inconnue lors de l'import"
            }
        }
    }
    */

    /** Export CSV */
    fun exportCsv(uri: Uri) {
        viewModelScope.launch {
            backupUseCase.exportCsvToFolder(uri)
        }
    }

    /** Export Excel (CSV compatible) */
    fun exportExcel(uri: Uri) {
        viewModelScope.launch {
            backupUseCase.exportExcelToFolder(uri)
        }
    }

    fun clearMessage() {
        _uiMessage.value = null
    }

    fun importJsonReplace(uri: Uri, context: Context, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                backupUseCase.importBackupFromUri(
                    uri = uri,
                    password = password.value.ifBlank { null },
                    replace = true
                )
                _uiMessage.value = context.getString(R.string.import_replace_success)

            } catch (e: Exception) {
                _uiMessage.value = context.getString(R.string.import_error, e.message ?: "Inconnue")
            }

            onDone()
        }
    }

    fun importJsonMerge(uri: Uri, context: Context, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                backupUseCase.importBackupFromUri(
                    uri = uri,
                    password = password.value.ifBlank { null },
                    replace = false
                )
                _uiMessage.value = context.getString(R.string.import_merge_success)

            } catch (e: Exception) {
                _uiMessage.value = context.getString(R.string.import_error, e.message ?: "Inconnue")
            }

            onDone()
        }
    }

    fun exportBackup(uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                backupUseCase.exportBackupToFolder(
                    treeUri = uri,
                    password = password.value.ifBlank { null }
                )
                _uiMessage.value = context.getString(R.string.export_success)

            } catch (e: Exception) {
                _uiMessage.value = context.getString(R.string.export_error, e.message ?: "Inconnue")
            }
        }
    }



}
