package com.giftech.instagramclone.core.data

import com.giftech.instagramclone.core.data.source.local.LocalDataSource
import com.giftech.instagramclone.core.data.source.remote.RemoteDataSource

class MainRepository private constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
){

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            localDataSource: LocalDataSource,
            remoteDataSource: RemoteDataSource
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(localDataSource, remoteDataSource).apply { instance = this }
            }
    }

}