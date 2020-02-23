package com.ctyeung.runyasso800.stateMachine

import android.location.Location

object StateSprint : MotionState(), Isprint, Ijog {

    override fun execute(previous:RunState) {

        this.prevState = previous
        // things to perform in this state

        goto()
    }

    override fun goto() {
        when {
            stepDis >= FINISH_DISTANCE -> listener.onChangeState(StateJog)

            else -> {
                // do nothing -- keep sprinting
            }
        }
    }

}