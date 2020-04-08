package com.ctyeung.runyasso800.stateMachine

import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.ctyeung.runyasso800.viewModels.RunViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel

class StateJog : MotionState, Ijog, Isprint, Idone {

    constructor(listener:IStateCallback,
                actListener: IRunStatsCallBack,
                runViewModel: RunViewModel,
                stepViewModel: StepViewModel):super(listener, actListener, runViewModel, stepViewModel)
    {
        FINISH_DISTANCE = SharedPrefUtility.getDistance(SharedPrefUtility.keyJogDis).toDouble()
    }

    /*
     * state change conditions
     * - either in Sprint or Jog
     */
    override fun goto():Boolean {
        val retVal = super.goto()

        if(retVal) {
            listener.onChangeState(StateSprint::class.java)
            actListener.onChangedSplit()
            return true
        }
        else {
            // current state @ Jog !
            listener.onChangeState(this.javaClass)
            return false
        }
    }
}