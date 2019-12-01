package com.ctyeung.runyasso800.room.splits

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

/*
 * Split is 800m, minimum unit of run length in this workout
 */
@Entity(tableName = "split_table")
data class Split(@PrimaryKey @ColumnInfo(name = "index")val index:Int=0,    // 0 - 9 : total of 10
                                                         val run_type:String,   // jog or sprint
                                                         val distance:Long,     // in meters x1000
                                                         val startTime:Long,    // in seconds
                                                         val startLat:Long,     // in Lat/Long unit x 1000
                                                         val startLong:Long,    // in Lat/Long unit x 1000
                                                         val endTime:Long,      // in seconds
                                                         val endLat:Long,       // in Lat/Long unit x 1000
                                                         val endLong:Long)      // in Lat/Long unit x 1000
{
    companion object{
        const val UNIT_SCALER:Double = 1000.0
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

    fun getDistanceInMeter():Number
    {
        return distance / UNIT_SCALER
    }

    fun getStartLatitude():Number
    {
        return startLat.toDouble()/UNIT_SCALER
    }

    fun getStartLongtitude():Number
    {
        return startLong.toDouble()/UNIT_SCALER
    }

    fun getEndLatitude():Number
    {
        return endLat.toDouble()/UNIT_SCALER
    }

    fun getEndLongtitude():Number
    {
        return endLong.toDouble()/UNIT_SCALER
    }
}