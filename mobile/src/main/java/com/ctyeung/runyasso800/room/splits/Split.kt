package com.ctyeung.runyasso800.room.splits

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

/*
 * Split is 800m, minimum unit of run length in this workout
 */
@Entity(tableName = "split_table", primaryKeys=["splitIndex"])
data class Split(val splitIndex:Int=0,  // 0 - 9 : total of 10
                 val run_type:String,   // jog or sprint
                 val dis:Double,        // in meters x1000
                 val startTime:Long,    // in seconds
                 val startLat:Double,   // in Lat/Long unit x 1000
                 val startLong:Double,  // in Lat/Long unit x 1000
                 val endTime:Long,      // in seconds
                 val endLat:Double,     // in Lat/Long unit x 1000
                 val endLong:Double)    // in Lat/Long unit x 1000
{
    companion object{
        const val UNIT_SCALER:Double = 1000.0
        const val RUN_TYPE_SPRINT:String = "sprint"
        const val RUN_TYPE_JOG:String = "jog"
    }

    fun getStartDate():String
    {
        return formatDate(startTime)
    }

    fun getEndDate():String
    {
        return formatDate(endTime)
    }

    private fun formatDate(seconds:Long):String
    {
        val df = SimpleDateFormat("d MMM yyyy HH:mm:ss")
        val date = df.format(seconds)
        return date
    }
}