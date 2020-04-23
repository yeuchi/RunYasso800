package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.room.splits.SplitRepository
import com.ctyeung.runyasso800.stateMachine.*
import com.ctyeung.runyasso800.utilities.TimeFormatter
import kotlinx.android.synthetic.main.activity_run.*
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/*
 * To do :
 * 1. refactor variables as getters / setters
 */
class RunViewModel (application: Application) : AndroidViewModel(application)
{
    var repository:SplitRepository
    var splits:LiveData<List<Split>>

    init {
        val splitDao = YassoDatabase.getDatabase(application, viewModelScope).splitDao()
        repository = SplitRepository(splitDao)
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

    fun init() {
        txtTotalSplits = "${MainApplication.applicationContext().resources.getString(R.string.total)}: ${SharedPrefUtility.getNumIterations()}"
    }

    fun updateData() {
        txtLat = StateMachine.prevLocation?.latitude.toString()
        txtLong = StateMachine.prevLocation?.longitude.toString()

        // distance in current split
        txtStepDistance = "Dis: ${getLastSplitDistance().roundToInt()}m";
        // distance total
        txtTotalDistance = "Total: ${getTotalDistance().roundToInt()}m"
        // split index
        txtSplitIndex =  "Split: ${(getIndex()+1)}"

        updateType()

        // split time (sprint or jog)
        txtSplitTime = "Time: ${TimeFormatter.printDateTime(getLastSplitElapsedTime())}"
        txtTotalTime = "Total: ${TimeFormatter.printDateTime(getTotalElapsedTime())}"
    }

    fun updateType() {
        /*
         * Use a hash here to reduce the code
         */
        val str = getString(StateMachine.current)
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
       return SharedPrefUtility.getIndex(SharedPrefUtility.keySplitIndex)
    }

    fun setIndex(i:Int) {
        SharedPrefUtility.setIndex(SharedPrefUtility.keySplitIndex, i)
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

    fun getTotalDistance():Double {
        var dis:Double = 0.0
        val list:List<Split>? = splits.value
        if(null!=list && list.size>0) {
            for (split in list) {
                dis += split.dis
            }
        }
        return dis
    }

    fun getLastSplitDistance():Double {
        return SharedPrefUtility.getSplitDistance()
    }

    fun insert(split: Split?) = viewModelScope.launch {
        if(null!=split) {
            repository.insert(split)
        }
    }

    fun clear() = viewModelScope.launch {
        repository.clear()
        SharedPrefUtility.setSplitDistance(0.0)
        SharedPrefUtility.setIndex(SharedPrefUtility.keySplitIndex, 0)
    }

    fun update(split: Split?) = viewModelScope.launch {
        if(null!=split) {
            repository.update(split)
        }
    }
}