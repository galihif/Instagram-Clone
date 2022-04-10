package com.giftech.instagramclone.core.data.source.local.preferences

import android.annotation.SuppressLint
import android.content.Context
import com.giftech.instagramclone.core.data.model.User

class Preferences private constructor(private val context: Context){
    companion object {
        private const val PREFS_NAME = "INSTA_PREF"

        private const val USERNAME = "USERNAME"
        private const val EMAIL = "EMAIL"
        private const val PASSWORD = "PASSWORD"
        private const val TOKEN = "TOKEN"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: Preferences? = null
        fun getInstance(mContext:Context): Preferences =
            instance ?: synchronized(this) {
                instance ?: Preferences(mContext.applicationContext).apply { instance = this }
            }
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveUser(user:User){
        preferences.edit().apply{
            putString(USERNAME, user.username)
            putString(EMAIL, user.email)
            putString(PASSWORD, user.password)
            putString(TOKEN, user.token)
        }.apply()
    }

    fun getUser():User{
        return User(
            username = preferences.getString(USERNAME,"")!!,
            email = preferences.getString(EMAIL,"")!!,
            password = preferences.getString(PASSWORD,"")!!,
            token = preferences.getString(TOKEN,"")!!,
        )
    }
}