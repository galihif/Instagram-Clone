package com.giftech.instagramclone.core.data.source.local

import com.giftech.instagramclone.core.data.model.User
import com.giftech.instagramclone.core.data.source.local.preferences.Preferences

class LocalDataSource private constructor(private val preferences: Preferences){

    companion object {
        private var instance: LocalDataSource? = null

        fun getInstance(preferences: Preferences): LocalDataSource =
            instance ?: synchronized(this) {
                instance ?: LocalDataSource(preferences)
            }
    }

    fun saveUser(user:User) = preferences.saveUser(user)

    fun getUser():User = preferences.getUser()


}