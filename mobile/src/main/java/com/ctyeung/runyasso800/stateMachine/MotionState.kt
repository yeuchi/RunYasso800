package com.ctyeung.runyasso800.stateMachine

import android.location.Location

abstract class MotionState : state() {
    var stepDis:Double = 0.0
    var previous:Location ?= null
    var splitIndex:Int = 0

    fun setLocation(current: Location?) {
        stepDis += distance(previous, current)

    }

    /*
     * Measure distance traveled
     * @return distance in meters
     */
    private fun distance(previous:Location?, present:Location?):Double {

        // perform calculations
        return 0.0;
    }
}