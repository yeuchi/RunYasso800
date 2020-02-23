package com.ctyeung.runyasso800.stateMachine

object StateClear : StateAbstract(), Iclear, Iidle {
    override fun execute(previous:RunState) {

        this.prevState = previous
        // things to perform in this state

        goto()
    }

    /*
     * go to idle after we clear everything
     */
    override fun goto() {
        listener.onChangeState(StateIdle)
    }
}