package com.example.gamecatalog.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.data.repository.GameRepository
import com.example.gamecatalog.data.repository.NetworkResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

data class GameUiState(
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@OptIn(FlowPreview::class)
class GameViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = GameRepository(app)

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _searchQueryFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)

    init {
        // Сбор потока с debounce
        viewModelScope.launch {
            _searchQueryFlow
                .debounce(500)
                .collectLatest { query ->
                    performSearch(query)
                }
        }

        // Первичная загрузка (запускаем в отдельной корутине)
        viewModelScope.launch {
            performSearch("")
        }
    }

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launch {
            _searchQueryFlow.emit(query)
        }
    }

    fun searchGames(query: String = "") {
        viewModelScope.launch {
            performSearch(query)
        }
    }

    private suspend fun performSearch(query: String) {
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