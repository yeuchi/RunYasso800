package com.ctyeung.runyasso800.viewModels

import com.ctyeung.runyasso800.RunActivity
import com.ctyeung.runyasso800.stateMachine.*
import kotlinx.android.synthetic.main.activity_run.*
import java.lang.reflect.Type

/*
 * To do:
 * 1. belong in a view model
 */
class RunFloatingActionButtons {

    private var run:RunActivity

    constructor(activity:RunActivity) {
        this.run = activity
        init()
    }

    fun init() {
        changeState(StateIdle::class.java)
    }

    fun changeState(state: Type){

        when(state) {

            StateSprint::class.java,
            StateJog::class.java,
            StateResume::class.java -> {
                run.btnStart.show()
                run.btnPause.show()
                run.btnStart.hide()
                run.btnClear.hide()
                run.btnNext.hide()
            }

            StateDone::class.java,
            StatePause::class.java -> {
                run.btnStart.show()
                run.btnClear.show()
                run.btnNext.show()
                run.btnPause.hide()
            }

            StateIdle::class.java,
            StateClear::class.java -> {
                run.btnStart.show()
                run.btnPause.hide()
                run.btnClear.hide()
                run.btnNext.hide()
            }
        }
    }
}