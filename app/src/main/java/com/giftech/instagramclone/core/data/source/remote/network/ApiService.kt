package com.giftech.instagramclone.core.data.source.remote.network

import com.giftech.instagramclone.core.data.source.remote.request.LoginRequest
import com.giftech.instagramclone.core.data.source.remote.request.RegisterRequest
import com.giftech.instagramclone.core.data.source.remote.response.LoginResponse
import com.giftech.instagramclone.core.data.source.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("register")
    fun register(@Body body:RegisterRequest):Call<RegisterResponse>

    @POST("login")
    fun login(@Body body:LoginRequest):Call<LoginResponse>


}