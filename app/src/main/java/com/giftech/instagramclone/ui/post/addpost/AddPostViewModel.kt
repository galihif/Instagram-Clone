package com.giftech.instagramclone.ui.post.addpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.giftech.instagramclone.core.data.MainRepository
import java.io.File

class AddPostViewModel(private val mainRepository: MainRepository):ViewModel() {
    fun uploadPost(photo: File, desc:String):LiveData<Boolean> = mainRepository.uploadPost(photo, desc)
}