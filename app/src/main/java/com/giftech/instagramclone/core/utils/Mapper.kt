package com.giftech.instagramclone.core.utils

import com.giftech.instagramclone.core.data.model.Post
import com.giftech.instagramclone.core.data.source.remote.response.StoryItem

object Mapper {

    fun getBearerToken(token:String):String{
        return "Bearer $token"
    }


    fun listStoryItemToListPost(listStory:List<StoryItem>):List<Post>{
        val listPost = listStory.map {
            Post(
                id = it.id,
                caption = it.description,
                username = it.name,
                photo = it.photoUrl
            )
        }
        return listPost
    }
}