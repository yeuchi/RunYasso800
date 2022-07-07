package com.ctyeung.runyasso800.data.room.steps

import androidx.lifecycle.LiveData
import javax.inject.Inject

class StepRepository @Inject constructor(private val stepDao: StepDao) {
    var steps: LiveData<List<Step>> = stepDao.getAll()

    fun query(splitIndex: Int): List<Step> {
        return stepDao.getBySplit(splitIndex)
    }

    suspend fun insert(step: Step) {
        stepDao.insert(step)
    }

    suspend fun deleteAll() {
        stepDao.deleteAll()
    }

    suspend fun update(step: Step) {
        stepDao.update(step)
    }
}
