package com.giftech.instagramclone.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.giftech.instagramclone.core.data.MainRepository
import com.giftech.instagramclone.core.data.model.User

class LoginViewModel(private val mainRepository: MainRepository):ViewModel() {

    val error = mainRepository.error
    val loading = mainRepository.loading
    fun login(user: User):LiveData<Boolean> = mainRepository.login(user)

}