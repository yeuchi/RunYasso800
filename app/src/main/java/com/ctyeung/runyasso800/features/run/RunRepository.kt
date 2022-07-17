package com.ctyeung.runyasso800.features.run

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.data.room.splits.Split
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.Step
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import com.ctyeung.runyasso800.features.run.stateMachine.RunStates
import com.ctyeung.runyasso800.storage.SharedPrefUtility
import com.ctyeung.runyasso800.storage.TimeFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt


class RunRepository @Inject constructor(
    private val splitRepository: SplitRepository,
    private val stepRepository: StepRepository
) : ViewModel() {

    /*
     * Steps - GPS data
     */

    fun getSteps() = stepRepository.steps
    fun getSplits() = splitRepository.splits

    fun insertStep(step: Step) = viewModelScope.launch {
        val dis = SharedPrefUtility.get(SharedPrefUtility.keySplitDistance, 0f)
        val splitDistance = dis + step.dis
        SharedPrefUtility.set(SharedPrefUtility.keySplitDistance, splitDistance.toFloat())
        stepRepository.insert(step)
    }

    fun reset() {
        SharedPrefUtility.set(SharedPrefUtility.keySplitDistance, 0f)
        SharedPrefUtility.set(SharedPrefUtility.keyStepIndex, 0)
    }

    fun clearStep() = viewModelScope.launch {
        stepRepository.deleteAll()
        SharedPrefUtility.set(SharedPrefUtility.keyStepIndex, 0)
        reset()
    }

    /*
     * Splits - mile distance abstraction
     */

    suspend fun getTotalDistanceRun(): Double {
        return splitRepository.getTotalDistanceRun()
    }

    fun getIndex(): Int {
        return SharedPrefUtility.get(SharedPrefUtility.keySplitIndex, 0)
    }

    fun getLastSplitElapsedTime(): Long {
        val size = splitRepository.splits.value?.size ?: 0
        if (size > 0) {
            var split = splitRepository.splits.value!![size - 1]
            val now = System.currentTimeMillis()
            return (now - split.startTime)
        }
        return 0
    }

    fun getTotalElapsedTime(): Long {
        val list: List<Split>? = splitRepository.splits.value
        val now = System.currentTimeMillis()
        if (null != list && list.size > 0) {
            return now - list[0].startTime
        }
        return 0
    }

    fun getLastSplitDistance(): Double {
        return SharedPrefUtility.get(SharedPrefUtility.keySplitDistance, 0f).toDouble()
    }

    fun insertSplit(split: Split?) = viewModelScope.launch {
        if (null != split) {
            splitRepository.insert(split)
        }
    }

    fun clearSplit() = viewModelScope.launch {
        splitRepository.deleteAll()
        SharedPrefUtility.set(SharedPrefUtility.keySplitDistance, 0f)
        SharedPrefUtility.set(SharedPrefUtility.keySplitIndex, 0)
    }

    fun updateSplit(split: Split?) = viewModelScope.launch {
        if (null != split) {
            splitRepository.update(split)
        }
    }
}