package com.ctyeung.runyasso800

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

class RunStateMachine {

    var previous:Location ?= null
    var stepDis:Double = 0.0
    var splitIndex:Int = 0
    var state:RunState = RunState.Idle
    var prevState:RunState = RunState.Idle

    /*
     * evaluate and change state as appropriate
     */
    fun changeState(goTo:RunState) {

        when(goTo) {
            RunState.Idle -> gotoIdle()
            RunState.Sprint -> gotoSprint()
            RunState.Jog -> gotoJog()
            RunState.Clear -> gotoClear()
            RunState.Done -> gotoDone()
            RunState.Pause -> gotoPause()
            RunState.Resume -> gotoResume()
            RunState.Error -> gotoError()
        }
    }

    fun update(present:Location)
    {
        if(!isMoving()) {

            stepDis += distance(previous, present)

            if (stepDis > 800) {
                when (state) {
                    RunState.Sprint -> changeState(RunState.Jog)
                    RunState.Jog -> changeState(RunState.Sprint)
                    // not considering other run-states
                }
            }
        }
    }

    fun gotoResume() {
        when(prevState) {
            RunState.Idle -> gotoSprint()
            RunState.Sprint -> gotoSprint()
            RunState.Jog -> gotoJog()
        }
    }

    fun isMoving():Boolean {
        when(state) {
            RunState.Done -> return false
            RunState.Pause -> return false
            RunState.Idle -> return false
            RunState.Error -> return false
        }
        return true
    }

    private fun gotoError() {
        state = RunState.Error
    }

    private fun gotoIdle() {
        // reset to idle
        state = RunState.Idle
    }

    /*
     * From Idle, Jog, Pause -> Sprint
     */
    private fun gotoSprint() {
        // evaluate if we are done ?
        if(isMoving()) {

            splitIndex++;

            if (splitIndex > 10) {
                // from jog: we are done !
                changeState(RunState.Done)
            } else {
                state = RunState.Sprint
            }
        }
    }

    private fun gotoJog() {
        if(isMoving()) {

            state = RunState.Jog
        }
    }

    private fun gotoClear() {
        // clear all (DB, sharedPreference, etc)
        stepDis = 0.0
        splitIndex = 0
        previous = null

        changeState(RunState.Idle)
    }

    /*
     * User stopped
     * @callback: give user option to the next activity or continue
     */
    private fun gotoPause() {
        if(isMoving()) {
            prevState = state
            state = RunState.Pause

            // call back to update
        }
    }

    /*
     * We are done !
     * @callback: let user move to the next activity
     */
    private fun gotoDone() {
        state = RunState.Done

        // call back to update
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