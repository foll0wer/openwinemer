package com.mouton.openwinemer.ui.edit

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mouton.openwinemer.data.model.WineEntity

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
            label = { Text("Pays") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.region ?: "",
            onValueChange = { onWineChange(wine.copy(region = it.ifBlank { null })) },
            label = { Text("Région viticole") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.subRegion ?: "",
            onValueChange = { onWineChange(wine.copy(subRegion = it.ifBlank { null })) },
            label = { Text("Sous-région") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.appellation ?: "",
            onValueChange = { onWineChange(wine.copy(appellation = it.ifBlank { null })) },
            label = { Text("Appellation") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.classification ?: "",
            onValueChange = { onWineChange(wine.copy(classification = it.ifBlank { null })) },
            label = { Text("Classement (Grand Cru, Premier Cru…)") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

