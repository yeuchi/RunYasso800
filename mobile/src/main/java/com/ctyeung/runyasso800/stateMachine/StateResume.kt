package com.ctyeung.runyasso800.stateMachine

import java.lang.reflect.Type

class StateResume : StateAbstract, Iresume, Isprint, Ijog {

    constructor(listener:IStateCallback):super(listener)
    {

    }

    override fun execute(previous:Type) {

        this.prevState = previous
        // things to perform in this state

        goto()
    }

    override fun goto() {

        /*
         * SPRINT or JOG ????????
         * !!! implement database query here  !!!!
         */

        // look up sharedPreference or db
        when(prevState){
            StateSprint::class.java -> listener.onChangeState(StateSprint::class.java)
            StateJog::class.java -> listener.onChangeState(StateJog::class.java)
            else -> listener.onChangeState(StateError::class.java)
        }
    }
}