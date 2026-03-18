package com.mouton.openwinemer.ui.edit

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mouton.openwinemer.data.model.WineEntity

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
            label = { Text("Aspect visuel") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.aromas ?: "",
            onValueChange = { onWineChange(wine.copy(aromas = it.ifBlank { null })) },
            label = { Text("Arômes") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.flavors ?: "",
            onValueChange = { onWineChange(wine.copy(flavors = it.ifBlank { null })) },
            label = { Text("Saveurs") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.structure ?: "",
            onValueChange = { onWineChange(wine.copy(structure = it.ifBlank { null })) },
            label = { Text("Structure") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.finish ?: "",
            onValueChange = { onWineChange(wine.copy(finish = it.ifBlank { null })) },
            label = { Text("Finale") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = wine.globalRating?.toString() ?: "",
            onValueChange = { onWineChange(wine.copy(globalRating = it.toFloatOrNull())) },
            label = { Text("Note globale") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
