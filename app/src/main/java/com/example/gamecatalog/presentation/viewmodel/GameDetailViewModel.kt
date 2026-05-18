package com.example.gamecatalog.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.data.repository.GameRepository
import com.example.gamecatalog.data.repository.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GameDetailUiState(
    val game: Game? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false // Новое поле состояния
)

class GameDetailViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = GameRepository(app)

    private val _uiState = MutableStateFlow(GameDetailUiState())
    val uiState: StateFlow<GameDetailUiState> = _uiState.asStateFlow()

    fun loadGameDetails(gameId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.fetchGameDetails(gameId)) {
                is NetworkResult.Success -> {
                    val game = result.data
                    // Проверяем, есть ли игра в избранном сразу после загрузки
                    val favoriteStatus = repository.isFavorite(game.id)

                    _uiState.value = _uiState.value.copy(
                        game = game,
                        isLoading = false,
                        isFavorite = favoriteStatus
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    // Логика переключения избранного
    suspend fun toggleFavorite() {
        val currentGame = _uiState.value.game ?: return
        val isFav = _uiState.value.isFavorite

        if (isFav) {
            repository.removeFromFavorites(currentGame)
            _uiState.value = _uiState.value.copy(isFavorite = false)
        } else {
            repository.addToFavorites(currentGame)
            _uiState.value = _uiState.value.copy(isFavorite = true)
        }
    }
}