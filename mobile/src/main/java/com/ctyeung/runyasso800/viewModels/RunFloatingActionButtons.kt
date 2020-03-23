package com.ctyeung.runyasso800.viewModels

import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.RunActivity
import com.ctyeung.runyasso800.stateMachine.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_run.*
import java.lang.reflect.Type

/*
 * To do:
 * 1. use Dagger dependency injection
 */
class RunFloatingActionButtons {

    private var run:RunActivity
    private var stateMachine:StateMachine

    constructor(activity:RunActivity,
                stateMachine: StateMachine) {
        this.run = activity
        this.stateMachine = stateMachine

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