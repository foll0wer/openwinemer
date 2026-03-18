package com.mouton.openwinemer.ui.edit

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mouton.openwinemer.data.model.WineEntity

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
            label = { Text("Potentiel de garde") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.optimalDrinkDate ?: "",
            onValueChange = { onWineChange(wine.copy(optimalDrinkDate = it.ifBlank { null })) },
            label = { Text("Date optimale de consommation") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.labelCondition ?: "",
            onValueChange = { onWineChange(wine.copy(labelCondition = it.ifBlank { null })) },
            label = { Text("État de l’étiquette") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.awards ?: "",
            onValueChange = { onWineChange(wine.copy(awards = it.ifBlank { null })) },
            label = { Text("Récompenses") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.reviews ?: "",
            onValueChange = { onWineChange(wine.copy(reviews = it.ifBlank { null })) },
            label = { Text("Critiques") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.price?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(price = it.toDoubleOrNull())) },
            label = { Text("Prix (€)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.availability ?: "",
            onValueChange = { onWineChange(wine.copy(availability = it.ifBlank { null })) },
            label = { Text("Disponibilité") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.distributor ?: "",
            onValueChange = { onWineChange(wine.copy(distributor = it.ifBlank { null })) },
            label = { Text("Distributeur") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.sku ?: "",
            onValueChange = { onWineChange(wine.copy(sku = it.ifBlank { null })) },
            label = { Text("SKU") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.barcode ?: "",
            onValueChange = { onWineChange(wine.copy(barcode = it.ifBlank { null })) },
            label = { Text("Code-barres") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.stockQuantity?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(stockQuantity = it.toIntOrNull())) },
            label = { Text("Quantité en stock") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.location ?: "",
            onValueChange = { onWineChange(wine.copy(location = it.ifBlank { null })) },
            label = { Text("Emplacement") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.purchaseDate ?: "",
            onValueChange = { onWineChange(wine.copy(purchaseDate = it.ifBlank { null })) },
            label = { Text("Date d’achat") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.purchasePrice?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(purchasePrice = it.toDoubleOrNull())) },
            label = { Text("Prix d’achat (€)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.generalDescription ?: "",
            onValueChange = { onWineChange(wine.copy(generalDescription = it.ifBlank { null })) },
            label = { Text("Description générale") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
