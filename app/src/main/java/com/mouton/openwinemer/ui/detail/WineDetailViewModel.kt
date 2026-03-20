// WineDetailViewModel.kt
package com.mouton.openwinemer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mouton.openwinemer.data.model.WineEntity
import com.mouton.openwinemer.data.repository.WineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel pour l'écran de détail d'un vin.
 * - charge un vin par id
 * - met à jour le stock
 * - supprime le vin
 */
@HiltViewModel
class WineDetailViewModel @Inject constructor(
    private val repository: WineRepository
) : ViewModel() {

    private val _wine = MutableStateFlow<WineEntity?>(null)
    val wine: StateFlow<WineEntity?> = _wine

    /**
     * Charge un vin depuis le repository.
     */
    fun loadWine(id: Long) {
        viewModelScope.launch {
            _wine.value = repository.getWineById(id)
        }
    }

    /**
     * Modifie le stock de +1 ou -1.
     */
    fun updateStock(delta: Int) {
        val current = _wine.value ?: return
        val newStock = (current.stockQuantity ?: 0) + delta
        if (newStock < 0) return

        val updated = current.copy(stockQuantity = newStock)

        viewModelScope.launch {
            repository.updateWine(updated)
            _wine.value = updated
        }
    }

    /**
     * Supprime le vin actuellement chargé.
     */
    fun deleteCurrentWine(onDeleted: () -> Unit) {
        val current = _wine.value ?: return
        viewModelScope.launch {
            repository.deleteWine(current)
            onDeleted()
        }
    }
    // export du vin
    fun exportCurrentWineAsJson(): String? {
        val wine = _wine.value ?: return null
        return repository.exportWineAsJson(wine)
    }
}
