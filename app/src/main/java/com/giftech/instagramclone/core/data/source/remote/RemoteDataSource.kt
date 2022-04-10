package com.giftech.instagramclone.core.data.source.remote

import com.giftech.instagramclone.core.data.source.remote.network.ApiService

class RemoteDataSource private constructor(private val apiService: ApiService){

    companion object{
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(apiService: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(apiService).apply { instance = this }
            }
    }
}