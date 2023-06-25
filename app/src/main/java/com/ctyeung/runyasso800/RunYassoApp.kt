package com.ctyeung.runyasso800

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RunYassoApp:Application() {
    override fun onCreate() {
        super.onCreate()
    }

}