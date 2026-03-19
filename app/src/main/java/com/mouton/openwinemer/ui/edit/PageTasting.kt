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
fun PageTasting(
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
            value = wine.visualAspect ?: "",
            onValueChange = { onWineChange(wine.copy(visualAspect = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_visual_aspect)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.aromas ?: "",
            onValueChange = { onWineChange(wine.copy(aromas = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_aroma)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.flavors ?: "",
            onValueChange = { onWineChange(wine.copy(flavors = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_flavor)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.structure ?: "",
            onValueChange = { onWineChange(wine.copy(structure = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_structure)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.finish ?: "",
            onValueChange = { onWineChange(wine.copy(finish = it.ifBlank { null })) },
            label = { Text(stringResource(R.string.wine_final)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.globalRating?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(globalRating = it.toFloatOrNull())) },
            label = { Text(stringResource(R.string.wine_grade)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
