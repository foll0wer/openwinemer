package com.mouton.openwinemer.ui.favorites

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mouton.openwinemer.ui.list.WineListItemRow
import com.mouton.openwinemer.ui.list.WineListViewModel
import androidx.compose.foundation.layout.padding

@Composable
fun FavoritesScreen(
    onWineClick: (Long) -> Unit,
    viewModel: WineListViewModel = hiltViewModel()
) {
    val wines by viewModel.wines.collectAsState()

    val favorites = wines.filter { it.isFavorite }

    Scaffold { padding ->
        if (favorites.isEmpty()) {
            Text("No favorites yet", modifier = Modifier.padding(padding))
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(favorites) { wine ->
                    WineListItemRow(
                        wine = wine,
                        isSelected = false,
                        onClick = { onWineClick(wine.id) },
                        onLongClick = {},
                        onToggleFavorite = { viewModel.toggleFavorite(wine.id) }
                    )
                }
            }
        }
    }
}
