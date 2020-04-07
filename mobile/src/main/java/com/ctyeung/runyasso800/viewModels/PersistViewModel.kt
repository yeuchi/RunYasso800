package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.splits.SplitRepository
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.room.steps.StepRepository

class PersistViewModel (application: Application) : AndroidViewModel(application){
    var splitRepository: SplitRepository
    var splits: LiveData<List<Split>>

    var stepRepository: StepRepository
    var steps: LiveData<List<Step>>

    init {
        val splitDao = YassoDatabase.getDatabase(application, viewModelScope).splitDao()
        splitRepository = SplitRepository(splitDao)
        splits = splitRepository.splits

        val stepDao = YassoDatabase.getDatabase(application, viewModelScope).stepDao()
        stepRepository = StepRepository(stepDao)
        steps = stepRepository.steps
    }

    fun getDistanceSprinted() {

    }

    fun getDistanceJogged() {

    }

    fun getTotalElapsedTime() {

    }

    fun getTotalElapsedSprintTime() {

    }

    fun getTotalElapsedJogTime() {

    }
}