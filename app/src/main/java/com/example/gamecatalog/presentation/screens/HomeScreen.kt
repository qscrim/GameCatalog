package com.example.gamecatalog.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.navigation.Screen
import com.example.gamecatalog.presentation.components.GameCard
import com.example.gamecatalog.presentation.components.ShimmerEffect
import com.example.gamecatalog.presentation.viewmodel.GameViewModel
import com.example.gamecatalog.ui.theme.*

@Composable
fun HomeScreen(
    onGameClick: (Int) -> Unit,
    navController: NavController? = null,
    viewModel: GameViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    // Загружаем игры при старте
    LaunchedEffect(Unit) {
        viewModel.searchGames()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Шапка с поиском
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    viewModel.searchGames(query)
                },
                label = { Text("Поиск игр...") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentPrimary,
                    unfocusedBorderColor = DarkSurfaceVariant,
                    focusedLabelColor = AccentPrimary,
                    unfocusedLabelColor = TextSecondary
                )
            )

            if (navController != null) {
                IconButton(onClick = { navController.navigate(Screen.Favorites.route) }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Избранное",
                        tint = AccentSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Логика отображения (Спиннер заменен на Shimmer сетку)
        when {
            uiState.isLoading -> {
                // Красивая сетка-заглушка вместо простого спиннера
                ShimmerGrid()
            }
            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Ошибка: ${uiState.error}", color = ErrorColor, style = MaterialTheme.typography.bodyLarge)
                }
            }
            uiState.games.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Ничего не найдено ", color = TextSecondary, style = MaterialTheme.typography.bodyLarge)
                }
            }
            else -> {
                GameGrid(games = uiState.games, onGameClick = onGameClick)
            }
        }
    }
}

// Новая функция: сетка из мерцающих карточек
@Composable
fun ShimmerGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Рисуем 6 фейковых карточек, пока грузится интернет
        items(6) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
            ) {
                Column {
                    // Картинка мерцает
                    ShimmerEffect(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.75f)
                    )
                    // Текст мерцает
                    Spacer(modifier = Modifier.height(8.dp))
                    ShimmerEffect(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .height(20.dp)
                            .fillMaxWidth(0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ShimmerEffect(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .height(12.dp)
                            .fillMaxWidth(0.4f)
                    )
                }
            }
        }
    }
}

@Composable
fun GameGrid(games: List<Game>, onGameClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(games) { game ->
            GameCard(
                game = game,
                onClick = { onGameClick(game.id) }
            )
        }
    }
}