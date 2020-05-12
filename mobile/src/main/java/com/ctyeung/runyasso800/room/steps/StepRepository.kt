package com.ctyeung.runyasso800.room.steps

import androidx.lifecycle.LiveData
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.stateMachine.MotionState
import com.ctyeung.runyasso800.viewModels.RunViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Named

class StepRepository @Inject constructor(private val stepDao: StepDao)
{
    var steps:LiveData<List<Step>> = stepDao.getAll()

    fun query(splitIndex:Int):List<Step> {
        return stepDao.getBySplit(splitIndex)
    }

    suspend fun insert(step:Step)
    {
        stepDao.insert(step)
    }

    suspend fun clear()
    {
        stepDao.deleteAll()
    }

    suspend fun update(step:Step)
    {
        stepDao.update(step)
    }
}
