package com.ctyeung.runyasso800.room.steps

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_table", primaryKeys= ["iteration", "step"])
data class Step(var iteration:Int=0,   // 0 - 9 : total of 10
                var step:Int=0,
                var run_type:String="",   // jog or sprint
                var time:Long=0,         // in seconds
                var lat:Double=0.0,          // in Lat/Long unit x 1000
                var long:Double=0.0)         // in Lat/Long unit x 1000
{

}