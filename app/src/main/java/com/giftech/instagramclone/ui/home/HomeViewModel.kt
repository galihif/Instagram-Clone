package com.giftech.instagramclone.ui.home

import androidx.lifecycle.ViewModel
import com.giftech.instagramclone.core.data.MainRepository
import com.giftech.instagramclone.core.data.model.User

class HomeViewModel(private val mainRepository: MainRepository):ViewModel() {

    fun getUser():User = mainRepository.getUser()

}