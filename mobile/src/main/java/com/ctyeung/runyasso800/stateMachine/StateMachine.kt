package com.ctyeung.runyasso800.stateMachine

import android.location.Location
import android.util.Log
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.RunViewModel
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.ctyeung.runyasso800.viewModels.StepViewModel
import java.lang.reflect.Type
import javax.inject.Inject

/*
 * TODO:
 *  1. Common 'Enter' and 'Exit' for states
 *  2. refactor execute and update logic for coherence and simplification
 *  3. Pause should be a persisted data structure as step type
 */
class StateMachine : IStateCallback {
    var prevLocation:Location ?= null
    private var previous:Type = StateIdle::class.java

    var actListener:IRunStatsCallBack?=null
    var stateMap = HashMap<Type, StateAbstract>()

    /*
     * TO DO :
     * Replace actListener with callback to Service
     * Then use Intent broadcast and receiver to communicate with Activity
     */
    constructor(actListener: IRunStatsCallBack) {
        this.actListener = actListener
        stateMap.clear()
        stateMap.put(StateSprint::class.java, StateSprint(this, actListener))
        stateMap.put(StateJog::class.java, StateJog(this, actListener))
        stateMap.put(StateError::class.java, StateError(this))
        stateMap.put(StateDone::class.java, StateDone(this, actListener))
        stateMap.put(StateClear::class.java, StateClear(this))
        stateMap.put(StatePause::class.java, StatePause(this, actListener))
        stateMap.put(StateResume::class.java, StateResume(this, actListener))
        stateMap.put(StateIdle::class.java, StateIdle(this, actListener))

        previous = StateIdle::class.java
        SharedPrefUtility.set(SharedPrefUtility.keyRunState, StateIdle::class.java)
    }

    /*
     * state has been updated
     */
    override fun onChangeState(current:Type) {
        previous = currentState
        SharedPrefUtility.set(SharedPrefUtility.keyRunState, current)

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
        when(currentState) {
            StateIdle::class.java,
            StatePause::class.java,
            StateResume::class.java -> {
                onChangeState(StateJog::class.java)
                actListener?.onChangedSplit()
            }
            StateJog::class.java,
            StateSprint::class.java -> { /* continue */ }
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
        when(currentState) {
            StateSprint::class.java,
            StateJog::class.java -> {
                stateMap[StatePause::class.java]?.execute((currentState))
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
        when(currentState) {
            StateIdle::class.java,
            StatePause::class.java,
            StateError::class.java,
            StateDone::class.java -> {
                stateMap[StateClear::class.java]?.execute(currentState)
            }

            else -> {
                // do nothing
            }
        }
    }

    /*
     * Update state machine of metrics
     * - wait until at least 2 consecutive reading are NOT null
     *
     * if total distance >= 800meter -> next state
     *  a. sprint, jog or done
     */
    fun update(location: Location) {
        if(null!=prevLocation) {
            when (currentState) {
                StateSprint::class.java,
                StateJog::class.java -> {
                    var state = stateMap[currentState] as MotionState
                    state.setLocation(prevLocation, location)
                    onChangeState(currentState)
                }
                else -> {}
            }
        }
        prevLocation = location
        SharedPrefUtility.set(SharedPrefUtility.keyLastLatitutde, location.latitude.toString())
        SharedPrefUtility.set(SharedPrefUtility.keyLastLongitude, location.longitude.toString())
    }

    private val currentState:Type
    get() {
        return SharedPrefUtility.get(SharedPrefUtility.keyRunState, StateError::class.java)
    }
}