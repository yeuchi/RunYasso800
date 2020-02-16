package com.ctyeung.runyasso800.stateMachine

abstract class state {

    var runState:RunState = RunState.Idle
    var prevState:RunState = RunState.Idle

    // the things to do in this state
    abstract fun setState(previous:RunState)

    // which state(s) can we transition to ?
    abstract fun getNextState():RunState
}