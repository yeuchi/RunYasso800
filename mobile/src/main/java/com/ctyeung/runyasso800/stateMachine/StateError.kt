package com.ctyeung.runyasso800.stateMachine

object StateError : StateAbstract(), Ierror, Iclear {

    override fun setState(previous:RunState) {

        this.prevState = previous
        // things to perform in this state
    }

    override fun getNextState():RunState {

        /*
         * Stuck in error until user CLEAR
         */
        return RunState.Error
    }
}