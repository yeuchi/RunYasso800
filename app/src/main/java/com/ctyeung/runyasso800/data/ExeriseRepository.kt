package com.ctyeung.runyasso800.data

import android.content.Context
import com.ctyeung.runyasso800.data.preference.StoreRepository
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExeriseRepository @Inject constructor(
    @ApplicationContext val context: Context,
    storeRepository: StoreRepository,
    stepRepository: StepRepository,
    splitRepository: SplitRepository
    ) {
    private var exData = ExerciseData()

    private val _event =
        MutableStateFlow<ExRepoEvent>(
            ExRepoEvent.Clear(),
        )
    val event: StateFlow<ExRepoEvent> = _event

    private var startTime: Long = System.currentTimeMillis()

    init {
        _event.value = ExRepoEvent.Idle(exData)
    }

    private fun loopTimer() {
        when (_event.value) {
            is ExRepoEvent.Done -> {
                _event.value = ExRepoEvent.Done(exData)
            }

            is ExRepoEvent.Clear -> {
                exData = ExerciseData()
                _event.value = ExRepoEvent.Idle(exData)
            }

            is ExRepoEvent.Pause -> {
                _event.value = ExRepoEvent.Pause(exData)
            }

            is ExRepoEvent.Run,
            is ExRepoEvent.Jog -> waitOneSecond()

//            is ExRepoEvent.Error
            else -> {
                _event.value = ExRepoEvent.Error("failed")
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
                /*
                 * TODO need to diff run vs jog
                 */
                val elapsedTime = (System.currentTimeMillis() - startTime) / 1000
                _event.value = ExRepoEvent.Run(exData)
                loopTimer()
            }
        }
    }

    suspend fun startTimer() {
        /*
         * TODO handle restart after pause ?
         */
        startTime = System.currentTimeMillis()
        _event.value = ExRepoEvent.Run(exData)
        need2Stop = false
        loopTimer()
    }

    private var need2Stop = false
    suspend fun stopTimer() {
        // state -> done
        need2Stop = true
        _event.value = ExRepoEvent.Done(exData)
    }

    suspend fun pauseTimer() {
        _event.value = ExRepoEvent.Pause(exData)
    }
}

sealed class ExRepoEvent() {
    class Idle(val exerciseData: ExerciseData) : ExRepoEvent()
    class Run(val exerciseData: ExerciseData) : ExRepoEvent()
    class Jog(val exerciseData: ExerciseData) : ExRepoEvent()
    class Pause(val exerciseData: ExerciseData) : ExRepoEvent()
    class Done(val exerciseData: ExerciseData) : ExRepoEvent()
    class Clear : ExRepoEvent()
    class Error(val msg: String) : ExRepoEvent()
}

data class ExerciseData(
    var lat: Double? = DEFAULT_LAT,
    var lon: Double? = DEFAULT_LON,
    var split: Int? = DEFAULT_SPLIT,
    var splitTargetTotal: Int? = DEFAULT_SPLIT_TOTAL,
    var state: ExerciseState? = DEFAULT_STATE,
    var time: Long? = DEFAULT_TIME,
    var timeTotal: Long? = DEFAULT_TIME_TOTAL,
    var distance: Long? = DEFAULT_DIS,
    var distanceTotal: Long? = DEFAULT_DIS_TOTAL
) {
    companion object {
        const val DEFAULT_LAT = 0.0
        const val DEFAULT_LON = 0.0
        const val DEFAULT_SPLIT = 0
        const val DEFAULT_SPLIT_TOTAL = 10
        val DEFAULT_STATE = ExerciseState.Idle
        const val DEFAULT_TIME = 0L
        const val DEFAULT_TIME_TOTAL = 0L
        const val DEFAULT_DIS = 0L
        const val DEFAULT_DIS_TOTAL = 0L
    }
    fun serialize() = "${lat},${lon},${split},${splitTargetTotal},${state},${time},${timeTotal},${distance},${distanceTotal}"

    fun deserialize(str: String) {
        val list = str.split(',')
        if (list.size == 9) {
            lat = list[0].toDouble()
            lon = list[1].toDouble()
            split = list[2].toInt()
            splitTargetTotal = list[3].toInt()
            state = ExerciseState.valueOf(list[4])
            time = list[5].toLong()
            timeTotal = list[6].toLong()
            time = list[7].toLong()
            timeTotal = list[8].toLong()
        }
    }
}

enum class ExerciseState {
    Idle,    // default IDLE -> Run / Jog
    Run,     // active Run -> Pause / Jog
    Jog,     // active Jog -> Pause / Run
    Pause,   // interrupt Pause -> Resume -> Run / Jog
    Clear,    // Clear -> IDLE
    Done,    // DONE
}