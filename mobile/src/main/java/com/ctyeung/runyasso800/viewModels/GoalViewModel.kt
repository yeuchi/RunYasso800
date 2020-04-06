package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class GoalViewModel (application: Application) : AndroidViewModel(application){

    fun getName():String {
        return SharedPrefUtility.getName()
    }

    fun setName(str:String) {
        SharedPrefUtility.setName(str)
    }

    fun getSprintGoal() {

    }

    fun setSprintGoal(sprintInSeconds:Long) {
        SharedPrefUtility.setGoal(SharedPrefUtility.keySprintGoal, sprintInSeconds)
    }

    fun getRaceGoal() {

    }

    fun setRaceGoal(raceInSeconds:Long) {
        SharedPrefUtility.setGoal(SharedPrefUtility.keyRaceGoal, raceInSeconds)
    }
}