package com.mouton.openwinemer.domain.model

/**
 * Represents a single price entry with its associated date.
 *
 * @param price The price value.
 * @param date The date when this price was recorded (ISO format: yyyy-MM-dd).
 */
data class PriceEntry(
    val price: Double,
    val date: String,
    val source: String //where price is found
)
