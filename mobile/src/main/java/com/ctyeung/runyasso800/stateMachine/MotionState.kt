package com.ctyeung.runyasso800.stateMachine

import android.location.Location
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.ctyeung.runyasso800.viewModels.SplitViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import java.lang.reflect.Type
import java.time.LocalTime

abstract class MotionState  : StateAbstract {

    var actListener: IRunStatsCallBack
    var stepViewModel: StepViewModel
    var splitViewModel:SplitViewModel
    var FINISH_DISTANCE = 800
    var split:Split?=null

    constructor(listener:IStateCallback,
                actListener: IRunStatsCallBack,
                splitViewModel:SplitViewModel,
                stepViewModel: StepViewModel) : super(listener)
    {
        this.actListener = actListener
        this.stepViewModel = stepViewModel
        this.splitViewModel = splitViewModel
    }

    override fun execute(previous:Type) {
        this.prevState = previous

        goto()
    }

    /*
     * prevLocation is never null here
     */
    fun setLocation(prevLocation:Location?, location: Location) {
        // insert db new data points & distance when user move more than unit of 1
        val dis: Double = location.distanceTo(prevLocation).toDouble()
        if (dis > 0) {
            // these values should be initialized from database -- not sharedPreference !!!!
            val timeNow = System.currentTimeMillis()

            val runType = getRunType()
            val step = Step(splitViewModel.index,
                            stepViewModel.index ++,
                            dis,
                            runType,
                            timeNow,
                            location.latitude,
                            location.longitude)

            stepViewModel.insert(step)

            // initialize Split
            if(split == null) {
                split = Split(splitViewModel.index,
                    runType,
                    0.0,
                    timeNow,
                    location.latitude,
                    location.longitude,
                    timeNow,
                    location.latitude,
                    location.longitude)

                splitViewModel.insert(split);
            }
            else  {
                split?.update(stepViewModel.totalDistance,timeNow, location.latitude, location.longitude)
                splitViewModel.update(split)

                goto()
            }
            this.actListener.onHandleLocationUpdate()
        }
    }

    /*
     * Return true if state change should occur
     */
    override fun goto():Boolean {
        if(stepViewModel.totalDistance >= FINISH_DISTANCE) {
            incrementSplitIndex()
            splitViewModel.totalDistance += stepViewModel.totalDistance
            stepViewModel.totalDistance = 0.0
            stepViewModel.startTime = System.currentTimeMillis()
            split = null
            return true
        }
        return false
    }

    private fun incrementSplitIndex() {
        if (Split.RUN_TYPE_JOG == getRunType())
            splitViewModel.index ++
    }

    /*
     * Don't need this ... just check parent class
     */
    private fun getRunType():String
    {
        if(this is StateSprint)
            return Split.RUN_TYPE_SPRINT

        else
            return Split.RUN_TYPE_JOG
    }
}