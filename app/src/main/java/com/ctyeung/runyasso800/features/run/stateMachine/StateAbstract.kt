package com.ctyeung.runyasso800.features.run.stateMachine

abstract class StateAbstract {
    var listener: IStateCallback
    var prevState: String = RunStates.Idle.tag

    constructor(listener: IStateCallback) {
        this.listener = listener
    }

    // the things to do in this state
    abstract fun execute(previous: String)

    // which state(s) can we transition to ?
    abstract fun goto(): Boolean
}