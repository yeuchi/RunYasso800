package com.ctyeung.runyasso800.stateMachine

import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import java.lang.reflect.Type

class StateIdle :StateAbstract, Iidle, Isprint {
    var actListener:IRunStatsCallBack

    constructor(listener:IStateCallback,
                actListener:IRunStatsCallBack):super(listener)
    {
        this.actListener = actListener
    }

    override fun execute(previous:Type) {

        this.prevState = previous
        // things to perform in this state

        goto()
    }

    override fun goto():Boolean {
        // do nothing -- IDLE
        // wait for interrupt to change state

        /*
         * update view
         * update state machine
         */
        actListener.onHandleLocationUpdate()
        listener.onChangeState(this.javaClass)
        return false
    }
}