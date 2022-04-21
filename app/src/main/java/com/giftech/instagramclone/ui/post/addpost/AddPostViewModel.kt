package com.giftech.instagramclone.ui.post.addpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.giftech.instagramclone.core.data.MainRepository
import java.io.File

class AddPostViewModel(private val mainRepository: MainRepository):ViewModel() {
    val loading = mainRepository.loading
    fun uploadPost(photo: File, desc:String,lat:Double,lon:Double):LiveData<Boolean> = mainRepository.uploadPost(photo, desc,lat,lon)
}