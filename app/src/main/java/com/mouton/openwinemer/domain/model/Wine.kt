package com.mouton.openwinemer.domain.model

import com.mouton.openwinemer.data.model.WineEntity

// Modèle métier utilisé par l’UI
data class Wine(
    val id: Long,

    // Tous les champs sont optionnels (nullable) pour respecter l'utilisateur.
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
    val prices: MutableList<PriceEntry> = mutableListOf(),
        // List of price entries (price + date). Mutable so the user can add new price entries over time.
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
    subRegion = subRegion,               // Sous-région
    appellation = appellation,             // Appellation
    classification = classification,          // Classement

    mainGrape = mainGrape,               // Cépage principal
    blend = blend,                   // Assemblage (texte libre)
    grapePercentages = grapePercentages,        // Pourcentage de chaque cépage (texte libre)

    alcohol = alcohol,                 // Teneur en alcool
    residualSugar = residualSugar,           // Sucre résiduel
    acidity = acidity,                 // Acidité
    ph = ph,                      // pH
    volumeMl = volumeMl,                   // Volume en millilitres
    closureType = closureType,             // Type de bouchon
    servingTemp = servingTemp,             // Température de service

    vinificationMethod = vinificationMethod,      // Méthode de vinification
    fermentationType = fermentationType,        // Type de fermentation
    ageingDuration = ageingDuration,          // Durée d’élevage
    barrelType = barrelType,              // Type de fût
    barrelTime = barrelTime,              // Temps en barrique

    visualAspect = visualAspect,            // Aspect visuel
    aromas = aromas,                  // Arômes
    flavors = flavors,                 // Saveurs
    structure = structure,               // Structure
    finish = finish,                  // Finale
    globalRating = globalRating,             // Note globale

    recommendedDishes = recommendedDishes,       // Plats recommandés
    cuisineType = cuisineType,             // Type de cuisine
    occasions = occasions,               // Occasions

    ageingPotential = ageingPotential,         // Potentiel de garde
    optimalDrinkDate = optimalDrinkDate,        // Date optimale de consommation
    labelCondition = labelCondition,          // État étiquette
    awards = awards,                  // Récompenses
    reviews = reviews,                 // Critiques
    prices = prices,                   // Prix
    availability = availability,            // Disponibilité
    distributor = distributor,             // Distributeur
    sku = sku,                     // SKU
    barcode = barcode,                 // Code-barres
    stockQuantity = stockQuantity,              // Quantité en stock
    location = location,                // Emplacement
    purchaseDate = purchaseDate,            // Date d’achat
    purchasePrice = purchasePrice,           // Prix d’achat
    generalDescription = generalDescription       // Description générale
)
