package com.ctyeung.runyasso800.service

/*
 * call back from state machine to RunActivity
 */
interface IRunCallBack {

    // when GPS info updates
    fun onHandleLocationUpdate()

    // we are done running yasso800
    fun onHandleYassoDone()

    // a split is done (switch from sprint/jog)
    fun onChangedSplit()
}