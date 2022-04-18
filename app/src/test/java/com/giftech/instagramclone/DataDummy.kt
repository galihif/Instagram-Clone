package com.giftech.instagramclone

import com.giftech.instagramclone.core.data.model.Post
import com.giftech.instagramclone.core.data.model.User

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

    fun generateDummyListPostLocation():List<Post>{
        val list = ArrayList<Post>()
        for (i in 0..10){
            list.add(
                Post(
                    id = i.toString(),
                    caption = "Foto ke $i",
                    lat = 0.0,
                    long = 0.0
                )
            )
        }
        return list
    }

    fun generateDummyUser():User{
        return User(
            id= "1",
            username = "galih",
            email = "email",
            password = "password",
            token = "sakdasdka"
        )
    }

    fun generateEmptyUser():User{
        return User(
            id= "1",
            username = "galih",
            email = "email",
            password = "password",
            token = "sakdasdka"
        )
    }

}