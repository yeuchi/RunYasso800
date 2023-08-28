package com.ctyeung.runyasso800.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.data.ExRepoEvent
import com.ctyeung.runyasso800.data.ExerciseData
import com.ctyeung.runyasso800.data.ExerciseState
import com.ctyeung.runyasso800.data.ExeriseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val exeriseRepository: ExeriseRepository
    ) : ViewModel() {

    private var _event = MutableLiveData<ExModelEvent>()
    val event: LiveData<ExModelEvent> = _event

    init {
        initEvent()
    }

    private fun initEvent() {
        kotlin.runCatching {
            viewModelScope.launch {
                exeriseRepository.apply {
                    event.collect() {
                        when(it) {
                            is ExRepoEvent.Idle -> {
                                _event.value = ExModelEvent.Idle(it.exerciseData)
                            }
                            is ExRepoEvent.Run -> {
                                _event.value = ExModelEvent.Run(it.exerciseData)
                            }
                            is ExRepoEvent.Jog -> {
                                _event.value = ExModelEvent.Jog(it.exerciseData)
                            }
                            is ExRepoEvent.Pause -> {
                                _event.value = ExModelEvent.Pause(it.exerciseData)
                            }
                            is ExRepoEvent.Done -> {
                                _event.value = ExModelEvent.Done(it.exerciseData)
                            }
                            else -> {
                                _event.value = ExModelEvent.Error("failed")
                            }
                        }
                    }
                }
            }
        }.onFailure {
            Log.e("ExerciseViewModel failed", it.toString())
            _event.value = ExModelEvent.Error("ExerciseViewModel failed")
        }
    }

    fun  getIcon(state: ExerciseState): Int{
            return when (state) {
                ExerciseState.Idle -> R.drawable.ic_person_idle
                ExerciseState.Run -> R.drawable.ic_run
                ExerciseState.Jog -> R.drawable.ic_jog
                ExerciseState.Pause -> R.drawable.ic_android
                else -> R.drawable.ic_android
            }
        }

    fun getColor(state:ExerciseState): Color{
            return when (state) {
                ExerciseState.Idle -> Color("#AAAAAA".toColorInt())
                ExerciseState.Run -> Color("#88FF88".toColorInt())
                ExerciseState.Jog -> Color("#88FFFF".toColorInt())
                ExerciseState.Pause -> Color("#FFBB55".toColorInt())
                ExerciseState.Clear -> Color("#FF0000".toColorInt())
                ExerciseState.Done -> Color("#8888FF".toColorInt())
                else -> Color("#999999".toColorInt())
            }
        }

    fun startTimer() {
        /*
         * TODO
         *  1. Idle -> run
         *  2. Pause -> run/jog
         */
        viewModelScope.launch {
            exeriseRepository.startTimer()
        }
    }

    fun stopTimer() {
        /*
         * TODO
         *  1. Pause -> Done
         *  2. Run/jog -> Done
         */
        viewModelScope.launch {
            exeriseRepository.stopTimer()
        }
    }

    fun pauseTimer() {
        /*
         * TODO
         *  1. Run/jog -> Pause
         */
        viewModelScope.launch {
            exeriseRepository.pauseTimer()
        }
    }
}

sealed class ExModelEvent() {
    class Idle(val exerciseData: ExerciseData) : ExModelEvent()
    class Run(val exerciseData: ExerciseData) : ExModelEvent()
    class Jog(val exerciseData: ExerciseData) : ExModelEvent()
    class Pause(val exerciseData: ExerciseData) : ExModelEvent()
    class Done(val exerciseData: ExerciseData) : ExModelEvent()
    class Error(val msg: String) : ExModelEvent()
}