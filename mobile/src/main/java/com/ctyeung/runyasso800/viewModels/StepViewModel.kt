package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.room.YassoDatabase_Impl
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.room.steps.StepRepository
import kotlinx.coroutines.launch

class StepViewModel (application: Application) : AndroidViewModel(application)
{
    var repository:StepRepository
    var steps:LiveData<List<Step>>
    var totalDistance:Double = 0.0

    init {
        val stepDao = YassoDatabase.getDatabase(application, viewModelScope).stepDao()
        repository = StepRepository(stepDao)
        steps = repository.steps
    }

    fun insert(step: Step, dis:Double) = viewModelScope.launch {
        totalDistance = totalDistance + dis
        repository.insert(step)
    }

    fun clear() = viewModelScope.launch {
        totalDistance = 0.0
        repository.clear()
    }
}