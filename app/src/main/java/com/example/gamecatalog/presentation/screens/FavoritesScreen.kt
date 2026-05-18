package com.example.gamecatalog.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.gamecatalog.ui.theme.TextSecondary

@Composable
fun FavoritesScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Экран избранного (в разработке)",
            style = MaterialTheme.typography.titleLarge,
            color = TextSecondary
        )
    }
}