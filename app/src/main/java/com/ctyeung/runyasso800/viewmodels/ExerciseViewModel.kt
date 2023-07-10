package com.ctyeung.runyasso800.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import com.ctyeung.runyasso800.ExerciseState
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.data.preference.StoreRepository
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    storeRepository: StoreRepository,
    stepRepository: StepRepository,
    splitRepository: SplitRepository
) : ViewModel()  {

    /*
    * Run
    */
    val lat = mutableStateOf<Int>(0)
    val lon = mutableStateOf<Int>(0)

    val currentSplit = mutableStateOf<Int>(0)
    val SplitCount = mutableStateOf<Int>(0)

    val exerciseState = mutableStateOf<ExerciseState>(ExerciseState.IDLE)

    val currentSplitTime = mutableStateOf<Int>(0)
    val totalTime = mutableStateOf<Int>(0)

    val currentSplitDistance = mutableStateOf<Int>(0)
    val totalDistance = mutableStateOf<Int>(0)

    val exerciseStateName : String
        get() {
            return when(exerciseState.value) {
                is ExerciseState.IDLE -> "IDLE"
                is ExerciseState.Run -> "RUN"
                is ExerciseState.Jog -> "JOG"
                is ExerciseState.Pause -> "PAUSE"
                is ExerciseState.Clear -> "Clear"
                is ExerciseState.Pause -> "DONE"
                else -> "Don't Know"
            }
        }

    val exerciseStateIcon : Int
        get() {
            return when(exerciseState.value) {
                is ExerciseState.IDLE -> R.drawable.ic_person_idle
                is ExerciseState.Run -> R.drawable.ic_run
                is ExerciseState.Jog -> R.drawable.ic_jog
                is ExerciseState.Pause -> R.drawable.ic_android
                else -> R.drawable.ic_android
            }
        }

    val exerciseStateColor : androidx.compose.ui.graphics.Color
        get() {
            return when(exerciseState.value) {
                is ExerciseState.IDLE -> Color("#AAAAAA".toColorInt())
                is ExerciseState.Run -> Color("#88FF88".toColorInt())
                is ExerciseState.Jog -> Color("#88FFFF".toColorInt())
                is ExerciseState.Pause -> Color("#FFBB55".toColorInt())
                is ExerciseState.Clear -> Color("#FF0000".toColorInt())
                is ExerciseState.Done -> Color("#8888FF".toColorInt())
                else -> Color("#999999".toColorInt())
            }
        }

}