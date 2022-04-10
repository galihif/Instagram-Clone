package com.giftech.instagramclone.ui.auth.login

import androidx.lifecycle.ViewModel
import com.giftech.instagramclone.core.data.MainRepository

class LoginViewModel(private val mainRepository: MainRepository):ViewModel() {

    fun checkLogin():Boolean = mainRepository.checkLogin()

}