package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.utilities.LocationUpdateService
import java.text.NumberFormat
import java.util.*

class AboutViewModel : AndroidViewModel{

    var sprintDistance:String
    var jogDistance:String
    var loops:String
    var sampleRate:String
    var version:String

    constructor(application: Application):super(application){
        sprintDistance = "${SharedPrefUtility.get(SharedPrefUtility.keySprintLength, Split.DEFAULT_SPLIT_DISTANCE.toInt())} m"
        jogDistance = "${SharedPrefUtility.get(SharedPrefUtility.keyJogLength, Split.DEFAULT_SPLIT_DISTANCE.toInt())} m"
        loops = "${SharedPrefUtility.get(SharedPrefUtility.keyNumIterations, Split.DEFAULT_SPLIT_ITERATIONS).toString()} X"
        val rate = SharedPrefUtility.get(SharedPrefUtility.keyGPSsampleRate, LocationUpdateService.DEFAULT_SAMPLE_RATE)
        sampleRate = "${formatLargeNumber(rate).toString()} ms"
        val context = MainApplication.applicationContext()
        version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode.toString()
    }

    fun factoryReset() {
        SharedPrefUtility.set(SharedPrefUtility.keySprintLength, Split.DEFAULT_SPLIT_DISTANCE.toInt())
        SharedPrefUtility.set(SharedPrefUtility.keyJogLength, Split.DEFAULT_SPLIT_DISTANCE.toInt())
        SharedPrefUtility.set(SharedPrefUtility.keyGPSsampleRate, LocationUpdateService.DEFAULT_SAMPLE_RATE)
        SharedPrefUtility.set(SharedPrefUtility.keyNumIterations, Split.DEFAULT_SPLIT_ITERATIONS)
        SharedPrefUtility.set(SharedPrefUtility.keyRaceGoal, 0L)
        SharedPrefUtility.set(SharedPrefUtility.keySprintGoal, 0L)
        SharedPrefUtility.set(SharedPrefUtility.keyName, MainApplication.applicationContext().resources.getString(R.string.run_yasso_800))
    }

    fun formatLargeNumber(num:Long):String {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(num)
    }
}