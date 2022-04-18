package com.giftech.instagramclone.core.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.giftech.instagramclone.core.data.PostPagingSource
import com.giftech.instagramclone.core.data.Result
import com.giftech.instagramclone.core.data.model.Post
import com.giftech.instagramclone.core.data.model.User
import com.giftech.instagramclone.core.data.source.remote.network.ApiService
import com.giftech.instagramclone.core.data.source.remote.request.LoginRequest
import com.giftech.instagramclone.core.data.source.remote.request.RegisterRequest
import com.giftech.instagramclone.core.data.source.remote.response.*
import com.giftech.instagramclone.core.utils.Mapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File

class RemoteDataSource private constructor(private val apiService: ApiService){

    fun register(user:User, callback: RegisterCallback){
        val body = RegisterRequest(user.username, user.email, user.password)
        apiService.register(body)
            .enqueue(object :retrofit2.Callback<RegisterResponse>{
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if(response.isSuccessful){
                        callback.onResponse(response.body()!!)
                    } else{
                        val error = JSONObject(response.errorBody()!!.string()).getString("message")
                        callback.onError(error)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.d("REMOTE", t.message.toString())
                    callback.onError(t.message.toString())
                }

            })
    }

    fun login(user:User, callback: LoginCallback){
        val body = LoginRequest(user.email, user.password)
        apiService.login(body)
            .enqueue(object :retrofit2.Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful){
                        callback.onResponse(response.body()?.loginResult!!)
                    } else{
                        val error = JSONObject(response.errorBody()!!.string()).getString("message")
                        callback.onError(error)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("REMOTE", t.message.toString())
                    callback.onError(t.message.toString())
                }

            })
    }

    fun uploadPost(photoFile: File, desc:String, token:String, callback: UploadPostCallback){
        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = photoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            photoFile.name,
            requestImageFile
        )
        apiService.uploadPost(imageMultipart,description,token)
            .enqueue(object :retrofit2.Callback<UploadPostResponse>{
                override fun onResponse(
                    call: Call<UploadPostResponse>,
                    response: Response<UploadPostResponse>
                ) {
                    if(response.isSuccessful){
                        callback.onResponse(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<UploadPostResponse>, t: Throwable) {
                    Log.d("REMOTE", t.message.toString())
                }

            })
    }

//    fun getAllPost(token:String, callback: GetPostCallback){
//        apiService.getAllPost(1,token)
//            .enqueue(object :retrofit2.Callback<GetAllPostResponse>{
//                override fun onResponse(
//                    call: Call<GetAllPostResponse>,
//                    response: Response<GetAllPostResponse>
//                ) {
//                    if(response.isSuccessful){
//                        callback.onResponse(response.body()?.listStory!!)
//                    }
//                }
//
//                override fun onFailure(call: Call<GetAllPostResponse>, t: Throwable) {
//                    Log.d("REMOTE", t.message.toString())
//                }
//
//            })
//    }
//
//    fun getAllPost(token:String):LiveData<Result<List<StoryItem>>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.getAllPost(1, token)
//            val listStory = response.listStory
//            emit(Result.Success(listStory))
//        } catch (e: Exception) {
//            Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
//            emit(Result.Error(e.message.toString()))
//        }
//    }


    fun getPostWithLocation(token:String):LiveData<Result<List<Post>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getPostWithLocation(token)
            val listStory = response.listStory
            val listPost = Mapper.listStoryItemToListPost(listStory)
            emit(Result.Success(listPost))
        }catch (e: Exception) {
            Log.d("RemoteData", "error: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllPost(token:String):LiveData<PagingData<Post>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                PostPagingSource(apiService,token)
            }
        ).liveData
    }

//    fun getPostWithLocation(token:String, callback: GetPostWithLocationCallback){
//        apiService.getPostWithLocation(token)
//            .enqueue(object :retrofit2.Callback<GetAllPostResponse>{
//                override fun onResponse(
//                    call: Call<GetAllPostResponse>,
//                    response: Response<GetAllPostResponse>
//                ) {
//                    if(response.isSuccessful){
//                        callback.onResponse(response.body()?.listStory!!)
//                    }
//                }
//
//                override fun onFailure(call: Call<GetAllPostResponse>, t: Throwable) {
//                    Log.d("REMOTE", t.message.toString())
//                }
//
//            })
//    }


    interface RegisterCallback{
        fun onResponse(response: RegisterResponse)
        fun onError(error:String)
    }

    interface LoginCallback{
        fun onResponse(response: LoginResult)
        fun onError(error:String)
    }

    interface UploadPostCallback{
        fun onResponse(response: UploadPostResponse)
        fun onError(error:String)
    }

    interface GetPostCallback{
        fun onResponse(response: List<StoryItem>)
        fun onError(error:String)
    }

    interface GetPostWithLocationCallback{
        fun onResponse(response: List<StoryItem>)
        fun onError(error:String)
    }

    companion object{
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(apiService: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(apiService).apply { instance = this }
            }
    }
}