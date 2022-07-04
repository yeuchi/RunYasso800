package com.ctyeung.runyasso800.features.run.stateMachine

sealed class RunStates {

    object Clear : RunStates() {
        val tag = "clear"
    }

    object Done : RunStates() {
        val tag = "done"
    }

    object Error : RunStates() {
        val tag = "error"
    }

    object Idle : RunStates() {
        val tag = "idle"
    }

    object Pause : RunStates() {
        val tag = "pause"
    }

    object Resume : RunStates() {
        val tag = "resume"
    }

    object Jog : RunStates() {
        val tag = "jog"
    }

    object Sprint : RunStates() {
        val tag = "sprint"
    }
}