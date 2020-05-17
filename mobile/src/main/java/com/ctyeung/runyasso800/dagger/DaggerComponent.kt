package com.ctyeung.runyasso800.dagger

import com.ctyeung.runyasso800.RunActivity
import com.ctyeung.runyasso800.dagger.SplitModule
import com.ctyeung.runyasso800.stateMachine.MotionState
import com.ctyeung.runyasso800.utilities.LocationUpdateService
import com.ctyeung.runyasso800.viewModels.PersistViewModel
import com.ctyeung.runyasso800.viewModels.ResultViewModel
import com.ctyeung.runyasso800.viewModels.RunViewModel
import dagger.Component

@Component(modules = [SplitModule::class, StepModule::class])
interface Component {
    // repositories
    fun injectRunViewModel(run: RunViewModel)
    fun injectMotionState(state: MotionState)
    fun injectResultViewModel(result:ResultViewModel)
    fun injectPersistViewModel(persist:PersistViewModel)
}

