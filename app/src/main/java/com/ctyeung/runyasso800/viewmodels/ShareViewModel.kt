package com.ctyeung.runyasso800.viewmodels

import androidx.lifecycle.ViewModel
import com.ctyeung.runyasso800.data.preference.StoreRepository
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    storeRepository: StoreRepository,
    stepRepository: StepRepository,
    splitRepository: SplitRepository
) : ViewModel() {

    /*
     * Share
     */
    var shareHeader:String = ""
    var shareFooter:String = ""
}