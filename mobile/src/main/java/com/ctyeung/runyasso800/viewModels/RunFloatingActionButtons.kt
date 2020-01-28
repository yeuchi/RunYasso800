package com.ctyeung.runyasso800.viewModels

import android.content.Context
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.RunActivity
import com.ctyeung.runyasso800.RunState
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

        btnPause.isEnabled = false;
        btnClear.isEnabled = false;
        btnNext.isEnabled = false;
    }
    fun changeState(state:RunState){

        when(state) {
            RunState.Resume -> {
                btnStart.isEnabled = false;
                btnPause.isEnabled = true;
                btnClear.isEnabled = false;
                btnNext.isEnabled = false;
            }

            RunState.Pause -> {
                btnStart.isEnabled = true;
                btnPause.isEnabled = false;
                btnClear.isEnabled = true;
                btnNext.isEnabled = true;
            }

            RunState.Clear -> {
                btnStart.isEnabled = true;
                btnPause.isEnabled = false;
                btnClear.isEnabled = false;
                btnNext.isEnabled = false;
            }
        }
    }
}