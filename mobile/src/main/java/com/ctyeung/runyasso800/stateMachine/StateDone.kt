package com.ctyeung.runyasso800.stateMachine

import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import java.lang.reflect.Type

class StateDone : StateAbstract, Idone {

    private var actListener:IRunStatsCallBack
    constructor(listener:IStateCallback,
                actListener: IRunStatsCallBack):super(listener)
    {
        this.actListener = actListener
    }

    override fun execute(previous:Type) {

        this.prevState = previous
        // things to perform in this state

        if(previous != this.javaClass) {
            actListener.onChangedSplit()
            actListener.onHandleYassoDone()
        }
    }

    override fun goto():Boolean {
        /*
         * we are stuck in this state
         * (except if user CLEAR
         */
        return false
    }
}