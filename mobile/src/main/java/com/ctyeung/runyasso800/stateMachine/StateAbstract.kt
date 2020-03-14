package com.ctyeung.runyasso800.stateMachine

import java.lang.reflect.Type

abstract class StateAbstract {

    var listener:IStateCallback
    var prevState:Type = StateIdle::class.java

    constructor(listener:IStateCallback){
        this.listener = listener
    }

    // the things to do in this state
    abstract fun execute(previous:Type)

    // which state(s) can we transition to ?
    abstract fun goto():Boolean
}