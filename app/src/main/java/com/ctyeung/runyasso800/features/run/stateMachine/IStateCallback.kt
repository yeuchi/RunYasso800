package com.ctyeung.runyasso800.features.run.stateMachine

interface IStateCallback {
    fun onChangeState(type: String);
}