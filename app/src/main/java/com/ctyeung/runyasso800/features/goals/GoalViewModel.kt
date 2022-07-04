package com.ctyeung.runyasso800.features.goals

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class GoalViewModel (application: Application) : AndroidViewModel(application){
    var name:String = ""
    var sprintGoal:String = ""
    var raceGoal:String = ""
}
