package com.ctyeung.runyasso800.stateMachine

import android.app.Activity
import android.location.Location
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.utilities.LocationUtils
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack

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

object StateMachine : IStateCallback {
    var prevLocation:Location ?= null
    var current:StateAbstract? = null
    lateinit var listener: IRunStatsCallBack

    /*
     * state has been updated
     */
    override fun onChangeState(state: StateAbstract) {
        this.current = state
    }

    fun getStateEnum() : RunState {

        val s = current?.runState?:RunState.Idle
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
     * -> going to next Activity
     * (so reset state machine)
     */
    fun interruptNext() {
        // reset
    }

    fun observe(listener:IRunStatsCallBack,
                lifeCycle:LifecycleOwner) {

        this.listener = listener

        LocationUtils.getLocation().observe(lifeCycle, Observer { location ->
            location?.let{
                // Yay! location recived. Do location related work here
                Log.i("RunActivity","Location: ${location.latitude}  ${location.longitude}")
                update(location)
            }
        })
    }

    /*
     * Update state machine of metrics
     */
    private fun update(location: Location) {

        if(null==prevLocation)
        {
            prevLocation = location
            return
        }

        // query total distance
        /*
         * if total distance >= 800meter -> next state
         *  a. sprint, jog or done
         */
        when(getStateEnum()) {
            RunState.Sprint -> StateSprint.setLocation(location)
            RunState.Jog -> StateJog.setLocation(location)

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