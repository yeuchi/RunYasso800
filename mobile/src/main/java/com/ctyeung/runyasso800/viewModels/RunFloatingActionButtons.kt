package com.ctyeung.runyasso800.viewModels

import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.RunActivity
import com.ctyeung.runyasso800.stateMachine.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.reflect.Type

class RunFloatingActionButtons(activity:RunActivity ) {

    // can be managed with binding below
     var btnStart: FloatingActionButton
     var btnPause: FloatingActionButton
     var btnClear: FloatingActionButton
     var btnNext: FloatingActionButton

    init {
        btnStart = activity.findViewById(R.id.btnStart) as FloatingActionButton
        btnPause = activity.findViewById(R.id.btnPause) as FloatingActionButton
        btnClear = activity.findViewById(R.id.btnClear) as FloatingActionButton
        btnNext = activity.findViewById(R.id.btnNext) as FloatingActionButton

        changeState(StateIdle::class.java)
    }

    fun changeState(state: Type){

        when(state) {

            StateSprint::class.java,
            StateJog::class.java,
            StateResume::class.java -> {
                btnPause.show()
                btnStart.hide()
                btnClear.hide()
                btnNext.hide()
            }

            StateDone::class.java,
            StatePause::class.java -> {
                btnStart.show()
                btnClear.show()
                btnNext.show()
                btnPause.hide()
            }

            StateIdle::class.java,
            StateClear::class.java -> {
                btnStart.show()
                btnPause.hide()
                btnClear.hide()
                btnNext.hide()
            }
        }
    }
}