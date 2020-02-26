package com.ctyeung.runyasso800.stateMachine

abstract class StateAbstract {

    lateinit var listener:IStateCallback
    var runState:RunState = RunState.Idle
    var prevState:RunState = RunState.Idle

    // the things to do in this state
    abstract fun execute(previous:RunState)

    // which state(s) can we transition to ?
    abstract fun goto()
}