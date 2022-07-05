package com.ctyeung.runyasso800

import android.app.Application
import android.content.Context
import com.ctyeung.runyasso800.MainActivity
import com.ctyeung.runyasso800.features.run.RunActivity
import java.lang.reflect.Type
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    init {
        instance = this
    }

    companion object {
        var lastSubActivity: Type = MainActivity::class.java
        private var instance: MainApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any
    }
}