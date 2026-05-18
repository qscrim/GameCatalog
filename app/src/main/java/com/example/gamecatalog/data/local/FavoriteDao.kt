package com.example.gamecatalog.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(game: FavoriteGame)

    @Delete
    suspend fun deleteFavorite(game: FavoriteGame)

    @Query("SELECT * FROM favorite_games ORDER BY name ASC")
    fun getAllFavorites(): Flow<List<FavoriteGame>>

    @Query("SELECT * FROM favorite_games WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchFavorites(query: String): Flow<List<FavoriteGame>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_games WHERE id = :gameId)")
    suspend fun isFavorite(gameId: Int): Boolean
}