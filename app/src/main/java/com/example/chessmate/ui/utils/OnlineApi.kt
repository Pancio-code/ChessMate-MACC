package com.example.chessmate.ui.utils

import com.example.chessmate.BuildConfig
import com.example.chessmate.multiplayer.RoomData
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface OnlineAPI {
    @GET("/api/online/{id}")
    suspend fun get(@Header("Authorization") token: String, @Path("id") userId: String): retrofit2.Response<RoomData>

    @POST("/api/v1/online/{id}")
    suspend fun create(@Header("Authorization") token: String, @Path("id") id: String, @Body body: String): retrofit2.Response<JsonObject>

    @PUT("/api/v1/online/{id}")
    suspend fun move(@Header("Authorization") token: String, @Path("id") id: String, @Body body: String): retrofit2.Response<JsonObject>

    @DELETE("/api/v1/online/{id}")
    suspend fun finish(@Header("Authorization") token: String, @Path("id") id: String): retrofit2.Response<JsonObject>
}


object HelperClassOnline {
    fun getIstance() : OnlineAPI {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(OnlineAPI::class.java)
    }
}