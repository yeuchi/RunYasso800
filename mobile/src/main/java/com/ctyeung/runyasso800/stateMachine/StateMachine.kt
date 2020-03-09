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

class StateMachine : IStateCallback {
    var prevLocation:Location ?= null
    var current:Type
    var previous:Type
    lateinit var listener: IRunStatsCallBack

    // put below objects in a hash ?
    var stateSprint:StateSprint
    var stateJog:StateJog
    var stateIdle:StateIdle
    var stateError:StateError
    var stateDone:StateDone
    var stateClear:StateClear
    var statePause:StatePause
    var stateResume:StateResume

    constructor(actListener: IRunStatsCallBack,
                splitViewModel: SplitViewModel,
                stepViewModel: StepViewModel)
    {
        stateSprint = StateSprint(this, actListener, splitViewModel, stepViewModel)
        stateJog = StateJog(this, actListener, splitViewModel, stepViewModel)

        stateError = StateError(this)
        stateDone = StateDone(this)
        stateClear = StateClear(this)
        statePause = StatePause(this)
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
            StateError::class.java -> {
                /*
                 * Yikes !  Handle error here !
                 */
            }
        }
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
            StateResume::class -> stateSprint.execute(current)

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
            StateJog::class -> statePause.execute(current)

            /*
             * if we were at pause, then resume !
             */
            StatePause::class -> stateResume.execute(previous)

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
            StateDone::class -> stateClear.execute(current)

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
     * if total distance >= 800meter -> next state
     *  a. sprint, jog or done
     */
    private fun update(location: Location) {
        if(null!=prevLocation) {
            when (current::class) {
                StateSprint::class -> stateSprint.setLocation(prevLocation, location)
                StateJog::class -> stateJog.setLocation(prevLocation, location)

                // check for state change

                else -> {
                    // nothing to do
                }
            }
            prevLocation = location
        }
    }
}