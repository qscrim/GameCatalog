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
    @SerializedName("short_screenshots") val screenshots: List<Screenshot>?,
    @SerializedName("description_raw") val description: String?,
    val metacritic: Int?,
    val stores: List<StoreWrapper>?
)

data class PlatformWrapper(
    val platform: PlatformInfo,
    val requirements: Requirements? = null
)

data class PlatformInfo(val name: String)

data class Requirements(
    val minimum: String? = null,
    val recommended: String? = null
)

data class Screenshot(val image: String)

data class StoreWrapper(val store: StoreInfo)
data class StoreInfo(val name: String, val domain: String)