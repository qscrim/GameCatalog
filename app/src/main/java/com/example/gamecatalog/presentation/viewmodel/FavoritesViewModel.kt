package com.example.gamecatalog.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamecatalog.data.local.FavoriteGame
import com.example.gamecatalog.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val favorites: List<FavoriteGame> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FavoritesViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = GameRepository(app)

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites(query: String = "") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            // Room возвращает Flow, который автоматически обновляется при изменении БД
            repository.getFavorites(query).collectLatest { list ->
                _uiState.value = _uiState.value.copy(
                    favorites = list,
                    isLoading = false
                )
            }
        }
    }

    fun removeFavorite(fav: FavoriteGame) {
        viewModelScope.launch {
            repository.removeFromFavorites(fav)
        }
    }
}