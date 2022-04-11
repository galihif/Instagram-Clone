package com.giftech.instagramclone.core.data.source.remote

import android.util.Log
import com.giftech.instagramclone.core.data.model.User
import com.giftech.instagramclone.core.data.source.remote.network.ApiService
import com.giftech.instagramclone.core.data.source.remote.request.LoginRequest
import com.giftech.instagramclone.core.data.source.remote.request.RegisterRequest
import com.giftech.instagramclone.core.data.source.remote.response.LoginResponse
import com.giftech.instagramclone.core.data.source.remote.response.LoginResult
import com.giftech.instagramclone.core.data.source.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.Response

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
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.d("REMOTE", t.message.toString())
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
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
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

    companion object{
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(apiService: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(apiService).apply { instance = this }
            }
    }
}