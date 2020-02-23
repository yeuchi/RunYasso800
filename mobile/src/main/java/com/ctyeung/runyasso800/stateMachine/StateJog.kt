package com.ctyeung.runyasso800.stateMachine

import android.location.Location

object StateJog : MotionState(), Ijog, Isprint, Idone {

    override fun execute(previous:RunState) {

        this.prevState = previous
        // things to perform in this state

        goto()
    }

    override fun goto() {

        when {
            stepDis >= FINISH_DISTANCE -> changeState()

            else -> {
                // do nothing -- keep jogging
            }
        }
    }

    fun changeState() {
        when(getIteration()) {

            10 -> {
                listener.onChangeState(StateDone)
            }
            else -> {
                listener.onChangeState(StateSprint)
            }
        }
    }

    fun getIteration():Int {
        // fetch count from persistence
        return 0
    }

}