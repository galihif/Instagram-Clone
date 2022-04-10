package com.giftech.instagramclone.core.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.giftech.instagramclone.core.data.model.User
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

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _isLogged = MutableLiveData<Boolean>()
    val isLogged:LiveData<Boolean> = _isLogged

    fun checkLogin():Boolean{
        return !localDataSource.getUser().username.equals("")
    }

    fun login(user: User){

    }

    fun getUser():User{
        return localDataSource.getUser()
    }

    fun register(user:User){
        localDataSource.saveUser(user)
        _isLogged.postValue(true)
    }

}