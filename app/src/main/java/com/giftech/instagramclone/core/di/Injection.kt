package com.giftech.instagramclone.core.di

import android.content.Context
import com.giftech.instagramclone.core.data.MainRepository
import com.giftech.instagramclone.core.data.source.local.LocalDataSource
import com.giftech.instagramclone.core.data.source.local.preferences.Preferences
import com.giftech.instagramclone.core.data.source.remote.RemoteDataSource
import com.giftech.instagramclone.core.data.source.remote.network.ApiConfig

object Injection {

    fun provideRepository(context: Context):MainRepository{
        val localDataSource = provideLocalDataSource(context)
        val remoteDataSource = provideRemoteDataSource()

        return MainRepository.getInstance(localDataSource, remoteDataSource)
    }

    private fun provideRemoteDataSource(): RemoteDataSource {
        val apiService = ApiConfig.getApiService()
        return RemoteDataSource.getInstance(apiService)
    }

    private fun provideLocalDataSource(context: Context): LocalDataSource {
        val preferences = Preferences.getInstance(context)
        return LocalDataSource.getInstance(preferences)
    }

}