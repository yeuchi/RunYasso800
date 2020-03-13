package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.room.splits.SplitRepository
import com.ctyeung.runyasso800.utilities.TimeFormatter
import kotlinx.android.synthetic.main.activity_run.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class SplitViewModel (application: Application) : AndroidViewModel(application)
{
    var repository:SplitRepository
    var yasso:LiveData<List<Split>>
    var totalDistance:Double = 0.0
    var elapsedTime:Long = 0
    var disTotalString:String = "Total: 0m"
    var indexString:String = "Split: 1"
    var typeString:String = "Sprint / Jog"
    var elapsedTimeString:String = "Total: 00:00"

    init {
        val splitDao = YassoDatabase.getDatabase(application, viewModelScope).splitDao()
        repository = SplitRepository(splitDao)
        yasso = repository.yasso
    }

    fun insert(split: Split?) = viewModelScope.launch {
        if(null!=split) {
            setTextProperties(split)
            repository.insert(split)
        }
    }

    private fun setTextProperties(split:Split)
    {
        totalDistance += split.dis
        disTotalString = "Total: " + totalDistance.roundToInt() + "m"
        var index = yasso.value?.size?:0 + 1
        indexString = "Split: " + index.toString()
        typeString = split.run_type.capitalize()
        calculateTimeElapsed(split.endTime)
        elapsedTimeString = "Total: " + TimeFormatter.printTime(elapsedTime)
    }

    /*
     * calculate the total elapsed time since start
     * ** but what about 'PAUSE' ????
     */
    private fun calculateTimeElapsed(seconds:Long) {
        val size = yasso.value?.size?:0
        if(null!=yasso.value && size > 0)
            elapsedTime = seconds - yasso.value!![0].startTime

        else
            elapsedTime = 0
    }


    fun clear() = viewModelScope.launch {
        totalDistance = 0.0
        elapsedTime = 0
        repository.clear()
    }

    fun update(split: Split?) = viewModelScope.launch {
        if(null!=split) {
            setTextProperties(split)
            repository.update(split)
        }
    }
}