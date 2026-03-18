// WineListViewModel.kt
package com.mouton.openwinemer.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mouton.openwinemer.data.model.WineEntity
import com.mouton.openwinemer.data.repository.WineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * ViewModel qui gère :
 * - la liste complète des vins
 * - le tri
 * - les filtres
 * - la recherche
 * - la suppression multiple
 */
@HiltViewModel
class WineListViewModel @Inject constructor(
    private val repository: WineRepository
) : ViewModel() {

    /** Liste brute venant de la base */
    private val allWinesFlow = repository.getAllWinesFlow()

    /** États UI */
    private val searchQuery = MutableStateFlow("")
    val search: StateFlow<String> = searchQuery.asStateFlow()
    private val regionFilter = MutableStateFlow<String?>(null)
    private val colorFilter = MutableStateFlow<String?>(null)
    private val mode = MutableStateFlow("ALL")
    private val sortMode = MutableStateFlow(SortMode.NAME_ASC)

    /**
     * On regroupe tous les filtres dans un seul Flow.
     * Cela évite les combine(vararg) et les casts.
     */
    private val filterState: Flow<FilterState> =
        combine(
            searchQuery,
            regionFilter,
            colorFilter,
            mode,
            sortMode
        ) { search, region, color, modeValue, sort ->
            FilterState(
                search = search,
                region = region,
                color = color,
                mode = modeValue,
                sort = sort
            )
        }

    /**
     * Liste finale affichée à l’écran.
     * combine() ici ne prend que 2 flows → aucun cast, aucun warning.
     */
    val wines: StateFlow<List<WineEntity>> =
        combine(allWinesFlow, filterState) { all, filters ->
            applyFilters(all, filters)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /** Applique recherche + filtres + tri */
    private fun applyFilters(
        all: List<WineEntity>,
        f: FilterState
    ): List<WineEntity> {
        var list = all

        // 1) Mode (depuis l’écran Catégories)
        when (f.mode) {
            "REGION" -> list = list.sortedBy { it.region ?: "" }
            "COLOR" -> list = list.sortedBy { it.color ?: "" }
        }

        // 2) Recherche
        if (f.search.isNotBlank()) {
            list = list.filter {
                it.name?.contains(f.search, ignoreCase = true) == true ||
                        it.producer?.contains(f.search, ignoreCase = true) == true ||
                        it.region?.contains(f.search, ignoreCase = true) == true ||
                        it.color?.contains(f.search, ignoreCase = true) == true
            }
        }

        // 3) Filtres
        f.region?.let { r -> list = list.filter { it.region == r } }
        f.color?.let { c -> list = list.filter { it.color == c } }

        // 4) Tri
        return when (f.sort) {
            SortMode.NAME_ASC -> list.sortedBy { it.name ?: "" }
            SortMode.NAME_DESC -> list.sortedByDescending { it.name ?: "" }
            SortMode.REGION_ASC -> list.sortedBy { it.region ?: "" }
            SortMode.REGION_DESC -> list.sortedByDescending { it.region ?: "" }
            SortMode.COLOR_ASC -> list.sortedBy { it.color ?: "" }
            SortMode.COLOR_DESC -> list.sortedByDescending { it.color ?: "" }
            SortMode.YEAR_ASC -> list.sortedBy { it.vintage ?: 0 }
            SortMode.YEAR_DESC -> list.sortedByDescending { it.vintage ?: 0 }
        }
    }

    /** Fonctions publiques */
    fun setMode(value: String) { mode.value = value }
    fun setSearch(query: String) { searchQuery.value = query }
    fun setRegionFilter(region: String?) { regionFilter.value = region }
    fun setColorFilter(color: String?) { colorFilter.value = color }
    fun setSortMode(mode: SortMode) { sortMode.value = mode }

    suspend fun deleteWines(ids: Set<Long>) {
        repository.deleteWinesByIds(ids)
    }
}

/** État complet des filtres */
data class FilterState(
    val search: String,
    val region: String?,
    val color: String?,
    val mode: String,
    val sort: SortMode
)

/** Enum représentant les différents tris possibles */
enum class SortMode {
    NAME_ASC, NAME_DESC,
    REGION_ASC, REGION_DESC,
    COLOR_ASC, COLOR_DESC,
    YEAR_ASC, YEAR_DESC
}
