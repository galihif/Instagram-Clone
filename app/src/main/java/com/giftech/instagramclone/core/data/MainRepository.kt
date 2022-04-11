package com.giftech.instagramclone.core.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.giftech.instagramclone.core.data.model.User
import com.giftech.instagramclone.core.data.source.local.LocalDataSource
import com.giftech.instagramclone.core.data.source.remote.RemoteDataSource
import com.giftech.instagramclone.core.data.source.remote.response.LoginResult
import com.giftech.instagramclone.core.data.source.remote.response.RegisterResponse

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
        return localDataSource.getUser().username != ""
    }

    fun logout(){
        val user = User(
            username = "",
            email = "",
            password = "",
            token = ""
        )
        localDataSource.saveUser(user)
    }

    fun getUser():User{
        return localDataSource.getUser()
    }


    fun login(user: User):LiveData<Boolean>{
        _loading.postValue(true)
        val isSuccess = MutableLiveData<Boolean>()
        remoteDataSource.login(user, object : RemoteDataSource.LoginCallback{
            override fun onResponse(response: LoginResult) {
                isSuccess.postValue(true)
                _loading.postValue(false)
                val userRes = User(
                    id = response.userId,
                    username = response.name,
                    token = response.token
                )
                localDataSource.saveUser(userRes)
            }

            override fun onError(error: String) {
                isSuccess.postValue(false)
                _loading.postValue(false)
            }

        })
        return isSuccess
    }


    fun register(user:User):LiveData<Boolean>{
        _loading.postValue(true)
        val isSuccess = MutableLiveData<Boolean>()
        remoteDataSource.register(user, object : RemoteDataSource.RegisterCallback{
            override fun onResponse(response: RegisterResponse) {
                isSuccess.postValue(true)
                _loading.postValue(false)
            }

            override fun onError(error: String) {
                isSuccess.postValue(false)
                _loading.postValue(false)
            }

        })
        return isSuccess
    }

}