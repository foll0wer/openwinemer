package com.mouton.openwinemer.domain.model

import com.mouton.openwinemer.data.model.WineEntity

// Modèle métier utilisé par l’UI
data class Wine(
    val id: Long,
    val name: String?,
    val producer: String?,
    val cuvee: String?,
    val vintage: Int?,
    val wineType: String?,
    val color: String?,
    val country: String?,
    val region: String?,
    val stockQuantity: Int?
)

// Conversion Entity → Domain
fun WineEntity.toDomain(): Wine = Wine(
    id = id,
    name = name,
    producer = producer,
    cuvee = cuvee,
    vintage = vintage,
    wineType = wineType,
    color = color,
    country = country,
    region = region,
    stockQuantity = stockQuantity
)
