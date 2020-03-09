package com.ctyeung.runyasso800.stateMachine

import java.lang.reflect.Type

class StateClear : StateAbstract, Iclear, Iidle {

    constructor(listener:IStateCallback):super(listener)
    {

    }

    override fun execute(previous:Type) {

        this.prevState = previous
        // things to perform in this state

        goto()
    }

    /*
     * go to idle after we clear everything
     */
    override fun goto() {
        listener.onChangeState(StateIdle::class.java)
    }
}