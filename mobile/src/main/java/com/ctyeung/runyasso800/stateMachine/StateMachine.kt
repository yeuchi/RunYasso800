package com.ctyeung.runyasso800.stateMachine

import android.location.Location

enum class RunState {
    Idle,
    Resume, // start / come back from pause
    Sprint,
    Jog,
    Pause,
    Clear,
    Done,
    Error
}

object StateMachine {

    var state:StateAbstract? = null

    fun getCurrent() : RunState {

        val s = state?.runState?:RunState.Idle
        return s;
    }

    /*
     * ONLY when IDLE state
     * -> goto SPRINT state
     * -> When DONE -> callback Activity
     */
    fun interruptStart() {

    }

    /*
      * ONLY when SPRINT or JOG or PAUSE state
      * - SPRINT / JOG -> goto PAUSE
      * - PAUSE -> goto RESUME
      *
      * Pause everything (timer, distance measure, etc) ... let user cheat.
      * Resume back to sprint / jog
      */
    fun interruptPause() {

    }

    /*
     * Only when PAUSE state
     * -> goto CLEAR -> IDLE
     */
    fun interruptClear() {

    }

    /*
     * Only when DONE state
     * -> goto next Activity
     */
    //fun interruptNext() {}

    /*
     * Update state machine of metrics
     */
    fun update(current:Location) {

        when(state) {
            RunState.Sprint -> StateSprint.setLocation(current)
            RunState.Jog -> StateJog.setLocation(current)

            // check for state change

            else -> {
                // nothing to do
            }
        }
    }

    /*
     * Measure distance traveled
     * @return distance in meters
     */
    private fun distance(previous:Location?, present:Location):Double {

        // perform calculations
        return 0.0;
    }
}