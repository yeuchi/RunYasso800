package com.ctyeung.runyasso800.features.run

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.data.room.splits.Split
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.features.run.stateMachine.RunStates
import com.ctyeung.runyasso800.storage.SharedPrefUtility
import com.ctyeung.runyasso800.storage.TimeFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.roundToInt

class SplitViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    @field:Named("split")
    lateinit var repository: SplitRepository
    var splits: LiveData<List<Split>>

    init {
//        DaggerComponent.create().injectRunViewModel(this)
        splits = repository.splits
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

    fun init() {
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
        txtStepDistance = "Dis: ${getLastSplitDistance().roundToInt()}m";

        val job = CoroutineScope(Dispatchers.IO).launch {
            // distance total
            val totalDistanceRun = repository.getTotalDistanceRun()
            txtTotalDistance = "Total: ${totalDistanceRun.roundToInt()}m"
            refresh()
        }

        // split index
        txtSplitIndex = "Split: ${(getIndex() + 1)}"

        updateType()

        // split time (sprint or jog)
        txtSplitTime = "Time: ${TimeFormatter.printDateTime(getLastSplitElapsedTime())}"
        txtTotalTime = "Total: ${TimeFormatter.printDateTime(getTotalElapsedTime())}"
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

    fun getIndex(): Int {
        return SharedPrefUtility.get(SharedPrefUtility.keySplitIndex, 0)
    }

    fun getLastSplitElapsedTime(): Long {
        val size = splits.value?.size ?: 0
        if (size > 0) {
            var split = splits.value!![size - 1]
            val now = System.currentTimeMillis()
            return (now - split.startTime)
        }
        return 0
    }

    fun getTotalElapsedTime(): Long {
        val list: List<Split>? = splits.value
        val now = System.currentTimeMillis()
        if (null != list && list.size > 0) {
            return now - list[0].startTime
        }
        return 0
    }

    fun getLastSplitDistance(): Double {
        return SharedPrefUtility.get(SharedPrefUtility.keySplitDistance, 0f).toDouble()
    }

    fun insert(split: Split?) = viewModelScope.launch {
        if (null != split) {
            repository.insert(split)
        }
    }

    fun clear() = viewModelScope.launch {
        repository.deleteAll()
        SharedPrefUtility.set(SharedPrefUtility.keySplitDistance, 0f)
        SharedPrefUtility.set(SharedPrefUtility.keySplitIndex, 0)
    }

    fun update(split: Split?) = viewModelScope.launch {
        if (null != split) {
            repository.update(split)
        }
    }
}