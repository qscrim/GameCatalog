package com.example.gamecatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.gamecatalog.ui.theme.GameCatalogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Убираем отступы под статус-бар
        setContent {
            GameCatalogTheme {
                // Пока заглушка — позже добавим навигацию
                androidx.compose.material3.Surface(
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    androidx.compose.material3.Text(
                        text = "🎮 GameCatalog",
                        style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}