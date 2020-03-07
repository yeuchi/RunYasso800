package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.room.steps.StepRepository
import com.ctyeung.runyasso800.utilities.TimeFormatter
import kotlinx.coroutines.launch

class StepViewModel (application: Application) : AndroidViewModel(application)
{
    var repository:StepRepository
    var steps:LiveData<List<Step>>
    var totalDistance:Double = 0.0
    var elapsedTime:Long = 0
    var disTotalString:String = "Step: 0m"
    var elapsedTimeString:String = "Time: 00:00"

    init {
        val stepDao = YassoDatabase.getDatabase(application, viewModelScope).stepDao()
        repository = StepRepository(stepDao)
        steps = repository.steps
    }

    fun insert(step: Step) = viewModelScope.launch {
        totalDistance += step.dis
        disTotalString = "Step: "+ totalDistance + "m";
        calculateTimeElapsed(step.time)
        elapsedTimeString = "Time: " + TimeFormatter.printTime(elapsedTime)
        repository.insert(step)
    }

    /*
     * calculate the total elapsed time since start
     * ** but what about 'PAUSE' ????
     */
    private fun calculateTimeElapsed(seconds:Long) {
        if(0==steps.value?.size)
            elapsedTime = 0

        else
            elapsedTime = seconds - steps.value!![0].time
    }

    fun clear() = viewModelScope.launch {
        totalDistance = 0.0
        repository.clear()
    }
}