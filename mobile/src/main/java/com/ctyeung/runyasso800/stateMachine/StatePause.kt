package com.ctyeung.runyasso800.stateMachine

object StatePause : StateAbstract(), Ipause, Iresume, Iclear {

    override fun execute(previous:RunState) {

        this.prevState = previous
        // things to perform in this state

        goto()
    }

    override fun goto() {
        /*
         * we are stuck in this state
         * (except if user CLEAR
         */
        when(prevState){
            RunState.Pause -> listener.onChangeState(StateResume)
            else -> {
                // do nothing -- stay in pause
            }
        }
    }
}