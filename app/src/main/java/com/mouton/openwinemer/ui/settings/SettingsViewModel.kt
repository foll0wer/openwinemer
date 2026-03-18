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

/**
 * ViewModel de l'écran des paramètres.
 * Il sert d'intermédiaire entre l'UI et BackupUseCase.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val backupUseCase: BackupUseCase
) : ViewModel() {

    private val _uiMessage = mutableStateOf<String?>(null)
    val uiMessage: State<String?> get() = _uiMessage

    /** Export JSON chiffré */
    fun exportBackup(uri: Uri, password: String?) {
        viewModelScope.launch {
            backupUseCase.exportBackupToFolder(uri, password)
        }
    }

    /** Import JSON chiffré ou non */
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

}
