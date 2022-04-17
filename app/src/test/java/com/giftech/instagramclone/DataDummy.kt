package com.giftech.instagramclone

import com.giftech.instagramclone.core.data.model.Post

object DataDummy {

    fun generateDummyListPost():List<Post>{
        val list = ArrayList<Post>()
        for (i in 0..10){
            list.add(
                Post(
                    id = i.toString(),
                    caption = "Foto ke $i"
                )
            )
        }
        return list
    }

}