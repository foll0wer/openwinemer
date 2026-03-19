package com.mouton.openwinemer.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoriesScreen(
    onShowByRegion: () -> Unit,
    onShowByColor: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = onShowByRegion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.wines_by_region))
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onShowByColor,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.wines_by_color))
        }
    }
}
