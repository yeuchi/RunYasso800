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
        sprintDistance = "${SharedPrefUtility.getDistance(SharedPrefUtility.keySprintDis)} m"
        jogDistance = "${SharedPrefUtility.getDistance(SharedPrefUtility.keyJogDis)} m"
        loops = "${SharedPrefUtility.getNumIterations().toString()} X"
        sampleRate = "${formatLargeNumber(SharedPrefUtility.getGPSsampleRate()).toString()} ms"
        val context = MainApplication.applicationContext()
        version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode.toString()
    }

    fun factoryReset() {
        SharedPrefUtility.setDistance(SharedPrefUtility.keySprintDis, Split.DEFAULT_SPLIT_DISTANCE.toInt())
        SharedPrefUtility.setDistance(SharedPrefUtility.keyJogDis, Split.DEFAULT_SPLIT_DISTANCE.toInt())
        SharedPrefUtility.setGPSsampleRate(LocationUpdateService.DEFAULT_SAMPLE_RATE)
        SharedPrefUtility.setNumIterations(Split.DEFAULT_SPLIT_ITERATIONS)
        SharedPrefUtility.setGoal(SharedPrefUtility.keyRaceGoal, 0)
        SharedPrefUtility.setGoal(SharedPrefUtility.keySprintGoal, 0)
        SharedPrefUtility.setName(MainApplication.applicationContext().resources.getString(R.string.run_yasso_800))
    }

    fun formatLargeNumber(num:Long):String {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(num)
    }
}