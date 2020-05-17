package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.utilities.LocationUpdateService

class MainViewModel (application: Application) : AndroidViewModel(application){
    fun getSprintDistance():Int {
        return SharedPrefUtility.get(SharedPrefUtility.keySprintDis, Split.DEFAULT_SPLIT_DISTANCE.toInt())
    }

    fun setSprintDistance(dis:Int) {
        SharedPrefUtility.set(SharedPrefUtility.keySprintDis, dis)
    }

    fun getJogDistance():Int {
        return SharedPrefUtility.get(SharedPrefUtility.keyJogDis, Split.DEFAULT_SPLIT_DISTANCE.toInt())
    }

    fun setJogDistance(dis:Int) {
        SharedPrefUtility.set(SharedPrefUtility.keyJogDis, dis)
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