// SortOptions.kt
package com.mouton.openwinemer.util

// Objet combinant champ + ordre.
data class SortOption(
    val field: SortField = SortField.NAME,
    val order: SortOrder = SortOrder.ASCENDING
)
