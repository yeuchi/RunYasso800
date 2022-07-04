package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ctyeung.runyasso800.utilities.SharedPrefUtility

class MainViewModel (application: Application) : AndroidViewModel(application){
    fun getSprintDistance():Int {
        return 1// SharedPrefUtility.get(SharedPrefUtility.keySprintLength, Split.DEFAULT_SPLIT_DISTANCE.toInt())
    }

    fun setSprintDistance(dis:Int) {
        SharedPrefUtility.set(SharedPrefUtility.keySprintLength, dis)
    }

    fun getJogDistance():Int {
        return 1// SharedPrefUtility.get(SharedPrefUtility.keyJogLength, Split.DEFAULT_SPLIT_DISTANCE.toInt())
    }

    fun setJogDistance(dis:Int) {
        SharedPrefUtility.set(SharedPrefUtility.keyJogLength, dis)
    }

    fun setIterations(num:Int) {
        SharedPrefUtility.set(SharedPrefUtility.keyNumIterations, num)
    }

    fun getIterations():Int {
        return 1//  SharedPrefUtility.get(SharedPrefUtility.keyNumIterations, Split.DEFAULT_SPLIT_ITERATIONS)
    }

    fun setSampleRate(rate:Long) {
        SharedPrefUtility.set(SharedPrefUtility.keyGPSsampleRate, rate)
    }

    fun getSampleRate():Long {
        return 1L// SharedPrefUtility.get(SharedPrefUtility.keyGPSsampleRate, LocationUpdateService.DEFAULT_SAMPLE_RATE)
    }
}