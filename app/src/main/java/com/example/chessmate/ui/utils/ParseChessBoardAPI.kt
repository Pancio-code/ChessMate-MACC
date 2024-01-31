package com.example.chessmate.ui.utils

import com.example.chessmate.BuildConfig
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ParseChessBoardAPI {
    @Multipart
    @POST("/api/v1/parse_chessboard")
    suspend fun parseChessBoard(@Header("Authorization") token: String, @Part image: MultipartBody.Part): retrofit2.Response<JsonObject>
}


object HelperClassParseChessBoard {
    fun getIstance() : ParseChessBoardAPI {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build().create(ParseChessBoardAPI::class.java)
    }
}