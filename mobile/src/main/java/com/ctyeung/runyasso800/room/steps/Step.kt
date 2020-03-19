package com.ctyeung.runyasso800.room.steps

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_table", primaryKeys= ["splitIndex", "stepIndex"])
data class Step(var splitIndex:Int=0,        // 0 - 9 : total of 10
                var stepIndex:Int=0,         // 0 - N : step id
                var dis:Double=0.0,          // piecewise distance
                var run_type:String="",      // jog or sprint
                var time:Long=0,             // in seconds
                var latitude:Double=0.0,          // in Lat/Long unit x 1000
                var longitude:Double=0.0)         // in Lat/Long unit x 1000
{

}