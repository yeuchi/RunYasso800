package com.ctyeung.runyasso800.features.run.stateMachine

import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.data.room.YassoDatabase
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import com.ctyeung.runyasso800.storage.SharedPrefUtility
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class RunStates(listener: IStateCallback) : StateAbstract(listener) {

    class Clear : RunStates {
        companion object {
            val tag = "clear"
        }
        var stepRepos: StepRepository
        var splitRepos: SplitRepository

        constructor(listener: IStateCallback) : super(listener) {
            var context = MainApplication.applicationContext()
            val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
            val splitDao = YassoDatabase.getDatabase(context, scope).splitDao()
            splitRepos = SplitRepository(splitDao)

            val stepDao = YassoDatabase.getDatabase(context, scope).stepDao()
            stepRepos = StepRepository(stepDao)
        }

        override fun execute(previous: String) {
            this.prevState = previous

            // things to perform in this state
            CoroutineScope(Dispatchers.IO).launch { stepRepos.deleteAll() }
            SharedPrefUtility.set(SharedPrefUtility.keyStepIndex, 0)
            SharedPrefUtility.set(SharedPrefUtility.keySplitDistance, 0f)
            SharedPrefUtility.set(SharedPrefUtility.keySplitIndex, 0)
            goto()
        }

        // go to idle after we clear everything
        override fun goto(): Boolean {
            listener.onChangeState(RunStates.Idle.tag)
            return true
        }
    }

    class Done : RunStates {
        companion object {
            val tag = "done"
        }
        private var actListener: IRunStatsCallBack

        constructor(
            listener: IStateCallback,
            actListener: IRunStatsCallBack
        ) : super(listener) {
            this.actListener = actListener
        }

        override fun execute(previous: String) {
            this.prevState = previous
            // things to perform in this state

            actListener.onChangedSplit()
            actListener.onHandleYassoDone()
        }

        override fun goto(): Boolean {
            // we are stuck in this state (except if user CLEAR
            return false
        }
    }

    class Error : RunStates {
        companion object {
            val tag = "error"
        }
        constructor(listener: IStateCallback) : super(listener) {}

        override fun execute(previous: String) {
            this.prevState = previous
            // things to perform in this state
        }

        override fun goto(): Boolean {
            // Stuck in error until user CLEAR
            return false
        }
    }

    class Idle : RunStates {
        companion object {
            val tag = "idle"
        }

        var actListener: IRunStatsCallBack

        constructor(
            listener: IStateCallback,
            actListener: IRunStatsCallBack
        ) : super(listener) {
            this.actListener = actListener
        }

        override fun execute(previous: String) {
            this.prevState = previous
            // things to perform in this state
            goto()
        }

        override fun goto(): Boolean {
            // do nothing -- IDLE
            // wait for interrupt to change state

            /*
             * update view
             * update state machine
             */
            listener.onChangeState(RunStates.Idle.tag)
            actListener.onHandleLocationUpdate()
            return false
        }
    }

    class Pause : RunStates {
        companion object {
            val tag = "pause"
        }

        private var actListener: IRunStatsCallBack

        constructor(
            listener: IStateCallback,
            actListener: IRunStatsCallBack
        ) : super(listener) {
            this.actListener = actListener
        }

        override fun execute(state: String) {
            goto()
            actListener.onChangedSplit()
        }

        override fun goto(): Boolean {
            /*
             * we are stuck in this state
             * except if user CLEAR or START
             */
            listener.onChangeState(RunStates.Pause.tag)
            return false
        }
    }

    class Resume : RunStates {
        companion object {
            val tag = "resume"
        }
        private var actListener: IRunStatsCallBack

        constructor(
            listener: IStateCallback,
            actListener: IRunStatsCallBack
        ) : super(listener) {
            this.actListener = actListener
        }

        override fun execute(previous: String) {
            when (previous) {
                RunStates.Resume.tag,
                RunStates.Sprint.tag,
                RunStates.Jog.tag -> {
                    this.prevState = previous
                    // things to perform in this state
                    goto()
                    actListener.onChangedSplit()
                }
                else -> {
                    // error - unsupported
                }
            }
        }

        override fun goto(): Boolean {

            /*
             * SPRINT or JOG ????????
             * !!! implement database query here  !!!!
             */

            // look up sharedPreference or db
            when (prevState) {
                RunStates.Sprint.tag -> listener.onChangeState(RunStates.Sprint.tag)
                RunStates.Jog.tag -> listener.onChangeState(RunStates.Jog.tag)
                else -> listener.onChangeState(RunStates.Error.tag)
            }
            return true
        }
    }

    class Jog : RunStates {
        companion object {
            val tag = "jog"
        }
        constructor(
            listener: IStateCallback,
            actListener: IRunStatsCallBack
        ) : super(listener) {

        }

        override fun execute(previous: String) {
            TODO("Not yet implemented")
        }

        /*
         * state change conditions
         * - either in Sprint or Jog
         */
        override fun goto(): Boolean {
            TODO("Not yet implemented")
        }
    }

    class Sprint : RunStates {
        companion object {
            val tag = "sprint"
        }

        constructor(
            listener: IStateCallback,
            actListener: IRunStatsCallBack
        ) : super(listener) {

        }

        override fun execute(previous: String) {
            TODO("Not yet implemented")
        }

        override fun goto(): Boolean {
            TODO("Not yet implemented")
        }
    }
}