package com.ctyeung.runyasso800.stateMachine

import android.location.Location

object StateJog : MotionState(), Ijog, Isprint, Idone {

    /*
     * Acknowledge we are in Jog state
     */
    override fun execute(previous:RunState) {

        this.prevState = previous
        // things to perform in this state

        goto()
    }

    /*
     * state change conditions
     * - Jog, Sprint or Done
     */
    override fun goto() {

        when {
            stepDis >= FINISH_DISTANCE -> changeState()

            else -> {
                // do nothing -- keep jogging
            }
        }
    }

    /*
     * Check if we are on the 10th split -> DONE !
     * - else, we have more iterations
     */
    fun changeState() {

        when(splitIndex) {

            10 -> {
                listener.onChangeState(StateDone)
            }
            else -> {
                listener.onChangeState(StateSprint)
            }
        }
    }
}