package com.ctyeung.runyasso800.stateMachine

object StateIdle :state(), Iidle, Isprint {

    override fun setState(previous:RunState) {

        this.prevState = previous
        // things to perform in this state

    }

    override fun getNextState():RunState {
        return RunState.Sprint
    }
}