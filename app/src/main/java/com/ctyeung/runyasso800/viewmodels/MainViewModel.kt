package com.ctyeung.runyasso800

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import com.ctyeung.runyasso800.data.preference.StoreRepository
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor(
    storeRepository: StoreRepository,
    stepRepository: StepRepository,
    splitRepository: SplitRepository
) : ViewModel() {

    /*
     * Recap
     */
    val showDetailDlg = mutableStateOf<Boolean>(false)
    val detailSplitIndex = mutableStateOf<Int>(0)
    val detailAction:String
    get() {
        /*
         * Retrieve Run or Jog Action time from database
         */
        return "Run"
    }
    val detailTime:String
        get() {
            /*
             * Retrieve elapse time from database
             */
            return "00:00:30"
        }

}

data class LatLong(val lat:Double, val lon:Double)

