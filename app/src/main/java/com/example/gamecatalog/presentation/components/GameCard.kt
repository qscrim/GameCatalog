package com.example.gamecatalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.ui.theme.*

@Composable
fun GameCard(
    game: Game,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
    ) {
        Box {
            // Заглушка фона (если картинка не загрузится)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkSurface)
            )

            // Загрузка обложки с обработкой ошибок
            SubcomposeAsyncImage(
                model = game.backgroundImage,
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    ShimmerEffect(
                        modifier = Modifier.fillMaxSize()
                    )
                },
                error = {
                    // Если ошибка — показываем просто темный фон
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF333333))
                    )
                }
            )

            // Бейдж Metacritic
            if (game.metacritic != null) {
                val color = when {
                    game.metacritic >= 75 -> Color(0xFF4CAF50)
                    game.metacritic >= 50 -> Color(0xFFFFC107)
                    else -> Color(0xFFF44336)
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(color.copy(alpha = 0.9f), shape = RoundedCornerShape(6.dp))
                        .border(1.dp, color, shape = RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${game.metacritic}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
            }

            // Градиент снизу
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                        )
                    )
            )

            // Текст и рейтинг
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (game.rating != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = String.format("%.1f", game.rating),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}