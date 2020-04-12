package com.ctyeung.runyasso800.stateMachine

import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import java.lang.reflect.Type

class StatePause : StateAbstract, Ipause, Iresume, Iclear {

    private var actListener:IRunStatsCallBack
    constructor(listener:IStateCallback,
                actListener: IRunStatsCallBack):super(listener)
    {
        this.actListener = actListener
    }

    override fun execute(state:Type) {
        goto()
        actListener.onChangedSplit()
    }

    override fun goto():Boolean {
        /*
         * we are stuck in this state
         * except if user CLEAR or START
         */
        listener.onChangeState(StatePause::class.java)
        return false
    }
}