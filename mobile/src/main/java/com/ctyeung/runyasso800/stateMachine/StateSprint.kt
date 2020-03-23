package com.ctyeung.runyasso800.stateMachine

import android.location.Location
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.ctyeung.runyasso800.viewModels.SplitViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import java.lang.reflect.Type

class StateSprint : MotionState, Isprint, Ijog {

    constructor(listener:IStateCallback,
                actListener: IRunStatsCallBack,
                splitViewModel:SplitViewModel,
                stepViewModel: StepViewModel):super(listener, actListener, splitViewModel, stepViewModel)
    {
        FINISH_DISTANCE = SharedPrefUtility.getDistance(SharedPrefUtility.keySprintDis)
    }

    /*
     * state change conditions
     * - either in Sprint or Jog
     */
    override fun goto():Boolean {
        val retVal = super.goto()

        if(retVal) {
            listener.onChangeState(StateJog::class.java)
            actListener.onChangedSplit()
            return true
        }
        else {
            // current state @ Sprint !
            listener.onChangeState(this.javaClass)
            return false
        }
        return false
    }
}