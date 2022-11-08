package com.ctyeung.runyasso800.viewmodels

import androidx.lifecycle.ViewModel
import com.ctyeung.runyasso800.data.RunRepository
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class RunViewModel @Inject constructor(
    runRepository: RunRepository
) : ViewModel() {
    val ENTER_TIME = "Enter Time"

    var runName:String = ""
    var goal800m:String = "choose"
    var goalMarathon:String = ENTER_TIME
}