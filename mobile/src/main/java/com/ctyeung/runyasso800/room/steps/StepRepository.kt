package com.ctyeung.runyasso800.room.steps

import androidx.lifecycle.LiveData
import javax.inject.Inject

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
