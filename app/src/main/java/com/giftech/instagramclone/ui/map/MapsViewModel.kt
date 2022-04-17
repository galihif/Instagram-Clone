package com.giftech.instagramclone.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.giftech.instagramclone.core.data.MainRepository
import com.giftech.instagramclone.core.data.model.Post

class MapsViewModel(private val mainRepository: MainRepository):ViewModel() {

    val loading = mainRepository.loading

    fun getPostWithLocation():LiveData<List<Post>> = mainRepository.getPostWithLocation()

}