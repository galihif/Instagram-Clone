package com.giftech.instagramclone.core.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.giftech.instagramclone.core.data.model.Post
import com.giftech.instagramclone.core.data.model.User
import com.giftech.instagramclone.core.data.source.local.LocalDataSource
import com.giftech.instagramclone.core.data.source.remote.network.ApiService
import com.giftech.instagramclone.core.data.source.remote.request.LoginRequest
import com.giftech.instagramclone.core.data.source.remote.request.RegisterRequest
import com.giftech.instagramclone.core.utils.Mapper

class MainRepository private constructor(
    private val localDataSource: LocalDataSource,
    private val apiService: ApiService
){

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            localDataSource: LocalDataSource,
            apiService: ApiService
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(localDataSource, apiService).apply { instance = this }
            }
    }

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

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

    fun login(user:User):LiveData<Result<Boolean>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(LoginRequest(user.email,user.password))
            if(!response.error){
                emit(Result.Success(true))
                val res = response.loginResult
                val userRes = User(
                    id = res.userId,
                    username = res.name,
                    token = res.token
                )
                localDataSource.saveUser(userRes)
            } else {
                emit(Result.Error(response.message))
            }
        }catch (e: Exception) {
            Log.d("Repo", "error: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(user:User):LiveData<Result<Boolean>> = liveData {
        emit(Result.Loading)
        try {
            val request = RegisterRequest(user.username, user.email, user.password)
            val res = apiService.register(request)
            if(!res.error){
                emit(Result.Success(true))
            } else {
                emit(Result.Error(res.message))
            }
        }catch (e: Exception) {
            Log.d("Repo", "error: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }


//    fun uploadPost(photo: File, desc:String):LiveData<Boolean>{
//        _loading.postValue(true)
//        val success = MutableLiveData<Boolean>()
//        val token = Mapper.getBearerToken(localDataSource.getUser().token)
//        remoteDataSource.uploadPost(photo, desc, token,
//            object : RemoteDataSource.UploadPostCallback{
//                override fun onResponse(response: UploadPostResponse) {
//                    success.postValue(true)
//                    _loading.postValue(false)
//                }
//
//                override fun onError(error: String) {
//                    _loading.postValue(false)
//                }
//
//            })
//        return success
//    }

    fun getAllPost():LiveData<PagingData<Post>>{
        val token = Mapper.getBearerToken(localDataSource.getUser().token)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                PostPagingSource(apiService,token)
            }
        ).liveData
    }

//    fun getPostWithLocation():LiveData<Result<List<Post>>>{
//        val token = Mapper.getBearerToken(localDataSource.getUser().token)
//        return remoteDataSource.getPostWithLocation(token)
//    }


//    fun getPostWithLocation():LiveData<List<Post>>{
//        _loading.postValue(true)
//        val listPost = MutableLiveData<List<Post>>()
//        val token = Mapper.getBearerToken(localDataSource.getUser().token)
//        remoteDataSource.getPostWithLocation(token,
//        object : RemoteDataSource.GetPostWithLocationCallback{
//            override fun onResponse(response: List<StoryItem>) {
//                val listRes = Mapper.listStoryItemToListPost(response)
//                listPost.postValue(listRes)
//                _loading.postValue(false)
//            }
//
//            override fun onError(error: String) {
//                _loading.postValue(false)
//            }
//
//        })
//        return listPost
//    }

}