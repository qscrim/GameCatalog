package com.example.gamecatalog.presentation.screens


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.navigation.Screen
import com.example.gamecatalog.presentation.components.EmptyState
import com.example.gamecatalog.presentation.components.GameCard
import com.example.gamecatalog.presentation.components.ShimmerEffect
import com.example.gamecatalog.presentation.viewmodel.GameViewModel
import com.example.gamecatalog.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGameClick: (Int) -> Unit,
    navController: NavController? = null,
    viewModel: GameViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.searchGames()
    }

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.searchGames(searchQuery) },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
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

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(
                targetState = uiState,
                transitionSpec = {
                    fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                },
                label = "uiStateAnimation"
            ) { state ->
                when {
                    state.isLoading -> ShimmerGrid()
                    state.error != null -> {
                        EmptyState(
                            icon = Icons.Filled.Warning,
                            title = "Ошибка загрузки",
                            description = state.error ?: "Неизвестная ошибка",
                            actionText = "Попробовать снова",
                            onActionClick = { viewModel.searchGames(searchQuery) }
                        )
                    }
                    state.games.isEmpty() -> {
                        EmptyState(
                            icon = Icons.Filled.Search,
                            title = "Ничего не найдено",
                            description = if (searchQuery.isNotBlank())
                                "По запросу \"${searchQuery}\" игр не найдено"
                            else
                                "Игры не найдены. Попробуйте другой запрос",
                            actionText = "Сбросить поиск",
                            onActionClick = {
                                searchQuery = ""
                                viewModel.searchGames()
                            }
                        )
                    }
                    else -> GameGrid(games = state.games, onGameClick = onGameClick)
                }
            }
        }
    }
}

@Composable
fun ShimmerGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(6) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
            ) {
                Column {
                    ShimmerEffect(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.75f)
                    )
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