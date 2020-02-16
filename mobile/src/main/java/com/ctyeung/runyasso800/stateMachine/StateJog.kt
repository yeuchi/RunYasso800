package com.ctyeung.runyasso800.stateMachine

import android.location.Location

object StateJog : MotionState(), Ijog, Isprint, Idone {

    override fun setState(previous:RunState) {

        this.prevState = previous
        // things to perform in this state

    }

    override fun getNextState():RunState {

        when(getIteration()) {

            10 -> {
                return RunState.Done
            }
            else -> {
                return RunState.Sprint
            }
        }
    }

    fun getIteration():Int {
        // fetch count from persistence
        return 0
    }
}