package com.ctyeung.runyasso800.stateMachine

import com.ctyeung.runyasso800.viewModels.SplitViewModel
import com.ctyeung.runyasso800.viewModels.StepViewModel
import java.lang.reflect.Type

class StateClear : StateAbstract, Iclear, Iidle {

    var stepViewModel: StepViewModel
    var splitViewModel:SplitViewModel

    constructor(listener:IStateCallback,
                splitViewModel: SplitViewModel,
                stepViewModel: StepViewModel):super(listener)
    {
        this.stepViewModel = stepViewModel
        this.splitViewModel = splitViewModel
    }

    override fun execute(previous:Type) {

        this.prevState = previous

        // things to perform in this state
        splitViewModel.clear()
        stepViewModel.clear()

        goto()
    }

    /*
     * go to idle after we clear everything
     */
    override fun goto():Boolean {
        listener.onChangeState(StateIdle::class.java)
        return true
    }
}