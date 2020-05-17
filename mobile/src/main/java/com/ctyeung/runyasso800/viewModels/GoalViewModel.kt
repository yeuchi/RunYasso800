package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.utilities.TimeFormatter
import kotlinx.android.synthetic.main.activity_goal.*

class GoalViewModel (application: Application) : AndroidViewModel(application){

    var name:String = ""
    var sprintGoal:String = ""
    var raceGoal:String = ""

    fun setInitValues(){
        name = SharedPrefUtility.get(SharedPrefUtility.keyName, MainApplication.applicationContext().resources.getString(R.string.run_yasso_800))
        val sprintInSeconds:Long = SharedPrefUtility.get(SharedPrefUtility.keySprintGoal, 0L)
        sprintGoal = TimeFormatter.printTime(sprintInSeconds)

        val raceInSeconds:Long = SharedPrefUtility.get(SharedPrefUtility.keyRaceGoal, 0L)
        raceGoal = TimeFormatter.printTime(raceInSeconds)
    }

    fun persistName(str:String) {
        SharedPrefUtility.set(SharedPrefUtility.keyName, str)
        name = str
    }

    fun setSprintGoal(hourOfDay:Int, minute:Int) {
        val sprintInSeconds = TimeFormatter.convertHHmmss(0, hourOfDay, minute)
        SharedPrefUtility.set(SharedPrefUtility.keySprintGoal, sprintInSeconds)
        sprintGoal = TimeFormatter.printTime(sprintInSeconds)
    }

    fun setRaceGoal(hourOfDay:Int, minute:Int) {
        val raceInSeconds = TimeFormatter.convertHHmmss(hourOfDay, minute, 0)
        SharedPrefUtility.set(SharedPrefUtility.keyRaceGoal, raceInSeconds)
        raceGoal = TimeFormatter.printTime(raceInSeconds)
    }
}