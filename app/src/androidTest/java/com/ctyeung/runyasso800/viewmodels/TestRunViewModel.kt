package com.ctyeung.runyasso800.viewmodels

import androidx.lifecycle.ViewModel
import com.ctyeung.runyasso800.data.RunRepository
import com.ctyeung.runyasso800.data.TestRunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestRunViewModel @Inject constructor(
    runRepository: TestRunRepository
) : RunViewModel(runRepository) {

}