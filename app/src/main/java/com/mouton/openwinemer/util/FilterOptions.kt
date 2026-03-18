// FilterOptions.kt
package com.mouton.openwinemer.util

// Représente les filtres possibles. Tous sont optionnels.
data class FilterOptions(
    val region: String? = null,
    val color: String? = null,
    val vintage: Int? = null,
    val wineType: String? = null,
    val country: String? = null
    // Possible d'en ajouter d’autres (disponibilité, prix max, etc.)
)
