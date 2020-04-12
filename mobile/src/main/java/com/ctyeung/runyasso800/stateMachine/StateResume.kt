package com.ctyeung.runyasso800.stateMachine

import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import java.lang.reflect.Type

class StateResume : StateAbstract, Iresume, Isprint, Ijog {
    private var actListener: IRunStatsCallBack

    constructor(listener:IStateCallback,
                actListener: IRunStatsCallBack):super(listener)
    {
        this.actListener = actListener
    }

    override fun execute(previous:Type) {

        this.prevState = previous
        // things to perform in this state
        goto()
        actListener.onChangedSplit()
    }

    override fun goto():Boolean {

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
        return true
    }
}