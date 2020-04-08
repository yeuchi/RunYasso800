package com.ctyeung.runyasso800.stateMachine

import android.location.Location
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.RunViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import java.lang.reflect.Type

abstract class MotionState  : StateAbstract {

    var actListener: IRunStatsCallBack
    var stepViewModel: StepViewModel
    var runViewModel:RunViewModel
    var FINISH_DISTANCE = Split.DEFAULT_SPLIT_DISTANCE
    var split:Split?=null
    var hasNewLocation:Boolean = false

    constructor(listener:IStateCallback,
                actListener: IRunStatsCallBack,
                runViewModel:RunViewModel,
                stepViewModel: StepViewModel) : super(listener)
    {
        this.actListener = actListener
        this.stepViewModel = stepViewModel
        this.runViewModel = runViewModel

        /*
         * Refactor this ???!!!!
         * Don't want to reset if we are rotating phone !!!!
         */
        stepViewModel.reset()
        runViewModel.setIndex(0)
    }

    override fun execute(previous:Type) {
        if(hasNewLocation) {
            this.prevState = previous
            update()
        }
    }

    private var prevLocation:Location?=null
    private var location:Location?=null
    /*
     * prevLocation is never null here
     */
    fun setLocation(prevLocation:Location?, location: Location) {
        this.prevLocation = prevLocation
        this.location = location
        hasNewLocation = true
    }

    fun update() {
        hasNewLocation = false
        if(null!=prevLocation && null!=location) {
            // insert db new data points & distance when user move more than unit of 1
            val dis: Double = location!!.distanceTo(prevLocation).toDouble()
            if (dis > 0) {
                // these values should be initialized from database -- not sharedPreference !!!!
                val timeNow = System.currentTimeMillis()

                val runType = getRunType()
                val step = Step(
                    runViewModel.getIndex(),
                    stepViewModel.getNextIndex(),
                    dis,
                    runType,
                    timeNow,
                    location!!.latitude,
                    location!!.longitude
                )

                stepViewModel.insert(step)

                // initialize Split
                if (split == null) {
                    split = Split(
                        runViewModel.getIndex(),
                        runType,
                        0.0,
                        timeNow,
                        location!!.latitude,
                        location!!.longitude,
                        timeNow,
                        location!!.latitude,
                        location!!.longitude
                    )
                    stepViewModel.reset()
                    runViewModel.insert(split);
                } else {
                    split?.update(
                        stepViewModel.totalDistance(),
                        timeNow,
                        location!!.latitude,
                        location!!.longitude
                    )
                    runViewModel.update(split)
                    goto()
                }
            }
        }
    }

    /*
     * Return true if state change should occur
     */
    override fun goto():Boolean {
        if(stepViewModel.totalDistance() >= FINISH_DISTANCE) {
            incrementSplitIndex()
            split = null
            return true
        }
        return false
    }

    private fun incrementSplitIndex() {
        if (Split.RUN_TYPE_SPRINT == getRunType()) {
            val i = runViewModel.getIndex()+1
            runViewModel.setIndex(i)
        }
    }

    private fun getRunType():String
    {
        if(this is StateSprint)
            return Split.RUN_TYPE_SPRINT

        else
            return Split.RUN_TYPE_JOG
    }
}