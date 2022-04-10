package com.giftech.instagramclone.core.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.giftech.instagramclone.core.data.MainRepository
import com.giftech.instagramclone.core.di.Injection
import com.giftech.instagramclone.ui.auth.login.LoginViewModel
import com.giftech.instagramclone.ui.auth.register.RegisterViewModel
import com.giftech.instagramclone.ui.splash.SplashViewModel

class ViewModelFactory private constructor(private val mainRepository: MainRepository)
    : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(SplashViewModel::class.java)->{
                SplashViewModel(mainRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java)->{
                LoginViewModel(mainRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java)->{
                RegisterViewModel(mainRepository) as T
            }

            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context)).apply {
                    instance = this
                }
            }
        }
    }

}