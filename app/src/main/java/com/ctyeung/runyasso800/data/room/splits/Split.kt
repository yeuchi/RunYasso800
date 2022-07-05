package com.ctyeung.runyasso800.data.room.splits

import androidx.room.Entity

/*
 * To do:
 * 1. normalize (refactor)
 *    - replace [startTime, startLat, startLong] with stepStartIndex
 *    - replace [endTime, endLat, endLong] with stepStopIndex
 *
 * 2. implement Joined entity to handle read-only result
 *
 * Split is 800m, minimum unit of run length in this workout
 */
@Entity(tableName = "split_table", primaryKeys=["splitIndex", "run_type"])
data class Split(val splitIndex:Int=0,  // 0 - 9 : total of 10
                 val run_type:String,   // jog or sprint
                 var dis:Double,        // in meters x1000
                 val startTime:Long,    // in seconds
                 val startLat:Double,   // in Lat/Long unit x 1000
                 val startLong:Double,  // in Lat/Long unit x 1000
                 var endTime:Long,      // in seconds
                 var endLat:Double,     // in Lat/Long unit x 1000
                 var endLong:Double,    // in Lat/Long unit x 1000
                 var meetGoal:Boolean=true)  // duration < sprint goal (run_type == sprint)
{
    companion object {
        const val DEFAULT_SPLIT_DISTANCE: Double = 800.0
        const val DEFAULT_SPLIT_ITERATIONS: Int = 10 // 1 iteration (jog + sprint) = 2 splits
        const val RUN_TYPE_SPRINT: String = "sprint"
        const val RUN_TYPE_JOG: String = "jog"
    }
}