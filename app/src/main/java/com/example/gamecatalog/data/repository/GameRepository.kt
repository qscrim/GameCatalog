package com.example.gamecatalog.data.repository

import android.content.Context
import com.example.gamecatalog.data.local.AppDatabase
import com.example.gamecatalog.data.local.FavoriteGame
import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.data.model.GameListResponse
import com.example.gamecatalog.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

class GameRepository(context: Context) {
    private val api = ApiClient.api
    private val db = AppDatabase.getDatabase(context)
    private val favoriteDao = db.favoriteDao()

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

    // Новый метод
    suspend fun fetchSimilarGames(gameId: Int): NetworkResult<GameListResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getAdditions(gameId)
                NetworkResult.Success(response)
            } catch (e: Exception) {
                NetworkResult.Error(e.localizedMessage ?: "Не удалось загрузить похожие игры")
            }
        }
    }

    fun getFavorites(query: String = ""): Flow<List<FavoriteGame>> {
        return if (query.isBlank()) {
            favoriteDao.getAllFavorites()
        } else {
            favoriteDao.searchFavorites(query)
        }
    }

    suspend fun isFavorite(gameId: Int): Boolean {
        return favoriteDao.isFavorite(gameId)
    }

    suspend fun addToFavorites(game: Game) {
        withContext(Dispatchers.IO) {
            val fav = FavoriteGame(
                id = game.id,
                name = game.name,
                backgroundImage = game.backgroundImage,
                rating = game.rating,
                released = game.released
            )
            favoriteDao.insertFavorite(fav)
        }
    }

    suspend fun removeFromFavorites(fav: FavoriteGame) {
        withContext(Dispatchers.IO) {
            favoriteDao.deleteFavorite(fav)
        }
    }
}