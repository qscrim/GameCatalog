package com.example.gamecatalog.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.presentation.components.GameScreenshots
import com.example.gamecatalog.presentation.components.SimilarGamesList
import com.example.gamecatalog.presentation.viewmodel.GameDetailViewModel
import com.example.gamecatalog.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    navController: NavController,
    gameId: Int,
    viewModel: GameDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(gameId) {
        viewModel.loadGameDetails(gameId)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(50))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад", tint = Color.White)
                }

                IconButton(
                    onClick = { scope.launch { viewModel.toggleFavorite() } },
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(50))
                ) {
                    Icon(
                        imageVector = if (uiState.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Избранное",
                        tint = if (uiState.isFavorite) Color(0xFFE91E63) else Color.White
                    )
                }
            }
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentPrimary)
                }
            }
            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Ошибка загрузки: ${uiState.error}", color = ErrorColor)
                }
            }
            uiState.game != null -> {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 2 }
                ) {
                    GameDetailsContent(
                        game = uiState.game!!,
                        similarGames = uiState.similarGames,
                        paddingValues = paddingValues,
                        onGameClick = { id ->
                            navController.navigate("detail/$id")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GameDetailsContent(
    game: Game,
    similarGames: List<Game>,
    paddingValues: PaddingValues,
    onGameClick: (Int) -> Unit
) {
    val requirements = game.platforms?.firstOrNull { it.requirements != null }?.requirements

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(bottom = 16.dp)
    ) {
        item {
            AsyncImage(
                model = game.backgroundImage,
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = game.name, style = MaterialTheme.typography.headlineLarge)

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (game.metacritic != null) {
                        ScoreChip(score = game.metacritic)
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    game.released?.let {
                        Text(text = it, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!game.genres.isNullOrEmpty()) {
                    Text(text = "Жанры:", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        game.genres.forEachIndexed { index, genreWrapper ->
                            val colors = listOf(AccentPrimary, AccentSecondary, Color(0xFFFFB74D), Color(0xFF90CAF9))
                            val color = colors[index % colors.size]

                            Box(
                                modifier = Modifier
                                    .background(color.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
                                    .border(1.dp, color, shape = RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(text = genreWrapper.name, style = MaterialTheme.typography.bodyMedium, color = color)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(text = "Платформы:", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    game.platforms?.forEach { platform ->
                        PlatformChip(name = platform.platform.name)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (requirements != null) {
                    Text(text = "Мин. системные требования", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkSurfaceVariant, shape = RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = requirements.minimum ?: "Не указаны",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                if (!game.screenshots.isNullOrEmpty()) {
                    GameScreenshots(screenshots = game.screenshots)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Блок похожих игр (Новое)
                if (similarGames.isNotEmpty()) {
                    SimilarGamesList(games = similarGames, onGameClick = onGameClick)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                Text(text = "Описание", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = game.description ?: "Описание отсутствует.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Где купить / Скачать", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    game.stores?.forEach { store ->
                        StoreLink(name = store.store.name, domain = store.store.domain)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreChip(score: Int) {
    val color = when {
        score >= 80 -> Color(0xFF4CAF50)
        score >= 60 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
            .border(1.dp, color, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = "$score", color = color, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun PlatformChip(name: String) {
    Box(
        modifier = Modifier
            .background(DarkSurfaceVariant, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = name, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
    }
}

@Composable
fun StoreLink(name: String, domain: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Открыть URL */ }
            .background(DarkSurfaceVariant, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Row {
            Icon(Icons.Default.Star, null, tint = AccentSecondary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = name, style = MaterialTheme.typography.bodyMedium)
                Text(text = domain, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
    }
}