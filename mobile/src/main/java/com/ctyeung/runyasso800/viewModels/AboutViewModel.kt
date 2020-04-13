package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.utilities.LocationUtils

class AboutViewModel : AndroidViewModel{

    var sprintDistance:String
    var jogDistance:String
    var loops:String
    var sampleRate:String
    var version:String

    constructor(application: Application):super(application){
        sprintDistance = "${SharedPrefUtility.getDistance(SharedPrefUtility.keySprintDis)} m"
        jogDistance = "${SharedPrefUtility.getDistance(SharedPrefUtility.keyJogDis)} m"
        loops = "${SharedPrefUtility.getNumIterations().toString()} X"
        sampleRate = "${SharedPrefUtility.getGPSsampleRate().toString()} ms"
        val context = MainApplication.applicationContext()
        version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode.toString()
    }

    fun factoryReset() {
        SharedPrefUtility.setDistance(SharedPrefUtility.keySprintDis, Split.DEFAULT_SPLIT_DISTANCE.toInt())
        SharedPrefUtility.setDistance(SharedPrefUtility.keyJogDis, Split.DEFAULT_SPLIT_DISTANCE.toInt())
        SharedPrefUtility.setGPSsampleRate(LocationUtils.DEFAULT_SAMPLE_RATE)
        SharedPrefUtility.setNumIterations(Split.DEFAULT_SPLIT_ITERATIONS)
    }
}