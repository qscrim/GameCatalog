package com.example.gamecatalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.gamecatalog.data.model.Screenshot
import com.example.gamecatalog.ui.theme.*

@Composable
fun GameScreenshots(screenshots: List<Screenshot>) {
    if (screenshots.isEmpty()) return

    Column {
        Text(
            text = "Скриншоты",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(screenshots) { screenshot ->
                ScreenshotItem(imageUrl = screenshot.image)
            }
        }
    }
}

@Composable
fun ScreenshotItem(imageUrl: String) {
    Card(
        modifier = Modifier
            .width(240.dp)
            .height(140.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkSurface)
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = "Screenshot",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    ShimmerEffect(
                        modifier = Modifier.fillMaxSize()
                    )
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF333333))
                    )
                }
            )
        }
    }
}