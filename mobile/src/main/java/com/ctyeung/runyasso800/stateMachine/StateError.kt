package com.ctyeung.runyasso800.stateMachine

object StateError : StateAbstract(), Ierror, Iclear {

    override fun execute(previous:RunState) {

        this.prevState = previous
        // things to perform in this state
    }

    override fun goto() {

        /*
         * Stuck in error until user CLEAR
         */
    }
}