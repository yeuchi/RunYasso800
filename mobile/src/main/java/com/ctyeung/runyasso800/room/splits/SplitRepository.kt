package com.ctyeung.runyasso800.room.splits

import androidx.lifecycle.LiveData

import javax.inject.Inject

/*
 * Yasso 800 is composed of 10 x sprints = jobs of 800 meters
 */
class SplitRepository @Inject constructor(private val splitDao: SplitDao)
{
    var splits:LiveData<List<Split>> = splitDao.getAll()

    suspend fun insert(split: Split)
    {
        splitDao.insert(split)
    }

    suspend fun clear()
    {
        splitDao.deleteAll()
    }

    suspend fun update(split: Split)
    {
        splitDao.update(split)
    }

    suspend fun getTotalDistanceRun():Double {
        return splitDao.getTotalDistanceRun()
    }
}



