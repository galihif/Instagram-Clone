package com.giftech.instagramclone.ui.post.selectphoto

import androidx.lifecycle.ViewModel
import com.giftech.instagramclone.core.data.MainRepository

class SelectPhotoViewModel(private val mainRepository: MainRepository):ViewModel() {

    fun checkLogin():Boolean = mainRepository.checkLogin()

}