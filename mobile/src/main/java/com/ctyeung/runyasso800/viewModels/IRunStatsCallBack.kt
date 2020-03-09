package com.ctyeung.runyasso800.viewModels

import android.location.Location

/*
 * call back from state machine to RunActivity
 */
interface IRunStatsCallBack {
    fun onHandleLocationUpdate();
}