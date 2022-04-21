package com.giftech.instagramclone.core.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.giftech.instagramclone.core.data.model.Post
import com.giftech.instagramclone.core.data.model.User
import com.giftech.instagramclone.core.data.source.local.LocalDataSource
import com.giftech.instagramclone.core.data.source.remote.RemoteDataSource
import com.giftech.instagramclone.core.data.source.remote.response.LoginResult
import com.giftech.instagramclone.core.data.source.remote.response.RegisterResponse
import com.giftech.instagramclone.core.data.source.remote.response.UploadPostResponse
import com.giftech.instagramclone.core.utils.Mapper
import java.io.File

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
                _error.postValue(error)
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
                _error.postValue(error)
                isSuccess.postValue(false)
                _loading.postValue(false)
            }

        })
        return isSuccess
    }

    fun uploadPost(photo: File, desc:String,lat:Double,lon:Double):LiveData<Boolean>{
        _loading.postValue(true)
        val success = MutableLiveData<Boolean>()
        val token = Mapper.getBearerToken(localDataSource.getUser().token)
        remoteDataSource.uploadPost(photo, desc,lat, lon,token,
            object : RemoteDataSource.UploadPostCallback{
                override fun onResponse(response: UploadPostResponse) {
                    success.postValue(true)
                    _loading.postValue(false)
                }

                override fun onError(error: String) {
                    _loading.postValue(false)
                }

            })
        return success
    }
//    fun getAllPost():LiveData<List<Post>>{
//        _loading.postValue(true)
//        val listPost = MutableLiveData<List<Post>>()
//        val token = Mapper.getBearerToken(localDataSource.getUser().token)
//        remoteDataSource.getAllPost(token,
//        object : RemoteDataSource.GetPostCallback{
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
//
    fun getAllPost():LiveData<PagingData<Post>>{
        val token = Mapper.getBearerToken(localDataSource.getUser().token)
        return remoteDataSource.getAllPost(token)
    }

    fun getPostWithLocation():LiveData<Result<List<Post>>>{
        val token = Mapper.getBearerToken(localDataSource.getUser().token)
        return remoteDataSource.getPostWithLocation(token)
    }


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