package com.example.chessmate.ui.utils

import com.example.chessmate.BuildConfig
import com.example.chessmate.matches.Match
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface MatchesApi {
    @GET("/api/v1/matches/{id}")
    suspend fun get(@Header("Authorization") token: String, @Path("id") id: String): retrofit2.Response<List<Match>>

    @POST("/api/v1/matches")
    suspend fun create(@Header("Authorization") token: String, @Body body: String): retrofit2.Response<JsonObject>

}


object HelperClassMatches {
    fun getIstance() : MatchesApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MatchesApi::class.java)
    }
}