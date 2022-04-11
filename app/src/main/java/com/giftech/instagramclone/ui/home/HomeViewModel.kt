package com.giftech.instagramclone.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.giftech.instagramclone.core.data.MainRepository
import com.giftech.instagramclone.core.data.model.Post
import com.giftech.instagramclone.core.data.model.User

class HomeViewModel(private val mainRepository: MainRepository):ViewModel() {

    val loading = mainRepository.loading

    fun getUser():User = mainRepository.getUser()

    fun logout() = mainRepository.logout()

    fun getAllPost():LiveData<List<Post>> = mainRepository.getAllPost()

}