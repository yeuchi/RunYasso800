package com.ctyeung.runyasso800.stateMachine

import java.lang.reflect.Type

class StatePause : StateAbstract, Ipause, Iresume, Iclear {

    constructor(listener:IStateCallback):super(listener)
    {

    }
    override fun execute(previous:Type) {

        this.prevState = previous
        // things to perform in this state

        goto()
    }

    override fun goto():Boolean {
        /*
         * we are stuck in this state
         * (except if user CLEAR
         */
        when(prevState){
            StatePause::class.java -> {
                listener.onChangeState(StateResume::class.java)
                return true
            }
            else -> {
                // do nothing -- stay in pause
                return false
            }
        }
    }
}