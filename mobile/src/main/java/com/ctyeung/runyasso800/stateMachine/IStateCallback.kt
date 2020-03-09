package com.ctyeung.runyasso800.stateMachine

import java.lang.reflect.Type

interface IStateCallback {
    fun onChangeState(type: Type);
}