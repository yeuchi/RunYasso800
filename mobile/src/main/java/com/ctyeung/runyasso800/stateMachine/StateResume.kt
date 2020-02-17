package com.ctyeung.runyasso800.stateMachine

object StateResume : StateAbstract(), Iresume, Isprint, Ijog {

    override fun setState(previous:RunState) {

        this.prevState = previous
        // things to perform in this state
    }

    override fun getNextState():RunState {

        /*
         * SPRINT or JOG ?
         */

        // look up sharedPreference or db
        return RunState.Sprint
    }
}