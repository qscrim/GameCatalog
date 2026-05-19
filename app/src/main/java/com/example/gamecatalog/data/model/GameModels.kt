package com.example.gamecatalog.data.model

import com.google.gson.annotations.SerializedName

data class GameListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Game>
)

data class Game(
    val id: Int,
    val name: String,
    @SerializedName("background_image") val backgroundImage: String?,
    val rating: Double?,
    val released: String?,
    val platforms: List<PlatformWrapper>?,
    val genres: List<GenreWrapper>?,
    @SerializedName("short_screenshots") val screenshots: List<Screenshot>?,
    @SerializedName("description_raw") val description: String?,
    val metacritic: Int?,
    val stores: List<StoreWrapper>?
) {
    // Переопределяем hashCode, чтобы избежать NPE при null полях
    override fun hashCode(): Int {
        return id.hashCode()
    }

    // Переопределяем equals для согласованности
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Game
        return id == other.id
    }
}

data class PlatformWrapper(
    val platform: PlatformInfo,
    val requirements: Requirements? = null
)

data class PlatformInfo(
    val name: String = "" // Дефолтное значение
)

data class GenreWrapper(
    val name: String = "" // Дефолтное значение
)

data class Requirements(
    val minimum: String? = null,
    val recommended: String? = null
)

data class Screenshot(
    val image: String
)

data class StoreWrapper(
    val store: StoreInfo
)

data class StoreInfo(
    val name: String = "",      // <-- НЕ nullable + дефолт
    val domain: String = ""     // <-- НЕ nullable + дефолт
) {
    // Безопасный hashCode
    override fun hashCode(): Int {
        return name.hashCode() xor domain.hashCode()
    }
}