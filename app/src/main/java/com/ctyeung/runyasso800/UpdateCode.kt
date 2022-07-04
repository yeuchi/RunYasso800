package com.ctyeung.runyasso800

/*
 * StateMachine Call back from LocationUpdateService -> RunActivity
 */
enum class UpdateCode {
    DONE,
    CHANGE_SPLIT,
    LOCATION_UPDATE
}