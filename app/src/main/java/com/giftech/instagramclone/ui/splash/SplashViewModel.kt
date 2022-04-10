package com.giftech.instagramclone.ui.splash

import androidx.lifecycle.ViewModel
import com.giftech.instagramclone.core.data.MainRepository

class SplashViewModel(private val mainRepository: MainRepository):ViewModel() {

    fun checkLogin():Boolean = mainRepository.checkLogin()

}