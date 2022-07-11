package com.ctyeung.runyasso800.features.run

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.data.room.YassoDatabase
import com.ctyeung.runyasso800.data.room.steps.Step
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import com.ctyeung.runyasso800.storage.SharedPrefUtility
import kotlinx.coroutines.launch

class StepViewModel (application: Application) : AndroidViewModel(application)
{
    var repository: StepRepository
    var steps: LiveData<List<Step>>

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
        repository.deleteAll()
        SharedPrefUtility.set(SharedPrefUtility.keyStepIndex, 0)
        reset()
    }
}