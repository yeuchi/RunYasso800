package com.ctyeung.runyasso800.stateMachine

import android.location.Location
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.ctyeung.runyasso800.utilities.LocationUtils
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.RunViewModel
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

    var actListener:IRunStatsCallBack
    var stateMap = HashMap<Type, StateAbstract>()

    constructor(actListener: IRunStatsCallBack,
                runViewModel: RunViewModel,
                stepViewModel: StepViewModel)
    {
        this.actListener = actListener
        stateMap.put(StateSprint::class.java, StateSprint(this, actListener, runViewModel, stepViewModel))
        stateMap.put(StateJog::class.java, StateJog(this, actListener, runViewModel, stepViewModel))
        stateMap.put(StateError::class.java, StateError(this))
        stateMap.put(StateDone::class.java, StateDone(this, actListener))
        stateMap.put(StateClear::class.java, StateClear(this, runViewModel, stepViewModel))
        stateMap.put(StatePause::class.java, StatePause(this, actListener))
        stateMap.put(StateResume::class.java, StateResume(this))
        stateMap.put(StateIdle::class.java, StateIdle(this, actListener))

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
            StateJog::class.java,
            StateDone::class.java,
            StateResume::class.java -> {
                stateMap[current]?.execute(current)
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
            StateResume::class.java -> {
                onChangeState(StateSprint::class.java)
                actListener.onChangedSplit()
            }
            StateJog::class.java,
            StateSprint::class.java -> {
                // continue
            }
            else -> {}
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
            StateJog::class.java -> {
                stateMap[StatePause::class.java]?.execute((previous))
            }

            //if we were at pause, then resume !
            StatePause::class.java -> {
                stateMap[StateResume::class.java]?.execute((previous))
            }

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
            StateDone::class.java -> {
                stateMap[StateClear::class.java]?.execute(current)
            }

            else -> {
                // do nothing
            }
        }
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
        if(null!=prevLocation) {
            when (current) {
                StateSprint::class.java,
                StateJog::class.java -> {
                    var state = stateMap[current] as MotionState
                    state.setLocation(prevLocation, location)
                    onChangeState(current)
                }
                else -> {}
            }
        }
        prevLocation = location
    }
}