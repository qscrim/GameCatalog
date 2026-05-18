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

data class GameUiState(
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Наследуем AndroidViewModel для доступа к контексту
class GameViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = GameRepository(app)

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun searchGames(query: String = "") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.fetchGames(query.ifBlank { null })) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        games = result.data.results,
                        isLoading = false
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
}