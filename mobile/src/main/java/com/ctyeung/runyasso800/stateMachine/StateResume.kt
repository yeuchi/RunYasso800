package com.ctyeung.runyasso800.stateMachine

object StateResume : StateAbstract(), Iresume, Isprint, Ijog {

    override fun execute(previous:RunState) {

        this.prevState = previous
        // things to perform in this state

        goto()
    }

    override fun goto() {

        /*
         * SPRINT or JOG ????????
         * !!! implement database query here  !!!!
         */
        val decision:RunState = RunState.Sprint

        // look up sharedPreference or db
        when(decision){
            RunState.Sprint -> listener.onChangeState(StateSprint)

            RunState.Jog -> listener.onChangeState(StateJog)
        }
    }
}