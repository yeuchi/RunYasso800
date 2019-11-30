package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.room.Split
import com.ctyeung.runyasso800.room.SplitDatabase
import com.ctyeung.runyasso800.room.SplitRepository
import kotlinx.coroutines.launch

class SplitViewModel (application: Application) : AndroidViewModel(application)
{
    var repository:SplitRepository
    var yasso:LiveData<List<Split>>

    init {
        val splitDao = SplitDatabase.getDatabase(application, viewModelScope).splitDao()
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