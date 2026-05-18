package com.example.gamecatalog.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                }
            )
        }
        // Заглушки для будущих экранов
        composable(Screen.Detail.route) { backStackEntry ->
            // TODO: DetailScreen(navController, gameId)
        }
        composable(Screen.Favorites.route) {
            // TODO: FavoritesScreen(navController)
        }
    }
}