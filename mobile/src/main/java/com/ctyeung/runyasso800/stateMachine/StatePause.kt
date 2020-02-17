package com.ctyeung.runyasso800.stateMachine

object StatePause : StateAbstract(), Ipause, Iresume, Iclear {

    override fun setState(previous:RunState) {

        this.prevState = previous
        // things to perform in this state
    }

    override fun getNextState():RunState {
        /*
         * we are stuck in this state
         * (except if user CLEAR
         */
        when(prevState){
            RunState.Pause -> return RunState.Resume
            else -> return RunState.Pause
        }
    }
}