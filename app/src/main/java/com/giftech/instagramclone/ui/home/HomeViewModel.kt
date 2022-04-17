package com.giftech.instagramclone.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.giftech.instagramclone.core.data.MainRepository
import com.giftech.instagramclone.core.data.model.Post
import com.giftech.instagramclone.core.data.model.User

class HomeViewModel(private val mainRepository: MainRepository):ViewModel() {

    val loading = mainRepository.loading

    fun getUser():User = mainRepository.getUser()

    fun logout() = mainRepository.logout()

    val post:LiveData<PagingData<Post>> = mainRepository.getAllPost().cachedIn(viewModelScope)

}