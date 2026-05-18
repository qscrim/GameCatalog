package com.example.gamecatalog.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.data.repository.GameRepository
import com.example.gamecatalog.data.repository.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Состояние UI для списка игр
data class GameUiState(
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class GameViewModel : ViewModel() {
    private val repository = GameRepository()

    // Приватный изменяемый поток состояния
    private val _uiState = MutableStateFlow(GameUiState())
    // Публичный поток только для чтения
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // Функция поиска/загрузки игр
    fun searchGames(query: String = "") {
        viewModelScope.launch {
            // Показываем загрузку
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Вызываем репозиторий
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
                is NetworkResult.Loading -> {} // Не используется в этом вызове
            }
        }
    }
}