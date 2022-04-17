package com.giftech.instagramclone.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    var id:String = "",
    var username:String = "",
    var caption:String = "",
    var photo:String = "",
    var lat:Double? = null,
    var long:Double? = null,
):Parcelable
