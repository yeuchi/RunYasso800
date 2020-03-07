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
    var current:StateAbstract = StateIdle
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

        when(current::class) {
            StateIdle::class,
            StatePause::class,
            StateResume::class -> StateSprint.execute(current.runState)

            StateJog::class,
            StateSprint::class -> {
                // continue
            }

            else -> {
                // do nothing
            }
        }
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

        when(current::class) {
            StateSprint::class,
            StateJog::class -> StatePause.execute(current.runState)

            StatePause::class -> interruptStart()

            else -> {
                // do nothing
            }
        }
    }

    /*
     * Only when PAUSE state
     * -> goto CLEAR -> IDLE
     */
    fun interruptClear() {

        when(current::class) {
            StatePause::class,
            StateError::class,
            StateDone::class -> StateClear.execute(current.runState)

            else -> {
                // do nothing
            }
        }
    }

    /*
     * Only when DONE state
     * -> going to next Activity
     * (so reset state machine)
     */
    fun interruptNext() {

        // reset or delete all
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
        when(current::class) {
            StateSprint::class -> StateSprint.setLocation(location)
            StateJog::class -> StateJog.setLocation(location)

            // check for state change

            else -> {
                // nothing to do
            }
        }
    }
}