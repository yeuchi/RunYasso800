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
                 var dis:Double,        // in meters x1000
                 val startTime:Long,    // in seconds
                 val startLat:Double,   // in Lat/Long unit x 1000
                 val startLong:Double,  // in Lat/Long unit x 1000
                 var endTime:Long,      // in seconds
                 var endLat:Double,     // in Lat/Long unit x 1000
                 var endLong:Double)    // in Lat/Long unit x 1000
{
    companion object{
        const val DEFAULT_SPLIT_DISTANCE:Double = 800.0
        const val DEFAULT_SPLIT_ITERATIONS:Int = 20
        const val UNIT_SCALER:Double = 1000.0
        const val RUN_TYPE_SPRINT:String = "sprint"
        const val RUN_TYPE_JOG:String = "jog"
    }

    fun update( dis:Double,        // in meters x1000
                endTime:Long,      // in seconds
                endLat:Double,     // in Lat/Long unit x 1000
                endLong:Double)    // in Lat/Long unit x 1000
    {
        this.dis = dis
        this.endTime = endTime
        this.endLat = endLat
        this.endLong = endLong
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