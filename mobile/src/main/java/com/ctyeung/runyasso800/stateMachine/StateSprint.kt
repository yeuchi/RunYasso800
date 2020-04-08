package com.ctyeung.runyasso800.stateMachine

import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.ctyeung.runyasso800.viewModels.RunViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel

class StateSprint : MotionState, Isprint, Ijog {
    var NUM_ITERATIONS:Int = Split.DEFAULT_SPLIT_ITERATIONS

    constructor(listener:IStateCallback,
                actListener: IRunStatsCallBack,
                runViewModel:RunViewModel,
                stepViewModel: StepViewModel):super(listener, actListener, runViewModel, stepViewModel)
    {
        FINISH_DISTANCE = SharedPrefUtility.getDistance(SharedPrefUtility.keySprintDis).toDouble()
        NUM_ITERATIONS = SharedPrefUtility.getNumIterations()
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

        if(runViewModel.getIndex() >= NUM_ITERATIONS)  {
            // done with Yasso !
            listener.onChangeState(StateDone::class.java)
        }
        else {
            // go to Jog !
            listener.onChangeState(StateJog::class.java)
        }
        actListener.onChangedSplit()
    }
}