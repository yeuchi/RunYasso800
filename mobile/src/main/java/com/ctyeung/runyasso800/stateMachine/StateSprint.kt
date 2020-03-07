package com.ctyeung.runyasso800.stateMachine

import android.location.Location

object StateSprint : MotionState(), Isprint, Ijog {

    /*
     * Acknowledge we are in Sprint state
     */
    override fun execute(previous:RunState) {

        this.prevState = previous
        // initialization things to perform in this state

        goto()
    }

    /*
     * state change conditions
     * - either in Sprint or Jog
     */
    override fun goto() {
        when {
            stepDis >= FINISH_DISTANCE -> {

                listener.onChangeState(StateJog)
            }

            else -> {
                // do nothing -- keep sprinting
            }
        }
    }

}