package com.example.gamecatalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.ui.theme.*

@Composable
fun SimilarGamesList(
    games: List<Game>,
    onGameClick: (Int) -> Unit
) {
    if (games.isEmpty()) return

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Похожие игры",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(games) { game ->
                SimilarGameCard(game = game, onClick = { onGameClick(game.id) })
            }
        }
    }
}

@Composable
fun SimilarGameCard(game: Game, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = game.backgroundImage,
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(androidx.compose.ui.Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                        )
                    )
            )

            Text(
                text = game.name,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(androidx.compose.ui.Alignment.BottomStart)
                    .padding(8.dp)
            )
        }
    }
}