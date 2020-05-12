package com.ctyeung.runyasso800.dagger

import com.ctyeung.runyasso800.dagger.SplitModule
import com.ctyeung.runyasso800.stateMachine.MotionState
import com.ctyeung.runyasso800.viewModels.PersistViewModel
import com.ctyeung.runyasso800.viewModels.ResultViewModel
import com.ctyeung.runyasso800.viewModels.RunViewModel
import dagger.Component

@Component(modules = [SplitModule::class, StepModule::class])
interface RepositoryComponent {
    fun injectRunViewModelRepository(run: RunViewModel)
    fun injectMotionStateRepository(state: MotionState)
    fun injectResultViewModelRepository(result:ResultViewModel)
    fun injectPersistViewModelRepository(persist:PersistViewModel)
}