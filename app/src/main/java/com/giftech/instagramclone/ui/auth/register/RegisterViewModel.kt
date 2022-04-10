package com.giftech.instagramclone.ui.auth.register

import androidx.lifecycle.ViewModel
import com.giftech.instagramclone.core.data.MainRepository

class RegisterViewModel(private val mainRepository: MainRepository):ViewModel() {

    fun checkLogin():Boolean = mainRepository.checkLogin()

}