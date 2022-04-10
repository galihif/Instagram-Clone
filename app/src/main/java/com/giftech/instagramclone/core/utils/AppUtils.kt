package com.giftech.instagramclone.core.utils

import android.content.Context
import android.widget.Toast

object AppUtils {

//    fun ImageView.loadCircleImage(context: Context,imageSource : String?) {
//        Glide.with(context)
//            .load(imageSource)
//            .centerCrop()
//            .circleCrop()
//            .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
//            .into(this)
//    }

    fun showToast(context: Context, message:String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}