package com.ctyeung.runyasso800.stateMachine

import android.location.Location
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.SplitViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel

abstract class MotionState : StateAbstract() {
    var FINISH_DISTANCE = 800
    var stepDis:Double = 0.0
    var previous:Location ?= null
    var splitIndex:Int = 0
    lateinit var actListener:IRunStatsCallBack
    lateinit var splitViewModel: SplitViewModel
    lateinit var stepViewModel: StepViewModel

    fun setLocation(location: Location) {
        // insert db new data points & distance when user move more than unit of 1
        val dis: Double = location.distanceTo(StateMachine.prevLocation) as Double
        if (dis > 0) {
            // these values should be initialized from database -- not sharedPreference !!!!
            StateMachine.prevLocation = location // will never use prev
            stepDis += dis

            val splitIndex = splitViewModel.yasso.value?.size ?: 0
            val stepIndex = stepViewModel.steps.value?.size ?: 0
            val latitude: Double = StateMachine.prevLocation?.latitude ?: 0.0
            val longitude: Double = StateMachine.prevLocation?.longitude ?: 0.0
            val step = Step(splitIndex,
                            stepIndex,
                            dis,
                            getRunType(splitIndex),
                            System.currentTimeMillis(),
                            latitude,
                            longitude)
            stepViewModel.insert(step)

            /*
             * if larger 800m -> update SplitViewModel and reset stepViewModel
             */

            /*
             * Consider idemtpotency ?
             */
            goto()

            this.actListener.onHandleLocationUpdate(location, splitIndex, stepIndex)
        }
    }

    /*
     * Don't need this ... just check parent class
     */
    private fun getRunType(iteration:Int):String
    {
        if(this is StateSprint)
            return Split.RUN_TYPE_SPRINT

        else
            return Split.RUN_TYPE_JOG
    }
}