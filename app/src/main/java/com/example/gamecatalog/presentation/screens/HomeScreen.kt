package com.example.gamecatalog.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.presentation.viewmodel.GameViewModel
import com.example.gamecatalog.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    onGameClick: (Int) -> Unit,
    viewModel: GameViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    // Загружаем популярные игры при первом открытии
    LaunchedEffect(Unit) {
        viewModel.searchGames()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Поле поиска
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                viewModel.searchGames(query)
            },
            label = { Text("Поиск игр...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение состояния
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Ошибка: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                }
            }
            uiState.games.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Ничего не найдено", color = TextSecondary)
                }
            }
            else -> {
                GameGrid(games = uiState.games, onGameClick = onGameClick)
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onGameClick(game.id) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = game.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "ID: ${game.id}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }
        }
    }
}