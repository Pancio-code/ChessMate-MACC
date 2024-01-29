package com.example.chessmate.ui.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface StockFishAPI {
    @GET("/api/stockfish.php")
    suspend fun get(
        @Query("fen") fen: String,
        @Query("depth") depth: Int,
        @Query("mode") mode: String
    ): retrofit2.Response<StockFishData>
}

object HelperClassStockFish {
    private const val stockFishURL : String  = "https://stockfish.online/"
    fun getIstance() : StockFishAPI {
        return Retrofit.Builder()
            .baseUrl(stockFishURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(StockFishAPI::class.java)
    }
}

data class StockFishData (
    val success : Boolean = true,
    val data : String? = null
)