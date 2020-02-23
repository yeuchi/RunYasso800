package com.ctyeung.runyasso800.stateMachine

object StateIdle :StateAbstract(), Iidle, Isprint {

    override fun execute(previous:RunState) {

        this.prevState = previous
        // things to perform in this state
    }

    override fun goto() {
        // do nothing -- IDLE

        // wait for interrupt to change state
    }
}