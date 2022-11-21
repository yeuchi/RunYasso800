package com.ctyeung.runyasso800.viewmodels

import com.ctyeung.runyasso800.data.TestRunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestRunViewModel @Inject constructor(
    runRepository: TestRunRepository
) : RunViewModel(runRepository) {

}