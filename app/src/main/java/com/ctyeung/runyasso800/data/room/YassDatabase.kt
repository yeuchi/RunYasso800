package com.ctyeung.runyasso800.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ctyeung.runyasso800.data.room.splits.SplitDao
import com.ctyeung.runyasso800.data.room.steps.Step
import com.ctyeung.runyasso800.data.room.splits.Split
import com.ctyeung.runyasso800.data.room.steps.StepDao

@Database(entities = [Split::class, Step::class], version = 1, exportSchema = false)
abstract class YassDatabase : RoomDatabase() {
    abstract fun splitDao(): SplitDao
    abstract fun stepDao(): StepDao
}