package com.ctyeung.runyasso800.stateMachine

import android.app.Activity
import android.location.Location
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.ctyeung.runyasso800.utilities.LocationUtils
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.SplitViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import java.lang.reflect.Type

/*
 * To do:
 * 1. add a hash table for type -> state
 * 2. refactor execute and update logic for coherence and simplification
 */
class StateMachine : IStateCallback {
    var prevLocation:Location ?= null
    var current:Type
    var previous:Type

    // put below objects in a hash ?
    var stateSprint:StateSprint
    var stateJog:StateJog
    var stateIdle:StateIdle
    var stateError:StateError
    var stateDone:StateDone
    var stateClear:StateClear
    var statePause:StatePause
    var stateResume:StateResume
    var actListener:IRunStatsCallBack

    constructor(actListener: IRunStatsCallBack,
                splitViewModel: SplitViewModel,
                stepViewModel: StepViewModel)
    {
        this.actListener = actListener

        stateSprint = StateSprint(this, actListener, splitViewModel, stepViewModel)
        stateJog = StateJog(this, actListener, splitViewModel, stepViewModel)

        stateError = StateError(this)
        stateDone = StateDone(this, actListener)
        stateClear = StateClear(this, splitViewModel, stepViewModel)
        statePause = StatePause(this,actListener)
        stateResume = StateResume(this)

        stateIdle = StateIdle(this, actListener)
        current = StateIdle::class.java
        previous = StateIdle::class.java
    }

    /*
     * state has been updated
     */
    override fun onChangeState(type:Type) {
        previous = current
        current = type

        when(current){
            StateSprint::class.java,
            StateJog::class.java -> {
                if(previous != current)
                    actListener.onChangedSplit()
            }
            StateDone::class.java -> {
                stateDone.execute(current)
            }
            StateResume::class.java -> {
                stateResume.execute(current)
            }
            StateError::class.java -> {
                // Yikes !  Handle error here !
            }
        }
    }

    /*
     * ONLY when IDLE state
     * -> goto SPRINT state
     * -> When DONE -> callback Activity
     */
    fun interruptStart() {

        when(current) {
            StateIdle::class.java,
            StatePause::class.java,
            StateResume::class.java -> stateSprint.execute(current)

            StateJog::class.java,
            StateSprint::class.java -> {
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

        /*
         * previous state is sprint or jog
         * - we wish to return to that state on resume
         */
        when(current) {
            StateSprint::class.java,
            StateJog::class.java -> statePause.execute(previous)

            /*
             * if we were at pause, then resume !
             */
            StatePause::class.java -> stateResume.execute(previous)

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

        when(current) {
            StateIdle::class.java,
            StatePause::class.java,
            StateError::class.java,
            StateDone::class.java -> stateClear.execute(current)

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

    fun observe(lifeCycle:LifecycleOwner) {

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
     * - wait until at least 2 consecutive reading are NOT null
     *
     * if total distance >= 800meter -> next state
     *  a. sprint, jog or done
     */
    private fun update(location: Location) {
        if(null!=prevLocation && null!=location) {
            when (current) {
                StateSprint::class.java -> {
                    stateSprint.setLocation(prevLocation, location)
                }
                StateJog::class.java -> {
                    stateJog.setLocation(prevLocation, location)
                }
                // just update the latitute / longitude
                StateIdle::class.java -> {
                    actListener.onHandleLocationUpdate()
                }

                else -> {
                    // nothing to do
                }
            }
        }
        prevLocation = location
    }
}