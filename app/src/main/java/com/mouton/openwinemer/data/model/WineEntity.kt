// WineEntity.kt
package com.mouton.openwinemer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
// pour le partage
import kotlinx.serialization.Serializable

// @Entity indique à Room que cette classe représente une table dans la base de données.
@Serializable
@Entity(tableName = "wines")
data class WineEntity(
    // @PrimaryKey définit l'identifiant unique de chaque vin.
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    // Tous les champs sont optionnels (nullable) pour respecter la contrainte.
    val name: String? = null,                    // Nom du vin
    val producer: String? = null,                // Producteur
    val cuvee: String? = null,                   // Cuvée
    val vintage: Int? = null,                    // Millésime
    val wineType: String? = null,                // Type de vin
    val color: String? = null,                   // Couleur

    val country: String? = null,                 // Pays
    val region: String? = null,                  // Région viticole
    val subRegion: String? = null,               // Sous-région
    val appellation: String? = null,             // Appellation
    val classification: String? = null,          // Classement

    val mainGrape: String? = null,               // Cépage principal
    val blend: String? = null,                   // Assemblage (texte libre)
    val grapePercentages: String? = null,        // Pourcentage de chaque cépage (texte libre)

    val alcohol: Double? = null,                 // Teneur en alcool
    val residualSugar: Double? = null,           // Sucre résiduel
    val acidity: Double? = null,                 // Acidité
    val ph: Double? = null,                      // pH
    val volumeMl: Int? = null,                   // Volume en millilitres
    val closureType: String? = null,             // Type de bouchon
    val servingTemp: String? = null,             // Température de service

    val vinificationMethod: String? = null,      // Méthode de vinification
    val fermentationType: String? = null,        // Type de fermentation
    val ageingDuration: String? = null,          // Durée d’élevage
    val barrelType: String? = null,              // Type de fût
    val barrelTime: String? = null,              // Temps en barrique

    val visualAspect: String? = null,            // Aspect visuel
    val aromas: String? = null,                  // Arômes
    val flavors: String? = null,                 // Saveurs
    val structure: String? = null,               // Structure
    val finish: String? = null,                  // Finale
    val globalRating: Float? = null,             // Note globale

    val recommendedDishes: String? = null,       // Plats recommandés
    val cuisineType: String? = null,             // Type de cuisine
    val occasions: String? = null,               // Occasions

    val ageingPotential: String? = null,         // Potentiel de garde
    val optimalDrinkDate: String? = null,        // Date optimale de consommation
    val labelCondition: String? = null,          // État étiquette
    val awards: String? = null,                  // Récompenses
    val reviews: String? = null,                 // Critiques
    val price: List<Double> = emptyList(),      // Prix - try 'val price: emptyArray<Double>()? = null,'
    val availability: String? = null,            // Disponibilité
    val distributor: String? = null,             // Distributeur
    val sku: String? = null,                     // SKU
    val barcode: String? = null,                 // Code-barres
    val stockQuantity: Int? = null,              // Quantité en stock
    val location: String? = null,                // Emplacement
    val purchaseDate: String? = null,            // Date d’achat
    val purchasePrice: Double? = null,           // Prix d’achat
    val generalDescription: String? = null       // Description générale
)

/**
 * Converts a WineEntity (Room database model)
 * into a Wine (domain model used by the app).
 *
 * Domain models use MutableList for prices so the user
 * can add new price entries over time.
 */
fun WineEntity.toDomain(): Wine {
    return Wine(
        id = id,
        name = name,
        producer = producer,
        cuvee = cuvee,
        vintage = vintage,
        wineType = wineType,
        color = color,

        country = country,
        region = region,
        subRegion = subRegion,
        appellation = appellation,
        classification = classification,

        mainGrape = mainGrape,
        blend = blend,
        grapePercentages = grapePercentages,

        alcohol = alcohol,
        residualSugar = residualSugar,
        acidity = acidity,
        ph = ph,
        volumeMl = volumeMl,
        closureType = closureType,
        servingTemp = servingTemp,

        vinificationMethod = vinificationMethod,
        fermentationType = fermentationType,
        ageingDuration = ageingDuration,
        barrelType = barrelType,
        barrelTime = barrelTime,

        visualAspect = visualAspect,
        aromas = aromas,
        flavors = flavors,
        structure = structure,
        finish = finish,
        globalRating = globalRating,

        recommendedDishes = recommendedDishes,
        cuisineType = cuisineType,
        occasions = occasions,

        ageingPotential = ageingPotential,
        optimalDrinkDate = optimalDrinkDate,
        labelCondition = labelCondition,
        awards = awards,
        reviews = reviews,

        // Convert immutable Room list → mutable domain list
        price = price.toMutableList(),

        availability = availability,
        distributor = distributor,
        sku = sku,
        barcode = barcode,
        stockQuantity = stockQuantity,
        location = location,
        purchaseDate = purchaseDate,
        purchasePrice = purchasePrice,
        generalDescription = generalDescription
    )
}
