package com.example.gamecatalog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_games")
data class FavoriteGame(
    @PrimaryKey val id: Int,
    val name: String,
    val backgroundImage: String?,
    val rating: Double?,
    val released: String?
)