package com.ctyeung.runyasso800.stateMachine

import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.ctyeung.runyasso800.viewModels.RunViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import java.lang.reflect.Type

class StateSprint : MotionState, Isprint, Ijog {
    // 1 iteration = 2 splits (jog + sprint)
    var NUM_SPLITS:Int = Split.DEFAULT_SPLIT_ITERATIONS * 2

    constructor(listener:IStateCallback,
                actListener: IRunStatsCallBack):super(listener, actListener)
    {
        FINISH_DISTANCE = SharedPrefUtility.get(SharedPrefUtility.keySprintLength, Split.DEFAULT_SPLIT_DISTANCE.toInt()).toDouble()
        NUM_SPLITS = SharedPrefUtility.get(SharedPrefUtility.keyNumIterations, Split.DEFAULT_SPLIT_ITERATIONS) * 2
    }

    /*
     * state change conditions
     * - Jog, Sprint or Done
     */
    override fun goto():Boolean {
        val retVal = super.goto()
        if(retVal) {
            changeState()
            return true
        }
        // current state @ Sprint !
        else {
            listener.onChangeState(this.javaClass)
            return false
        }
    }

    /*
     * Check if we are on the 10th split -> DONE !
     * - else, we have more iterations
     */
    fun changeState() {
        // go to Jog !
        var stateType:Type = StateJog::class.java

        if(getSplitIndex() >= NUM_SPLITS)  {
            // done with Yasso !
            stateType = StateDone::class.java
        }

        listener.onChangeState(stateType)
        actListener.onChangedSplit()
    }
}