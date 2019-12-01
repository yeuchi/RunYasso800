package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.room.splits.SplitRepository
import kotlinx.coroutines.launch

class SplitViewModel (application: Application) : AndroidViewModel(application)
{
    var repository:SplitRepository
    var yasso:LiveData<List<Split>>

    init {
        val splitDao = YassoDatabase.getDatabase(application, viewModelScope).splitDao()
        repository = SplitRepository(splitDao)
        yasso = repository.yasso
    }

    fun insert(split: Split) = viewModelScope.launch {
        repository.insert(split)
    }

    fun clear() = viewModelScope.launch {
        repository.clear()
    }

    fun update(split: Split) = viewModelScope.launch {
        repository.update(split)
    }
}