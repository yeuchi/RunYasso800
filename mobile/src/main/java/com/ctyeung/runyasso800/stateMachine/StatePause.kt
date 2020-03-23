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

    override fun execute(previous:Type) {

        this.prevState = previous
        // things to perform in this state
        goto()

        if(previous != this.javaClass)
            actListener.onChangedSplit()
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
                listener.onChangeState(StatePause::class.java)
                return false
            }
        }
    }
}