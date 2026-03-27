package com.mouton.openwinemer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.mouton.openwinemer.ui.navigation.OpenWinemerNavGraph
import com.mouton.openwinemer.ui.theme.OpenWinemerTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.automirrored.filled.List

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenWinemerTheme {

                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = false,
                                onClick = { navController.navigate("home") },
                                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                                label = { Text(stringResource(R.string.home_button)) }
                            )
                            NavigationBarItem(
                                selected = false,
                                onClick = { navController.navigate("categories") },
                                icon = { Icon(Icons.AutoMirrored.Default.List, contentDescription = null) },
                                label = { Text(stringResource(R.string.navbar_categories)) }
                            )
                            NavigationBarItem(
                                selected = false,
                                onClick = { navController.navigate("settings") },
                                icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                                label = { Text(stringResource(R.string.navbar_settings)) }
                            )
                        }
                    }
                ) { padding ->
                    OpenWinemerNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}
