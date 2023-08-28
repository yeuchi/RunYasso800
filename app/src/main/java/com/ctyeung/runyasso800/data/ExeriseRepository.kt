package com.ctyeung.runyasso800.data

import android.content.Context
import android.os.SystemClock
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExeriseRepository @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val _event =
        MutableStateFlow<RepositoryEvent>(
            RepositoryEvent.TimerStop,
        )
    val event: StateFlow<RepositoryEvent> = _event

    private var startTime: Long = System.currentTimeMillis()

    init {
        _event.value = RepositoryEvent.TimerStop
    }

    private fun loopTimer() {
        when (_event.value) {
            is RepositoryEvent.TimerStop -> {
                // never happends
                _event.value = RepositoryEvent.Error("should not happen")
            }

            is RepositoryEvent.TimerStart,
            is RepositoryEvent.OneSecond -> waitOneSecond()

            is RepositoryEvent.Error -> {
                _event.value = RepositoryEvent.Error("failed")
            }
        }
    }

    private fun waitOneSecond() {
        if(need2Stop) {
            need2Stop = false
        }
        else {
            CoroutineScope(Dispatchers.Default).launch {
                delay(1000)
                val elapsedTime = (System.currentTimeMillis() - startTime) / 1000
                _event.value = RepositoryEvent.OneSecond(elapsedTime)
                loopTimer()
            }
        }
    }

    suspend fun startTimer() {
        startTime = System.currentTimeMillis()
        _event.value = RepositoryEvent.TimerStart
        need2Stop = false
        loopTimer()
    }

    private var need2Stop = false
    suspend fun stopTimer() {
        need2Stop = true
        _event.value = RepositoryEvent.TimerStop
    }
}

sealed class RepositoryEvent() {
    object TimerStart : RepositoryEvent()
    object TimerStop : RepositoryEvent()
    class OneSecond(val elapsed: Long) : RepositoryEvent()
    class Error(val msg: String) : RepositoryEvent()
}