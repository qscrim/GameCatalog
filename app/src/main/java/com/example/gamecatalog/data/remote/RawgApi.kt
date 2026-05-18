package com.example.gamecatalog.data.remote

import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.data.model.GameListResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File
import java.io.FileInputStream
import java.util.Properties

interface RawgApi {
    @GET("games")
    suspend fun getGames(
        @Query("search") query: String? = null,
        @Query("page") page: Int = 1,
        @Query("key") apiKey: String = ApiConfig.API_KEY
    ): GameListResponse

    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String = ApiConfig.API_KEY
    ): Game
}

// Объект для чтения API ключа
object ApiConfig {
    val API_KEY: String by lazy { loadApiKey() }

    private fun loadApiKey(): String {
        return try {
            val properties = Properties()
            val rootDir = System.getProperty("user.dir")
            val localPropertiesFile = File(rootDir, "local.properties")

            if (localPropertiesFile.exists()) {
                FileInputStream(localPropertiesFile).use { input ->
                    properties.load(input)
                }
                properties.getProperty("rawg_api_key", "")
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}

object ApiClient {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.rawg.io/api/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: RawgApi = retrofit.create(RawgApi::class.java)
}