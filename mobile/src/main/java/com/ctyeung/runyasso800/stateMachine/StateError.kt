package com.ctyeung.runyasso800.stateMachine

import java.lang.reflect.Type

class StateError : StateAbstract, Ierror, Iclear {

    constructor(listener:IStateCallback):super(listener)
    {

    }

    override fun execute(previous:Type) {

        this.prevState = previous
        // things to perform in this state
    }

    override fun goto() {

        /*
         * Stuck in error until user CLEAR
         */
    }
}