package com.ctyeung.runyasso800.stateMachine

import android.location.Location
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.dagger.DaggerComponent
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.splits.SplitRepository
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.room.steps.StepRepository
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.RunViewModel
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.ctyeung.runyasso800.viewModels.StepViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Named

abstract class MotionState  : StateAbstract {
    @Inject
    @field:Named("split") lateinit var splitRepos:SplitRepository
    @Inject
    @field:Named("step") lateinit var stepRepos:StepRepository


    var actListener: IRunStatsCallBack
    var FINISH_DISTANCE = Split.DEFAULT_SPLIT_DISTANCE
    var split:Split?=null
    var hasNewLocation:Boolean = false

    constructor(listener:IStateCallback,
                actListener: IRunStatsCallBack) : super(listener)
    {
        this.actListener = actListener

        /*
         * Refactor this ???!!!!
         * Don't want to reset if we are rotating phone !!!!
         */
    }

    init {
        DaggerComponent.create().injectMotionState(this)
        setSplitIndex(0)
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
            Log.d("MotionState.update()","dis:${dis} prev:(${prevLocation?.latitude},${prevLocation?.longitude}) loc:(${location?.latitude},${location?.longitude})")

            if (dis > 0) {
                // these values should be initialized from database -- not sharedPreference !!!!
                val timeNow = System.currentTimeMillis()

                val runType = getRunType()
                val step = Step(
                    getSplitIndex(),
                    getNextStepIndex(),
                    runType,
                    dis,
                    timeNow,
                    location!!.latitude,
                    location!!.longitude
                )

                insertStep(step)

                // initialize Split
                if (split == null) {
                    split = Split(
                        getSplitIndex(),
                        runType,
                        0.0,
                        timeNow,
                        location!!.latitude,
                        location!!.longitude,
                        timeNow,
                        location!!.latitude,
                        location!!.longitude
                    )
                    insertSplit(split);
                } else {
                    split?.update(
                        getTotalStepDistance(),
                        timeNow,
                        location!!.latitude,
                        location!!.longitude
                    )
                    updateSplit(split)
                    goto()
                }
            }
        }
    }

    /*
     * Return true if state change should occur
     */
    override fun goto():Boolean {
        if(getTotalStepDistance() >= FINISH_DISTANCE) {
            SharedPrefUtility.set(SharedPrefUtility.keySplitDistance, 0f)

            if (Split.RUN_TYPE_SPRINT == getRunType()) {
                SharedPrefUtility.set(SharedPrefUtility.keyStepIndex,0)
            }
            incrementSplitIndex()
            split = null
            return true
        }
        return false
    }

    private fun incrementSplitIndex() {
        val i = getSplitIndex()+1
        setSplitIndex(i)
    }

    private fun getRunType():String
    {
        if(this is StateSprint)
            return Split.RUN_TYPE_SPRINT

        else
            return Split.RUN_TYPE_JOG
    }

    fun getSplitIndex():Int {
        return SharedPrefUtility.get(SharedPrefUtility.keySplitIndex, 0)
    }

    fun setSplitIndex(i:Int) {
        SharedPrefUtility.set(SharedPrefUtility.keySplitIndex, i)
    }

    fun insertSplit(split:Split?) {
        if(split!=null)
            CoroutineScope(Dispatchers.IO).launch {splitRepos.insert(split)}
    }

    fun updateSplit(split:Split?) {
        if(split!=null)
            CoroutineScope(Dispatchers.IO).launch {splitRepos.update(split)}
    }

    fun getNextStepIndex():Int {
        val i = SharedPrefUtility.get(SharedPrefUtility.keyStepIndex, 0)
        SharedPrefUtility.set(SharedPrefUtility.keyStepIndex,i+1)
        return i
    }

    fun getTotalStepDistance():Double {
        return SharedPrefUtility.get(SharedPrefUtility.keySplitDistance, 0f).toDouble()
    }

    fun insertStep(step:Step) {
        val splitDistance = getTotalStepDistance() + step.dis
        SharedPrefUtility.set(SharedPrefUtility.keySplitDistance, splitDistance.toFloat())
        Log.d("MotionState.insertStep()","splitIndex:${step.splitIndex} stepIndex:${step.stepIndex} type:${step.run_type} step:${step.dis} total:${splitDistance}")
        CoroutineScope(Dispatchers.IO).launch {stepRepos.insert(step)}
    }
}