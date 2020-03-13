package com.ctyeung.runyasso800.stateMachine

import android.location.Location
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.ctyeung.runyasso800.viewModels.SplitViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import java.lang.reflect.Type

class StateJog : MotionState, Ijog, Isprint, Idone {

    constructor(listener:IStateCallback,
                actListener: IRunStatsCallBack,
                splitViewModel: SplitViewModel,
                stepViewModel: StepViewModel):super(listener, actListener, splitViewModel, stepViewModel)
    {
        FINISH_DISTANCE = SharedPrefUtility.getDistance(SharedPrefUtility.keyJogDis)
    }

    /*
     * Acknowledge we are in Jog state
     */
    override fun execute(previous:Type) {

        this.prevState = previous
        // things to perform in this state

        goto()
    }

    /*
     * state change conditions
     * - Jog, Sprint or Done
     */
    override fun goto() {

        when {
            stepTotalDis >= FINISH_DISTANCE -> changeState()

            // current state @ Jog !
            else -> listener.onChangeState(this.javaClass)
        }
    }

    /*
     * Check if we are on the 10th split -> DONE !
     * - else, we have more iterations
     */
    fun changeState() {

        when(splitIndex) {

            10 -> {
                listener.onChangeState(StateDone::class.java)
            }
            else -> {
                listener.onChangeState(StateSprint::class.java)
            }
        }
    }
}