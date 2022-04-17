package com.giftech.instagramclone.core.data.source.remote

import android.util.Log
import com.giftech.instagramclone.core.data.model.User
import com.giftech.instagramclone.core.data.source.remote.network.ApiService
import com.giftech.instagramclone.core.data.source.remote.request.LoginRequest
import com.giftech.instagramclone.core.data.source.remote.request.RegisterRequest
import com.giftech.instagramclone.core.data.source.remote.response.*
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

    fun getAllPost(token:String, callback: GetPostCallback){
        apiService.getAllPost(token)
            .enqueue(object :retrofit2.Callback<GetAllPostResponse>{
                override fun onResponse(
                    call: Call<GetAllPostResponse>,
                    response: Response<GetAllPostResponse>
                ) {
                    if(response.isSuccessful){
                        callback.onResponse(response.body()?.listStory!!)
                    }
                }

                override fun onFailure(call: Call<GetAllPostResponse>, t: Throwable) {
                    Log.d("REMOTE", t.message.toString())
                }

            })
    }

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

    companion object{
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(apiService: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(apiService).apply { instance = this }
            }
    }
}