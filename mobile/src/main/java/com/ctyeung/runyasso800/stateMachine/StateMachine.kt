package com.ctyeung.runyasso800.stateMachine

import android.location.Location
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.RunViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import java.lang.reflect.Type

/*
 * To do:
 * 2. refactor execute and update logic for coherence and simplification
 */
object StateMachine : IStateCallback {
    var prevLocation:Location ?= null
    var current:Type = StateIdle::class.java
    var previous:Type = StateIdle::class.java

    var actListener:IRunStatsCallBack?=null
    var stateMap = HashMap<Type, StateAbstract>()

    /*
     * TO DO :
     * Replace actListener with callback to Service
     * Then use Intent broadcast and receiver to communicate with Activity
     */
    fun initialize(actListener: IRunStatsCallBack) {
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
        when(current) {
            StateSprint::class.java,
            StateJog::class.java -> {
                stateMap[StatePause::class.java]?.execute((current))
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

    /*
     * Update state machine of metrics
     * - wait until at least 2 consecutive reading are NOT null
     *
     * if total distance >= 800meter -> next state
     *  a. sprint, jog or done
     */
    fun update(location: Location) {
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