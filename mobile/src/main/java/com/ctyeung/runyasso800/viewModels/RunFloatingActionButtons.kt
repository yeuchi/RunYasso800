package com.ctyeung.runyasso800.viewModels

import android.opengl.Visibility
import android.view.View
import androidx.core.view.isVisible
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.RunActivity
import com.ctyeung.runyasso800.stateMachine.RunState
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RunFloatingActionButtons(activity:RunActivity ) {

    // can be managed with binding below
    lateinit var btnStart: FloatingActionButton
    lateinit var btnPause: FloatingActionButton
    lateinit var btnClear: FloatingActionButton
    lateinit var btnNext: FloatingActionButton

    init {
        btnStart = activity.findViewById(R.id.btnStart) as FloatingActionButton
        btnPause = activity.findViewById(R.id.btnPause) as FloatingActionButton
        btnClear = activity.findViewById(R.id.btnClear) as FloatingActionButton
        btnNext = activity.findViewById(R.id.btnNext) as FloatingActionButton

        changeState(RunState.Idle)
    }

    fun changeState(state: RunState){

        when(state) {

            RunState.Sprint,
            RunState.Jog,
            RunState.Resume -> {
                btnPause.show()
                btnStart.hide()
                btnClear.hide()
                btnNext.hide()
            }

            RunState.Pause -> {
                btnStart.show()
                btnClear.show()
                btnNext.show()
                btnPause.hide()
            }

            RunState.Idle,
            RunState.Clear -> {
                btnStart.show()
                btnPause.hide()
                btnClear.hide()
                btnNext.hide()
            }
        }
    }
}