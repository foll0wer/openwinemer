// AddEditWineViewModel.kt
package com.mouton.openwinemer.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mouton.openwinemer.data.model.WineEntity
import com.mouton.openwinemer.data.repository.WineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.ui.res.stringResource
import com.mouton.openwinemer.R


/**
 * ViewModel qui gère l’ajout et l’édition d’un vin.
 */
data class AddEditWineUiState(
    val currentPage: Int = 0,
    val wine: WineEntity = WineEntity()
)

@HiltViewModel
class AddEditWineViewModel @Inject constructor(
    private val repository: WineRepository
) : ViewModel() {

    var uiState = androidx.compose.runtime.mutableStateOf(AddEditWineUiState())
        private set

    /** Charge un vin existant pour l’édition. */
    fun loadWine(id: Long?) {
        if (id == null || id <= 0) return
        viewModelScope.launch {
            val existing = repository.getWineById(id)
            if (existing != null) {
                uiState.value = uiState.value.copy(wine = existing)
            }
        }
    }

    /** Met à jour l’objet vin dans l’état. */
    fun updateWine(wine: WineEntity) {
        uiState.value = uiState.value.copy(wine = wine)
    }

    fun nextPage() {
        uiState.value = uiState.value.copy(currentPage = uiState.value.currentPage + 1)
    }

    fun previousPage() {
        uiState.value = uiState.value.copy(currentPage = uiState.value.currentPage - 1)
    }

    /**
     * Sauvegarde le vin :
     * - si id = 0 → ajout
     * - sinon → mise à jour
     */
    fun save(onFinished: () -> Unit) {
        viewModelScope.launch {
            val wine = uiState.value.wine
            if (wine.id == 0L) {
                repository.addWine(wine)   // ← maintenant ça compile
            } else {
                repository.updateWine(wine)
            }
            onFinished()
        }
    }
}
