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
import kotlin.math.roundToInt

class StepViewModel (application: Application) : AndroidViewModel(application)
{
    var repository:StepRepository
    var steps:LiveData<List<Step>>
    var startTime:Long = 0
    var index:Int = 0
    var totalDistance:Double = 0.0
    var elapsedTime:Long = 0

    init {
        val stepDao = YassoDatabase.getDatabase(application, viewModelScope).stepDao()
        repository = StepRepository(stepDao)
        steps = repository.steps
    }

    fun insert(step: Step) = viewModelScope.launch {
        totalDistance += step.dis
        elapsedTime = calculateTimeElapsed(step.time)
        repository.insert(step)
    }

    /*
     * calculate the total elapsed time since start
     * ** but what about 'PAUSE' ????
     */
    private fun calculateTimeElapsed(seconds:Long):Long {
        val size = steps.value?.size?:0
        if(null != steps.value && size > 0) {
            return seconds - startTime
        }
        else {
            startTime = seconds
            return 0
        }
      }

    fun clear() = viewModelScope.launch {
        totalDistance = 0.0
        repository.clear()
    }
}