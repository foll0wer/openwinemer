package com.mouton.openwinemer.ui.edit

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mouton.openwinemer.data.model.WineEntity

@Composable
fun PageGrapes(
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
            value = wine.mainGrape ?: "",
            onValueChange = { onWineChange(wine.copy(mainGrape = it.ifBlank { null })) },
            label = { Text("Cépage principal") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.blend ?: "",
            onValueChange = { onWineChange(wine.copy(blend = it.ifBlank { null })) },
            label = { Text("Assemblage") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.grapePercentages ?: "",
            onValueChange = { onWineChange(wine.copy(grapePercentages = it.ifBlank { null })) },
            label = { Text("Pourcentage de chaque cépage") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


