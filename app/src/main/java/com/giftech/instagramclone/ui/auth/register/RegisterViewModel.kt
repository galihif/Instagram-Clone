package com.giftech.instagramclone.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.giftech.instagramclone.core.data.MainRepository
import com.giftech.instagramclone.core.data.model.User

class RegisterViewModel(private val mainRepository: MainRepository):ViewModel() {

    val loading = mainRepository.loading
    fun register(user:User):LiveData<Boolean> = mainRepository.register(user)

}