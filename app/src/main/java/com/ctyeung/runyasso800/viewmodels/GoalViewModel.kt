package com.ctyeung.runyasso800.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.data.preference.StoreRepository
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    val storeRepository: StoreRepository,
    val stepRepository: StepRepository,
    val splitRepository: SplitRepository
) : ViewModel() {

    private var goalData: GoalData = GoalData()
    private var _event = MutableLiveData<GoalEvent>()
    val event: LiveData<GoalEvent> = _event

    init {
        initDataStoreEvent()
    }

    private fun initDataStoreEvent() {
        kotlin.runCatching {
            viewModelScope.launch {
                storeRepository.apply {
                    goalFlow.collect() {
                        it.let {
                            goalData.deserialize(it)
                        }
                        _event.value = GoalEvent.Success(goalData)
                    }
                }
            }
        }.onFailure {
            Log.e("GoalViewModel failed", it.toString())
            _event.value = GoalEvent.Error("GoalViewModel failed")
        }
    }

    fun updateName(newName: String) {
        goalData.name = newName
    }

    fun updateGoal() {
        viewModelScope.launch {
            storeRepository.setString(StoreRepository.GOAL_KEY, goalData.serialize())
        }
        _event.value = GoalEvent.Success(goalData)
    }

    fun resetFactory() {
        goalData = GoalData()
        updateGoal()
        _event.value = GoalEvent.Success(goalData)
    }
}

sealed class GoalEvent() {
    class Success(val goalData: GoalData) : GoalEvent()
    object InProgress : GoalEvent()
    class Error(val msg: String) : GoalEvent()
}

data class GoalData(
    var name: String? = DEFAULT_NAME,
    var goalMarathonInSeconds: Long? = DEFAULT_MARATHON_GOAL,
    var goal800mCalculated: Long? = DEFAULT_800M_GOAL
) {
    companion object {
        const val DEFAULT_NAME = "Yasso"
        const val DEFAULT_MARATHON_GOAL = 10800L    // 3hrs
        const val DEFAULT_800M_GOAL = 180L          // 3minutes
    }

    fun serialize() = "${name},${goalMarathonInSeconds},${goal800mCalculated}"

    fun deserialize(str: String) {
        val list = str.split(',')
        if (list.size == 3) {
            name = list[0]
            goalMarathonInSeconds = list[1].toLong()
            goal800mCalculated = list[2].toLong()
        }
    }
}

