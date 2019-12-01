package com.ctyeung.runyasso800.room.steps

import androidx.lifecycle.LiveData

class StepRepository(private val stepDao: StepDao)
{
    var steps:LiveData<List<Step>> = stepDao.getAll()

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