package com.ctyeung.runyasso800.stateMachine

import android.location.Location
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.SplitViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel

abstract class MotionState : StateAbstract() {
    var stepDis:Double = 0.0
    var previous:Location ?= null
    var splitIndex:Int = 0
    lateinit var listener:IRunStatsCallBack
    lateinit var splitViewModel: SplitViewModel
    lateinit var stepViewModel: StepViewModel

    fun setLocation(location: Location) {
        // insert db new data points & distance when user move more than unit of 1
        val dis: Float = location.distanceTo(StateMachine.prevLocation)
        if (dis > 0) {
            // these values should be initialized from database -- not sharedPreference !!!!
            StateMachine.prevLocation = location // will never use prev

            /*
             * should this stuff be under state machine ??
             */
            val splitIndex = splitViewModel.yasso.value?.size ?: 0
            val stepIndex = stepViewModel.steps.value?.size ?: 0
            val latitude: Double = StateMachine.prevLocation?.latitude ?: 0.0
            val longitude: Double = StateMachine.prevLocation?.longitude ?: 0.0
            val step = Step(
                splitIndex,
                stepIndex,
                getRunType(splitIndex),
                System.currentTimeMillis(),
                latitude,
                longitude
            )
            stepViewModel.insert(step, dis.toDouble())

            this.listener.onHandleLocationUpdate(location, splitIndex, stepIndex)
        }
    }

    /*
     * Don't need this ... just check parent class
     */
    private fun getRunType(iteration:Int):String
    {
        if(0==iteration%2)
            return Split.RUN_TYPE_SPRINT

        else
            return Split.RUN_TYPE_JOG
    }
}