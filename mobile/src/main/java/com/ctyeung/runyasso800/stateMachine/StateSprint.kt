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
     * Acknowledge we are in Sprint state
     */
    override fun execute(previous:Type) {

        this.prevState = previous
        // initialization things to perform in this state

        goto()
    }

    /*
     * state change conditions
     * - either in Sprint or Jog
     */
    override fun goto() {
        when {
            stepTotalDis >= FINISH_DISTANCE -> {

                listener.onChangeState(StateJog::class.java)
            }

            // current state @ Sprint !
            else -> listener.onChangeState(this.javaClass)
        }
    }

}