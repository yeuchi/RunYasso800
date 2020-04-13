package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.utilities.LocationUtils

class MainViewModel (application: Application) : AndroidViewModel(application){
    fun getSprintDistance():Int {
        return SharedPrefUtility.getDistance(SharedPrefUtility.keySprintDis)
    }

    fun setSprintDistance(dis:Int) {
        SharedPrefUtility.setDistance(SharedPrefUtility.keySprintDis, dis)
    }

    fun getJogDistance():Int {
        return SharedPrefUtility.getDistance(SharedPrefUtility.keyJogDis)
    }

    fun setJogDistance(dis:Int) {
        SharedPrefUtility.setDistance(SharedPrefUtility.keyJogDis, dis)
    }

    fun setIterations(num:Int) {
        SharedPrefUtility.setNumIterations(num)
    }

    fun getIterations():Int {
        return SharedPrefUtility.getNumIterations()
    }

    fun setSampleRate(rate:Long) {
        SharedPrefUtility.setGPSsampleRate(rate)
    }

    fun getSampleRate():Long {
        return SharedPrefUtility.getGPSsampleRate()
    }
}