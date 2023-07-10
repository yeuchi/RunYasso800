package com.ctyeung.runyasso800.viewmodels

import androidx.lifecycle.ViewModel
import com.ctyeung.runyasso800.data.preference.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    storeRepository: StoreRepository
) : ViewModel() {

}