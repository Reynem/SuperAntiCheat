package com.example.superanticheat

import android.app.Application

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AuthManager.init(this)
    }
}