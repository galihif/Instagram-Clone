package com.giftech.instagramclone.core.data.source.remote.network

import com.giftech.instagramclone.core.data.source.remote.request.LoginRequest
import com.giftech.instagramclone.core.data.source.remote.request.RegisterRequest
import com.giftech.instagramclone.core.data.source.remote.response.GetAllPostResponse
import com.giftech.instagramclone.core.data.source.remote.response.LoginResponse
import com.giftech.instagramclone.core.data.source.remote.response.RegisterResponse
import com.giftech.instagramclone.core.data.source.remote.response.UploadPostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    fun register(@Body body:RegisterRequest):Call<RegisterResponse>

    @POST("login")
    fun login(@Body body:LoginRequest):Call<LoginResponse>


    @Multipart
    @POST("stories")
    fun uploadPost(
        @Part file: MultipartBody.Part,
        @Part("description") description:RequestBody,
        @Part("lat") lat:Double,
        @Part("lon") lon:Double,
        @Header("Authorization") token: String,
    ): Call<UploadPostResponse>

    @GET("stories")
    suspend fun getAllPost(
        @Query("page") page:Int,
        @Header("Authorization") token: String
    ):GetAllPostResponse

    @GET("stories?location=1")
    suspend fun getPostWithLocation(
        @Header("Authorization") token: String
    ):GetAllPostResponse

}