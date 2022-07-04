package com.ctyeung.runyasso800.features.run.stateMachine

import android.location.Location
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack

class StateMachine {
    constructor(actListener: IRunStatsCallBack) {}

    fun interruptStart() {}

    fun interruptPause() {}

    fun interruptClear() {}

    fun update(location: Location) {}
}