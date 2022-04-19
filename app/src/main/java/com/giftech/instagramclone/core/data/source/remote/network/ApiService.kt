package com.giftech.instagramclone.core.data.source.remote.network

import com.giftech.instagramclone.core.data.source.remote.request.LoginRequest
import com.giftech.instagramclone.core.data.source.remote.request.RegisterRequest
import com.giftech.instagramclone.core.data.source.remote.response.GetAllPostResponse
import com.giftech.instagramclone.core.data.source.remote.response.LoginResponse
import com.giftech.instagramclone.core.data.source.remote.response.RegisterResponse
import com.giftech.instagramclone.core.data.source.remote.response.UploadPostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("register")
    suspend fun register(@Body body:RegisterRequest):RegisterResponse

    @POST("login")
    suspend fun login(@Body body:LoginRequest):LoginResponse


    @Multipart
    @POST("stories")
    suspend fun uploadPost(
        @Part file: MultipartBody.Part,
        @Part("description") description:RequestBody,
        @Header("Authorization") token: String,
    ): UploadPostResponse

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