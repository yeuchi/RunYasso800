package com.ctyeung.runyasso800.stateMachine

import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.room.splits.SplitRepository
import com.ctyeung.runyasso800.room.steps.StepRepository
import com.ctyeung.runyasso800.viewModels.RunViewModel
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.ctyeung.runyasso800.viewModels.StepViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class StateClear : StateAbstract, Iclear, Iidle {

    var stepRepos:StepRepository
    var splitRepos:SplitRepository

    constructor(listener:IStateCallback):super(listener)
    {
        var context = MainApplication.applicationContext()
        val scope:CoroutineScope = CoroutineScope(Dispatchers.IO)
        val splitDao = YassoDatabase.getDatabase(context, scope).splitDao()
        splitRepos = SplitRepository(splitDao)

        val stepDao = YassoDatabase.getDatabase(context, scope).stepDao()
        stepRepos = StepRepository(stepDao)
    }

    override fun execute(previous:Type) {

        this.prevState = previous

        // things to perform in this state
        clearSplit()
        clearStep()
        goto()
    }

    fun clearStep() {
        CoroutineScope(Dispatchers.IO).launch {stepRepos.clear()}
        SharedPrefUtility.set(SharedPrefUtility.keyStepIndex, 0)
        SharedPrefUtility.set(SharedPrefUtility.keySplitDistance, 0f)
        SharedPrefUtility.set(SharedPrefUtility.keyStepIndex,0)
    }

    fun clearSplit() {
        CoroutineScope(Dispatchers.IO).launch {splitRepos.clear()}
        SharedPrefUtility.set(SharedPrefUtility.keySplitDistance, 0f)
        SharedPrefUtility.set(SharedPrefUtility.keySplitIndex, 0)
    }

    /*
     * go to idle after we clear everything
     */
    override fun goto():Boolean {
        listener.onChangeState(StateIdle::class.java)
        return true
    }
}