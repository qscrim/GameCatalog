package com.example.gamecatalog.data.repository

import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.data.model.GameListResponse
import com.example.gamecatalog.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Универсальная обёртка для состояний сети
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

class GameRepository {
    private val api = ApiClient.api

    suspend fun fetchGames(query: String? = null, page: Int = 1): NetworkResult<GameListResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getGames(query, page)
                NetworkResult.Success(response)
            } catch (e: Exception) {
                NetworkResult.Error(e.localizedMessage ?: "Неизвестная ошибка сети")
            }
        }
    }

    suspend fun fetchGameDetails(gameId: Int): NetworkResult<Game> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getGameDetails(gameId)
                NetworkResult.Success(response)
            } catch (e: Exception) {
                NetworkResult.Error(e.localizedMessage ?: "Неизвестная ошибка сети")
            }
        }
    }
}