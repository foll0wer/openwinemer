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
fun PageRegion(
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
            value = wine.country ?: "",
            onValueChange = { onWineChange(wine.copy(country = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_country)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.region ?: "",
            onValueChange = { onWineChange(wine.copy(region = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_region)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.subRegion ?: "",
            onValueChange = { onWineChange(wine.copy(subRegion = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_sub_region)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.appellation ?: "",
            onValueChange = { onWineChange(wine.copy(appellation = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_appellation)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.classifications ?: "",
            onValueChange = { onWineChange(wine.copy(classifications = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_classifications)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

