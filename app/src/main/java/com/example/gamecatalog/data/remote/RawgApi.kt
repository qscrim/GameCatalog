package com.example.gamecatalog.data.remote

import com.example.gamecatalog.BuildConfig
import com.example.gamecatalog.data.model.Game
import com.example.gamecatalog.data.model.GameListResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

interface RawgApi {
    @GET("games")
    suspend fun getGames(
        @Query("search") query: String? = null,
        @Query("page") page: Int = 1,
        @Query("key") apiKey: String = BuildConfig.RAWG_API_KEY
    ): GameListResponse

    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String = BuildConfig.RAWG_API_KEY
    ): Game

    @GET("games/{id}/additions")
    suspend fun getAdditions(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String = BuildConfig.RAWG_API_KEY
    ): GameListResponse
}

object ApiClient {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // === НАСТРОЙКИ ===
    // Впиши СВОЙ IP из ipconfig (начинается на 192.168...)
    private const val PROXY_HOST = "172.20.10.13" // <-- ЗАМЕНИ НА СВОЙ!
    private const val PROXY_PORT = 2080            // Порт Mixed/Socks из NekoBox
    // =================

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        // Используем SOCKS5, так как NekoBox (V2Ray) работает с ним стабильнее
        .proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress(PROXY_HOST, PROXY_PORT)))
        // Большие таймауты, чтобы не обрывало
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        // Переподключение при обрыве
        .retryOnConnectionFailure(true)
        // Держим соединение живым
        .connectionPool(
            okhttp3.ConnectionPool(
                maxIdleConnections = 5,
                keepAliveDuration = 5,
                TimeUnit.MINUTES
            )
        )
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.rawg.io/api/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: RawgApi = retrofit.create(RawgApi::class.java)
}