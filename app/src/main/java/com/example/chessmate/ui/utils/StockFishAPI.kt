package com.example.chessmate.ui.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface StockFishAPI {
    @GET("/{fen}")
    suspend fun get(@Path("fen") fen: String): retrofit2.Response<StockFishData>
}

object HelperClassStockFISH {
    private const val stockFishURL : String  = "https://stockfish.online/api/stockfish.php"
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