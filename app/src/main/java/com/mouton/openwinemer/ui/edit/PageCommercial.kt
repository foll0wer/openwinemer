package com.mouton.openwinemer.ui.edit

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.mouton.openwinemer.R
import com.mouton.openwinemer.data.model.WineEntity
import com.mouton.openwinemer.data.model.PriceEntryEntity

@Composable
fun PageCommercial(
    wine: WineEntity,
    onWineChange: (WineEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // --- Helper: get last price entry or empty fields ---
        val lastPrice = wine.prices.lastOrNull()
        val priceValue = lastPrice?.price?.toString() ?: ""
        val priceDate = lastPrice?.date ?: ""
        val priceSource = lastPrice?.source ?: ""

        val today = java.time.LocalDate.now().toString() // yyyy-MM-dd
        // --- READ STRINGS SAFELY INSIDE COMPOSABLE ---
        val userInputSourceLabel = stringResource(R.string.price_source_user_input)
        val priceSourceLabel = stringResource(R.string.wine_price_source)

        // --- AGEING POTENTIAL ---
        OutlinedTextField(
            value = wine.ageingPotential ?: "",
            onValueChange = { onWineChange(wine.copy(ageingPotential = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_aging_potential)) },
            placeholder = { Text(stringResource(R.string.placeholder_aging_potential)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- OPTIMAL DRINK DATE ---
        OutlinedTextField(
            value = wine.optimalDrinkDate ?: "",
            onValueChange = { onWineChange(wine.copy(optimalDrinkDate = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_optimal_date)) },
            placeholder = { Text(stringResource(R.string.placeholder_optimal_date)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- LABEL CONDITION ---
        OutlinedTextField(
            value = wine.labelCondition ?: "",
            onValueChange = { onWineChange(wine.copy(labelCondition = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_label_state)) },
            placeholder = { Text(stringResource(R.string.placeholder_label_state)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- AWARDS ---
        OutlinedTextField(
            value = wine.awards ?: "",
            onValueChange = { onWineChange(wine.copy(awards = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_awards)) },
            placeholder = { Text(stringResource(R.string.placeholder_awards)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- REVIEWS ---
        OutlinedTextField(
            value = wine.reviews ?: "",
            onValueChange = { onWineChange(wine.copy(reviews = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_critics)) },
            placeholder = { Text(stringResource(R.string.placeholder_critics)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // ============================
        // PRICE ENTRY (NEW MODEL)
        // ============================

        // --- PRICE VALUE ---
        OutlinedTextField(
            value = priceValue,
            onValueChange = { newValue ->
                val parsed = newValue.toDoubleOrNull()

                if (parsed == null) {
                    // User cleared the price → do NOT create a PriceEntryEntity
                    onWineChange(wine.copy(prices = wine.prices))
                    return@OutlinedTextField
                }

                // Build new entry with auto-filled defaults
                val newEntry = PriceEntryEntity(
                    price = parsed,
                    date = priceDate.ifBlank { today },
                    source = priceSource.ifBlank { userInputSourceLabel }
                )

                onWineChange(wine.copy(prices = wine.prices.dropLast(1) + newEntry))
            },
            label = { Text(stringResource(R.string.wine_price)) },
            placeholder = { Text(stringResource(R.string.placeholder_price)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

// --- PRICE DATE ---
        OutlinedTextField(
            value = priceDate,
            onValueChange = { newDate ->
                if (priceValue.isBlank()) return@OutlinedTextField // no price → ignore

                val newEntry = PriceEntryEntity(
                    price = lastPrice?.price ?: 0.0,
                    date = newDate,
                    source = priceSource.ifBlank { userInputSourceLabel }
                )

                onWineChange(wine.copy(prices = wine.prices.dropLast(1) + newEntry))
            },
            label = { Text(stringResource(R.string.wine_price_date)) },
            placeholder = { Text(stringResource(R.string.placeholder_price_date)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

// --- PRICE SOURCE ---
        OutlinedTextField(
            value = priceSource,
            onValueChange = { newSource ->
                if (priceValue.isBlank()) return@OutlinedTextField // no price → ignore

                val newEntry = PriceEntryEntity(
                    price = lastPrice?.price ?: 0.0,
                    date = priceDate.ifBlank { today },
                    source = newSource
                )

                onWineChange(wine.copy(prices = wine.prices.dropLast(1) + newEntry))
            },
            label = { Text(priceSourceLabel) },
            placeholder = { Text(stringResource(R.string.placeholder_price_source)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- AVAILABILITY ---
        OutlinedTextField(
            value = wine.availability ?: "",
            onValueChange = { onWineChange(wine.copy(availability = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_availability)) },
            placeholder = { Text(stringResource(R.string.placeholder_availability)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- DISTRIBUTOR ---
        OutlinedTextField(
            value = wine.distributor ?: "",
            onValueChange = { onWineChange(wine.copy(distributor = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_distributor)) },
            placeholder = { Text(stringResource(R.string.placeholder_distributor)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- SKU ---
        OutlinedTextField(
            value = wine.sku ?: "",
            onValueChange = { onWineChange(wine.copy(sku = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_sku)) },
            placeholder = { Text(stringResource(R.string.placeholder_sku)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- BARCODE ---
        OutlinedTextField(
            value = wine.barcode ?: "",
            onValueChange = { onWineChange(wine.copy(barcode = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_barcode)) },
            placeholder = { Text(stringResource(R.string.placeholder_barcode)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- STOCK ---
        OutlinedTextField(
            value = wine.stockQuantity?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(stockQuantity = it.toIntOrNull())) },
            label = { Text(stringResource(R.string.wine_stock)) },
            placeholder = { Text(stringResource(R.string.placeholder_stock)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- LOCATION ---
        OutlinedTextField(
            value = wine.location ?: "",
            onValueChange = { onWineChange(wine.copy(location = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_storage_location)) },
            placeholder = { Text(stringResource(R.string.placeholder_location)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- PURCHASE DATE ---
        OutlinedTextField(
            value = wine.purchaseDate ?: "",
            onValueChange = { onWineChange(wine.copy(purchaseDate = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_buy_date)) },
            placeholder = { Text(stringResource(R.string.placeholder_buy_date)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- PURCHASE PRICE ---
        OutlinedTextField(
            value = wine.purchasePrice?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(purchasePrice = it.toDoubleOrNull())) },
            label = { Text(stringResource(R.string.wine_buy_price)) },
            placeholder = { Text(stringResource(R.string.placeholder_buy_price)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- GENERAL DESCRIPTION ---
        OutlinedTextField(
            value = wine.generalDescription ?: "",
            onValueChange = { onWineChange(wine.copy(generalDescription = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_general_desc)) },
            placeholder = { Text(stringResource(R.string.placeholder_general_desc)) }, // NEW
            modifier = Modifier.fillMaxWidth()
        )
    }
}
