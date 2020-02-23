package com.ctyeung.runyasso800.stateMachine

object StateDone : StateAbstract(), Idone {

    override fun execute(previous:RunState) {

        this.prevState = previous
        // things to perform in this state

    }

    override fun goto() {

        /*
         * we are stuck in this state
         * (except if user CLEAR
         */
    }
}