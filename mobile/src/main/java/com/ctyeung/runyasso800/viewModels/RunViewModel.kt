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
class RunViewModel (application: Application) : AndroidViewModel(application)
{
    var repository:SplitRepository
    var splits:LiveData<List<Split>>

    init {
        val splitDao = YassoDatabase.getDatabase(application, viewModelScope).splitDao()
        repository = SplitRepository(splitDao)
        splits = repository.splits
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
    }

    fun update(split: Split?) = viewModelScope.launch {
        if(null!=split) {
            repository.update(split)
        }
    }
}