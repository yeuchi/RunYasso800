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

/*
 * To do :
 * 1. refactor variables as getters / setters
 */
class SplitViewModel (application: Application) : AndroidViewModel(application)
{
    var index:Int = 0
    var repository:SplitRepository
    var splits:LiveData<List<Split>>
    var totalDistance:Double = 0.0
    var elapsedTime:Long = 0
    var typeString:String = "Sprint / Jog"

    init {
        val splitDao = YassoDatabase.getDatabase(application, viewModelScope).splitDao()
        repository = SplitRepository(splitDao)
        splits = repository.splits
    }

    fun insert(split: Split?) = viewModelScope.launch {
        if(null!=split) {
            setProperties(split)
            repository.insert(split)
        }
    }

    private fun setProperties(split:Split)
    {
        typeString = split.run_type.capitalize()
        calculateTimeElapsed(split.endTime)
    }

    /*
     * calculate the total elapsed time since start
     * ** but what about 'PAUSE' ????
     */
    private fun calculateTimeElapsed(seconds:Long) {
        val size = splits.value?.size?:0
        if(null!=splits.value && size > 0)
            elapsedTime = seconds - splits.value!![0].startTime

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
            setProperties(split)
            repository.update(split)
        }
    }
}