package com.ctyeung.runyasso800.viewmodels

import androidx.lifecycle.ViewModel
import com.ctyeung.runyasso800.data.RunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(
    runRepository: RunRepository
) : ViewModel() {

}