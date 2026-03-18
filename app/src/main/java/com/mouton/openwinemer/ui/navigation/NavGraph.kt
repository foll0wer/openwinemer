package com.mouton.openwinemer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mouton.openwinemer.ui.list.WineListScreen
import com.mouton.openwinemer.ui.detail.WineDetailScreen
import com.mouton.openwinemer.ui.settings.SettingsScreen
import com.mouton.openwinemer.ui.home.CategoriesScreen
import com.mouton.openwinemer.ui.edit.AddEditWineFlow
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel



object Routes {
    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val SETTINGS = "settings"
}

@Composable
fun OpenWinemerNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(Routes.HOME) {
            WineListScreen(
                mode = "ALL",
                onWineClick = { id -> navController.navigate("detail/$id") },
                onAddWine = { navController.navigate("add_edit?wineId=-1") }
            )
        }

        composable(Routes.CATEGORIES) {
            CategoriesScreen(
                onShowByRegion = { navController.navigate("list_region") },
                onShowByColor = { navController.navigate("list_color") }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }



        composable(
            route = "add_edit?wineId={wineId}",
            arguments = listOf(
                navArgument("wineId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val wineId = backStackEntry.arguments?.getLong("wineId")
            AddEditWineFlow(
                wineId = wineId,
                onFinished = { navController.popBackStack() }
            )
        }

        composable("list_region") {
            WineListScreen(
                mode = "REGION",
                onWineClick = { /* TODO */ },
                onAddWine = { navController.navigate("add_edit?wineId=-1") }
            )
        }

        composable("list_color") {
            WineListScreen(
                mode = "COLOR",
                onWineClick = { /* TODO */ },
                onAddWine = { navController.navigate("add_edit?wineId=-1") }
            )
        }

        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getLong("id")
            WineDetailScreen(
                wineId = id,
                onBack = { navController.popBackStack() },
                onEdit = { wineId ->
                    navController.navigate("add_edit?wineId=$wineId")
                },
                onDeleted = {
                    // On revient à la liste après suppression
                    navController.popBackStack()
                }
            )
        }






    }
}
