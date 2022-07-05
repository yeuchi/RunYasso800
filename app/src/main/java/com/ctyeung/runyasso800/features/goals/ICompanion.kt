package com.ctyeung.runyasso800.features.goals

interface ICompanion {
    fun isAvailable():Boolean {
        return false
    }

    fun isCompleted():Boolean {
        return false
    }
}