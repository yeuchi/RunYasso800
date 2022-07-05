package com.ctyeung.runyasso800.viewModels

import android.location.Location

/*
 * call back from state machine to RunActivity
 */
interface IRunStatsCallBack {

    // when GPS info updates
    fun onHandleLocationUpdate()

    // we are done running yasso800
    fun onHandleYassoDone()

    // a split is done (switch from sprint/jog)
    fun onChangedSplit()
}