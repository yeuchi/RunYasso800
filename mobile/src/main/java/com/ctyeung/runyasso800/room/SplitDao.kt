package com.ctyeung.runyasso800.room

import androidx.lifecycle.LiveData
import androidx.room.*


/*
 * SQL statements here for CRUD operations
 */

@Dao
interface SplitDao
{
    @Query("SELECT * from split_table ORDER BY `index`")
    fun getAll() : LiveData<List<Split>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(split: Split)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(split: Split)

    @Query("DELETE FROM split_table")
    suspend fun deleteAll()
}
