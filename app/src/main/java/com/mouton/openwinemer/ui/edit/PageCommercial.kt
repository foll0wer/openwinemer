package com.mouton.openwinemer.ui.edit

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mouton.openwinemer.data.model.WineEntity
import androidx.compose.ui.res.stringResource
import com.mouton.openwinemer.R


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
        OutlinedTextField(
            value = wine.ageingPotential ?: "",
            onValueChange = { onWineChange(wine.copy(ageingPotential = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_aging_potential)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.optimalDrinkDate ?: "",
            onValueChange = { onWineChange(wine.copy(optimalDrinkDate = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_optimal_date)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.labelCondition ?: "",
            onValueChange = { onWineChange(wine.copy(labelCondition = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_label_state)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.awards ?: "",
            onValueChange = { onWineChange(wine.copy(awards = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_awards)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.reviews ?: "",
            onValueChange = { onWineChange(wine.copy(reviews = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_critics)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.price?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(price = it.toDoubleOrNull())) },
            label = { Text(stringResource(R.string.wine_price)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.availability ?: "",
            onValueChange = { onWineChange(wine.copy(availability = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_availability)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.distributor ?: "",
            onValueChange = { onWineChange(wine.copy(distributor = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_distributor)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.sku ?: "",
            onValueChange = { onWineChange(wine.copy(sku = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_sku)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.barcode ?: "",
            onValueChange = { onWineChange(wine.copy(barcode = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_barcode)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.stockQuantity?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(stockQuantity = it.toIntOrNull())) },
            label = { Text(stringResource(R.string.wine_stock)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.location ?: "",
            onValueChange = { onWineChange(wine.copy(location = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_storage_location)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.purchaseDate ?: "",
            onValueChange = { onWineChange(wine.copy(purchaseDate = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_buy_date)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.purchasePrice?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(purchasePrice = it.toDoubleOrNull())) },
            label = { Text(stringResource(R.string.wine_buy_price)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.generalDescription ?: "",
            onValueChange = { onWineChange(wine.copy(generalDescription = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_general_desc)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
