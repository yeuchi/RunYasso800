package com.ctyeung.runyasso800.stateMachine

import java.lang.reflect.Type

class StateDone : StateAbstract, Idone {

    constructor(listener:IStateCallback):super(listener)
    {

    }

    override fun execute(previous:Type) {

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