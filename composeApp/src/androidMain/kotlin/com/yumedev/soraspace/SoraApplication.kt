package com.yumedev.soraspace

import android.app.Application
import android.content.Context

class SoraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}