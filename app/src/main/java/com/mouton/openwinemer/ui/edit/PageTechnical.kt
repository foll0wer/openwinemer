package com.mouton.openwinemer.ui.edit

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mouton.openwinemer.data.model.WineEntity
import androidx.compose.ui.res.stringResource
import com.mouton.openwinemer.R

@Composable
fun PageTechnical(
    wine: WineEntity,
    onWineChange: (WineEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = wine.alcohol?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(alcohol = it.toDoubleOrNull())) },
            label = { Text(stringResource(R.string.wine_alcohol)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.residualSugar?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(residualSugar = it.toDoubleOrNull())) },
            label = { Text(stringResource(R.string.wine_sugar)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.acidity?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(acidity = it.toDoubleOrNull())) },
            label = { Text(stringResource(R.string.wine_acidity)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.ph?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(ph = it.toDoubleOrNull())) },
            label = { Text(stringResource(R.string.wine_ph)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.volumeMl?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(volumeMl = it.toIntOrNull())) },
            label = { Text(stringResource(R.string.wine_vol)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.closureType ?: "",
            onValueChange = { onWineChange(wine.copy(closureType = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_cork)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.servingTemp ?: "",
            onValueChange = { onWineChange(wine.copy(servingTemp = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_service_temp)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
