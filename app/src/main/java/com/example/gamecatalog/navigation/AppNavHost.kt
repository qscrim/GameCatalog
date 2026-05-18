package com.example.gamecatalog.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gamecatalog.presentation.screens.DetailScreen
import com.example.gamecatalog.presentation.screens.FavoritesScreen
import com.example.gamecatalog.presentation.screens.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onGameClick = { gameId ->
                    navController.navigate(Screen.Detail.createRoute(gameId))
                },
                navController = navController
            )
        }

        composable(Screen.Detail.route) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")?.toIntOrNull()
            if (gameId != null) {
                DetailScreen(navController = navController, gameId = gameId)
            }
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(navController = navController)
        }
    }
}