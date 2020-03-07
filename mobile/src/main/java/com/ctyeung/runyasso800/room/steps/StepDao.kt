package com.ctyeung.runyasso800.room.steps

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ctyeung.runyasso800.room.splits.Split

@Dao
interface StepDao
{
    @Query("SELECT * from step_table ORDER BY splitIndex, stepIndex")
    fun getAll() : LiveData<List<Step>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(step: Step)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(step: Step)

    @Query("DELETE FROM step_table")
    suspend fun deleteAll()
}