package com.ctyeung.runyasso800.stateMachine

object StateDone : StateAbstract(), Idone {

    override fun setState(previous:RunState) {

        this.prevState = previous
        // things to perform in this state

    }

    override fun getNextState():RunState {

        /*
         * we are stuck in this state
         * (except if user CLEAR
         */
        return RunState.Done
    }
}