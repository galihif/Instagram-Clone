package com.giftech.instagramclone.core.data.source.remote.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    var ROOT = "https://story-api.dicoding.dev/v1/"

    private val loggingInterceptor =
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ROOT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun getApiService():ApiService = retrofit.create(ApiService::class.java)

//    fun getApiService():ApiService{
//        val loggingInterceptor = HttpLoggingInterceptor()
//                .setLevel(HttpLoggingInterceptor.Level.BODY)
//        val client = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()
//        val retrofit by lazy{
//                Retrofit.Builder()
//                    .baseUrl(ROOT)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(client)
//                    .build()
//            }
//        return retrofit.create(ApiService::class.java)
//    }

}