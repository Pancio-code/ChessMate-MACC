package com.example.chessmate.ui.utils

import com.example.chessmate.BuildConfig
import com.example.chessmate.sign_in.UserData
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


interface UserAPI {
    @GET("/api/v1/user/{id}")
    suspend fun get(@Header("Authorization") token: String, @Path("id") id: String): retrofit2.Response<UserData>

    @POST("/api/v1/user")
    suspend fun create(@Header("Authorization") token: String, @Body body: String): retrofit2.Response<JsonObject>

    @Multipart
    @PUT("/api/v1/user/{id}")
    suspend fun updateWithAvatar(@Header("Authorization") token: String, @Path("id") id: String, @Part("json_data") body: RequestBody, @Part image: MultipartBody.Part): retrofit2.Response<JsonObject>

    @PUT("/api/v1/user/{id}")
    suspend fun update(@Header("Authorization") token: String, @Path("id") id: String, @Body body: RequestBody): retrofit2.Response<JsonObject>

    @PUT("/api/v1/user/score/{id}")
    suspend fun resetScore(@Header("Authorization") token: String, @Path("id") id: String): retrofit2.Response<JsonObject>

    @DELETE("/api/v1/user/{id}")
    suspend fun delete(@Header("Authorization") token: String, @Path("id") id: String): retrofit2.Response<JsonObject>
}


object HelperClassUser {
    fun getIstance() : UserAPI {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build().create(UserAPI::class.java)
    }
}