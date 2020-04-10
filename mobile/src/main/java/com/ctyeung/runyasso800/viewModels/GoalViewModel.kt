package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.utilities.TimeFormatter
import kotlinx.android.synthetic.main.activity_goal.*

class GoalViewModel (application: Application) : AndroidViewModel(application){

    var name:String = ""
    var sprintGoal:String = ""
    var raceGoal:String = ""

    fun setInitValues(){
        name = SharedPrefUtility.getName()
        val sprintInSeconds = SharedPrefUtility.getGoal(SharedPrefUtility.keySprintGoal)
        sprintGoal = TimeFormatter.printTime(sprintInSeconds)

        val raceInSeconds = SharedPrefUtility.getGoal(SharedPrefUtility.keyRaceGoal)
        raceGoal = TimeFormatter.printTime(raceInSeconds)
    }

    fun persistName(str:String) {
        SharedPrefUtility.setName(str)
        name = str
    }

    fun setSprintGoal(hourOfDay:Int, minute:Int) {
        val sprintInSeconds = TimeFormatter.convertHHmmss(0, hourOfDay, minute)
        SharedPrefUtility.setGoal(SharedPrefUtility.keySprintGoal, sprintInSeconds)
        sprintGoal = TimeFormatter.printTime(sprintInSeconds)
    }

    fun setRaceGoal(hourOfDay:Int, minute:Int) {
        val raceInSeconds = TimeFormatter.convertHHmmss(hourOfDay, minute, 0)
        SharedPrefUtility.setGoal(SharedPrefUtility.keyRaceGoal, raceInSeconds)
        raceGoal = TimeFormatter.printTime(raceInSeconds)
    }
}