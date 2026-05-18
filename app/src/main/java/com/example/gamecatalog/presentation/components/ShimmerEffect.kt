package com.example.gamecatalog.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier,
    visible: Boolean = true
) {
    if (!visible) return

    // Цвета для мерцания (от темного к светло-серому)
    val shimmerColors = listOf(
        Color(0xFF2A2A2A), // Базовый темный
        Color(0xFF3A3A3A), // Светлая полоса
        Color(0xFF2A2A2A)  // Снова темный
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "translate"
    )

    androidx.compose.foundation.layout.Box(
        modifier = modifier.background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(translateAnim.value, 0f),
                end = Offset(translateAnim.value + 500f, 1000f)
            )
        )
    )
}