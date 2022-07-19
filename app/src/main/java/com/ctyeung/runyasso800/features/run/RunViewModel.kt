package com.ctyeung.runyasso800.features.run

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.data.room.YassoDatabase
import com.ctyeung.runyasso800.data.room.splits.Split
import com.ctyeung.runyasso800.data.room.steps.Step
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import com.ctyeung.runyasso800.features.run.stateMachine.RunStates
import com.ctyeung.runyasso800.storage.SharedPrefUtility
import com.ctyeung.runyasso800.storage.TimeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class RunViewModel @Inject constructor(
    private val repository: RunRepository
) : ViewModel() {

    var steps: LiveData<List<Step>> = repository.steps
    var splits: LiveData<List<Split>> = repository.splits

    /*
     * Steps - GPS data
     */
    fun insertStep(step: Step) {
        repository.insertStep(step)
    }

    fun reset() {
        repository.reset()
    }

    fun clearStep() = viewModelScope.launch {
        repository.clearStep()
    }

    /*
     * Split
     */

    fun insertSplit(split: Split?) {
        repository.insertSplit(split)
    }

    fun clearSplit() {
        repository.clearSplit()
    }

    fun updateSplit(split: Split?) {
        repository.updateSplit(split)
    }

    var txtLat: String = ""
    var txtLong: String = ""
    var txtStepDistance: String = "Step: 0m"
    var txtTotalDistance: String = "Total:800m"
    var txtSplitIndex: String = "Split: 1"
    var txtSplitTime: String = "Time: 00:00"
    var txtTotalTime: String = "Total: 00:00"
    var txtSplitType: String = "Sprint/Jog"
    var txtTotalSplits: String = "Total: 20"
    val defaultValue = "--"

    init {
        // 1 iteration = 2 splits (jog + sprint)
        val numSplits = SharedPrefUtility.get(
            SharedPrefUtility.keyNumIterations,
            Split.DEFAULT_SPLIT_ITERATIONS
        ) * 2
        txtTotalSplits =
            "${MainApplication.applicationContext().resources.getString(R.string.total)}: ${numSplits}"
        SharedPrefUtility.set(SharedPrefUtility.keyLastLatitutde, defaultValue)
        SharedPrefUtility.set(SharedPrefUtility.keyLastLongitude, defaultValue)
    }
    fun updateData(refresh: () -> Unit) {
        txtLat = SharedPrefUtility.get(SharedPrefUtility.keyLastLatitutde, defaultValue)
        txtLong = SharedPrefUtility.get(SharedPrefUtility.keyLastLongitude, defaultValue)

        // distance in current split
        txtStepDistance = "Dis: ${repository.getLastSplitDistance().roundToInt()}m";

        val job = CoroutineScope(Dispatchers.IO).launch {
            // distance total
            val totalDistanceRun = repository.getTotalDistanceRun()
            txtTotalDistance = "Total: ${totalDistanceRun.roundToInt()}m"
            refresh()
        }

        // split index
        txtSplitIndex = "Split: ${(repository.getIndex() + 1)}"

        updateType()

        // split time (sprint or jog)
        txtSplitTime = "Time: ${TimeFormatter.printDateTime(repository.getLastSplitElapsedTime())}"
        txtTotalTime = "Total: ${TimeFormatter.printDateTime(repository.getTotalElapsedTime())}"
    }

    fun updateType() {
        val runState = SharedPrefUtility.get(SharedPrefUtility.keyRunState, RunStates.Error.tag)
        val str = getString(runState)
        txtSplitType = str
    }

    private fun getString(type: String): String {
        var id = R.string.idle
        when (type) {
            RunStates.Idle.tag -> {/*default*/
            }
            RunStates.Jog.tag -> {
                id = R.string.jog
            }
            RunStates.Sprint.tag -> {
                id = R.string.sprint
            }
            RunStates.Pause.tag -> {
                id = R.string.pause
            }
            RunStates.Done.tag -> {
                id = R.string.done
            }
            else -> {
                id = R.string.dunno
            }
        }
        return MainApplication.applicationContext().resources.getString(id)
    }
}