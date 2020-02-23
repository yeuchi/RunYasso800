package com.ctyeung.runyasso800.stateMachine

interface IStateCallback {
    fun onChangeState(state:StateAbstract);
}