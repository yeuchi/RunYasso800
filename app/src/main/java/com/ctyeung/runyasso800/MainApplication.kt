package com.ctyeung.runyasso800

import android.app.Application
import android.content.Context
import com.ctyeung.runyasso800.MainActivity
import java.lang.reflect.Type

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