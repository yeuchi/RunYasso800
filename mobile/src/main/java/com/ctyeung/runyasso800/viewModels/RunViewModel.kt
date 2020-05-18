package com.ctyeung.runyasso800.viewModels

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.ctyeung.runyasso800.dagger.DaggerComponent
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.room.splits.SplitRepository
import com.ctyeung.runyasso800.stateMachine.*
import com.ctyeung.runyasso800.utilities.TimeFormatter
import kotlinx.android.synthetic.main.activity_run.*
import kotlinx.coroutines.*
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/*
 * TODO:
 *  1. Magic strings need to be consolidated into string.xml
 *  2. update time fields at every second interval.
 */
class RunViewModel (application: Application) : AndroidViewModel(application)
{
    @Inject @field:Named("split") lateinit var repository:SplitRepository
    var splits:LiveData<List<Split>>

    init {
        DaggerComponent.create().injectRunViewModel(this)
        splits = repository.splits
    }

    var txtLat:String = ""
    var txtLong:String = ""
    var txtStepDistance:String = "Step: 0m"
    var txtTotalDistance:String = "Total:800m"
    var txtSplitIndex:String = "Split: 1"
    var txtSplitTime:String = "Time: 00:00"
    var txtTotalTime:String = "Total: 00:00"
    var txtSplitType:String = "Sprint/Jog"
    var txtTotalSplits:String = "Total: 10"
    val defaultValue = "--"

    fun init() {
        txtTotalSplits = "${MainApplication.applicationContext().resources.getString(R.string.total)}: ${SharedPrefUtility.get(SharedPrefUtility.keyNumIterations, Split.DEFAULT_SPLIT_ITERATIONS)}"
        SharedPrefUtility.set(SharedPrefUtility.keyLastLatitutde, defaultValue)
        SharedPrefUtility.set(SharedPrefUtility.keyLastLongitude, defaultValue)
    }

    fun updateData() = runBlocking {
        txtLat = SharedPrefUtility.get(SharedPrefUtility.keyLastLatitutde, defaultValue)
        txtLong = SharedPrefUtility.get(SharedPrefUtility.keyLastLongitude, defaultValue)

        // distance in current split
        txtStepDistance = "Dis: ${getLastSplitDistance().roundToInt()}m";

        val job = CoroutineScope(Dispatchers.IO).launch {
            // distance total
            val totalDistanceRun = repository.getTotalDistanceRun()
            txtTotalDistance = "Total: ${totalDistanceRun.roundToInt()}m"
        }
        job.join()

        // split index
        txtSplitIndex =  "Split: ${(getIndex()+1)}"

        updateType()

        // split time (sprint or jog)
        txtSplitTime = "Time: ${TimeFormatter.printDateTime(getLastSplitElapsedTime())}"
        txtTotalTime = "Total: ${TimeFormatter.printDateTime(getTotalElapsedTime())}"
    }

    fun updateType() {
        val runState = SharedPrefUtility.get(SharedPrefUtility.keyRunState, StateError::class.java)
        val str = getString(runState)
        txtSplitType = str
    }

    private fun getString(type: Type):String {
        var id = R.string.idle
        when(type) {
            StateIdle::class.java -> {/*default*/}
            StateJog::class.java -> { id = R.string.jog }
            StateSprint::class.java -> { id = R.string.sprint }
            StatePause::class.java -> {id = R.string.pause}
            StateDone::class.java -> {id = R.string.done}
            else -> {id = R.string.dunno}
        }
        return MainApplication.applicationContext().resources.getString(id)
    }

    fun getIndex():Int {
       return SharedPrefUtility.get(SharedPrefUtility.keySplitIndex, 0)
    }

    fun getLastSplitElapsedTime():Long {
        val size = splits.value?.size?:0
        if(size>0){
            var split = splits.value!![size-1]
            val now = System.currentTimeMillis()
            return (now - split.startTime)
        }
        return 0
    }

    fun getTotalElapsedTime():Long {
        val list:List<Split>? = splits.value
        val now = System.currentTimeMillis()
        if(null!=list && list.size>0) {
            return now - list[0].startTime
        }
        return 0
    }

    fun getLastSplitDistance():Double {
        return SharedPrefUtility.get(SharedPrefUtility.keySplitDistance, 0f).toDouble()
    }

    fun insert(split: Split?) = viewModelScope.launch {
        if(null!=split) {
            repository.insert(split)
        }
    }

    fun clear() = viewModelScope.launch {
        repository.clear()
        SharedPrefUtility.set(SharedPrefUtility.keySplitDistance, 0f)
        SharedPrefUtility.set(SharedPrefUtility.keySplitIndex, 0)
    }

    fun update(split: Split?) = viewModelScope.launch {
        if(null!=split) {
            repository.update(split)
        }
    }
}