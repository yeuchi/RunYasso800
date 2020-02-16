package com.ctyeung.runyasso800.stateMachine

import android.location.Location

object StateSprint : MotionState(), Isprint, Ijog {

    override fun setState(previous:RunState) {

        this.prevState = previous
        // things to perform in this state

    }

    override fun getNextState():RunState {
        return RunState.Jog
    }

}