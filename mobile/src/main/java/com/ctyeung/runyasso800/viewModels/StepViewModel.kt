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

/*
 * To do:
 */
class StepViewModel (application: Application) : AndroidViewModel(application)
{
    var repository:StepRepository
    var steps:LiveData<List<Step>>

    init {
        val stepDao = YassoDatabase.getDatabase(application, viewModelScope).stepDao()
        repository = StepRepository(stepDao)
        steps = repository.steps
    }

    fun insert(step: Step) = viewModelScope.launch {
        val dis = SharedPrefUtility.get(SharedPrefUtility.keySplitDistance, 0f)
        val splitDistance = dis + step.dis
        SharedPrefUtility.set(SharedPrefUtility.keySplitDistance, splitDistance.toFloat())
        repository.insert(step)
    }

    fun reset() {
        SharedPrefUtility.set(SharedPrefUtility.keySplitDistance, 0f)
        SharedPrefUtility.set(SharedPrefUtility.keyStepIndex,0)
    }

    fun clear() = viewModelScope.launch {
        repository.clear()
        SharedPrefUtility.set(SharedPrefUtility.keyStepIndex, 0)
        reset()
    }
}