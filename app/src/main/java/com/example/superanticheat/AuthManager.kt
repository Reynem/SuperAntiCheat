package com.example.superanticheat

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.Intent

@SuppressLint("StaticFieldLeak")
object AuthManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_USER_ID = "user_id"

    private var context: Context? = null

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    private val sharedPreferences: SharedPreferences
        get() = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            ?: throw IllegalStateException("AuthManager must be initialized first!")

    var accessToken: String?
        get() = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
        set(value) = sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, value).apply()

    var userId: Long
        get() = sharedPreferences.getLong(KEY_USER_ID, -1)
        set(value) = sharedPreferences.edit().putLong(KEY_USER_ID, value).apply()

    val isLoggedIn: Boolean
        get() = !accessToken.isNullOrEmpty()

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}

class App: Application(){
    override fun onCreate() {
        super.onCreate()
        checkAuthState()
    }

    private fun checkAuthState(){
        if (!AuthManager.isLoggedIn){
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
