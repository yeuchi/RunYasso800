package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.utilities.LocationUpdateService

class MainViewModel (application: Application) : AndroidViewModel(application){
    fun getSprintDistance():Int {
        return SharedPrefUtility.get(SharedPrefUtility.keySprintLength, Split.DEFAULT_SPLIT_DISTANCE.toInt())
    }

    fun setSprintDistance(dis:Int) {
        SharedPrefUtility.set(SharedPrefUtility.keySprintLength, dis)
    }

    fun getJogDistance():Int {
        return SharedPrefUtility.get(SharedPrefUtility.keyJogLength, Split.DEFAULT_SPLIT_DISTANCE.toInt())
    }

    fun setJogDistance(dis:Int) {
        SharedPrefUtility.set(SharedPrefUtility.keyJogLength, dis)
    }

    fun setIterations(num:Int) {
        SharedPrefUtility.set(SharedPrefUtility.keyNumIterations, num)
    }

    fun getIterations():Int {
        return SharedPrefUtility.get(SharedPrefUtility.keyNumIterations, Split.DEFAULT_SPLIT_ITERATIONS)
    }

    fun setSampleRate(rate:Long) {
        SharedPrefUtility.set(SharedPrefUtility.keyGPSsampleRate, rate)
    }

    fun getSampleRate():Long {
        return SharedPrefUtility.get(SharedPrefUtility.keyGPSsampleRate, LocationUpdateService.DEFAULT_SAMPLE_RATE)
    }
}