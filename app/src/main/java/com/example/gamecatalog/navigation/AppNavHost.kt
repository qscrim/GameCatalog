package com.example.gamecatalog.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gamecatalog.presentation.screens.DetailScreen
import com.example.gamecatalog.presentation.screens.FavoritesScreen
import com.example.gamecatalog.presentation.screens.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(onGameClick = { gameId ->
                navController.navigate(Screen.Detail.createRoute(gameId))
            })
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("gameId") { type = NavType.IntType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getInt("gameId") ?: return@composable
            DetailScreen(navController = navController, gameId = gameId)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen()
        }
    }
}