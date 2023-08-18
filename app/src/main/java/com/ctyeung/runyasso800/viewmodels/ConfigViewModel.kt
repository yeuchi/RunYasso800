package com.ctyeung.runyasso800.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.data.preference.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    storeRepository: StoreRepository
) : ViewModel() {

    private var configData: ConfigData = ConfigData()
    private var _event = MutableLiveData<ConfigEvent>()
    val event: LiveData<ConfigEvent> = _event

    init {
        initDataStoreEvent()
    }

    private fun initDataStoreEvent() {
        kotlin.runCatching {
            viewModelScope.launch {
                _event.value = ConfigEvent.Success(configData)
            }
        }.onFailure {
            Log.e("ConfigViewModel", it.toString())
        }
    }

    fun updateJogDistance(meters: Int) {
        configData.jogDisMeter = meters
    }

    fun updateRunDistance(meters: Int) {
        configData.runDisMeter = meters
    }

    fun updateLoop(count: Int) {
        configData.loopCount = count
    }

    fun updateSampleRate(time: Int) {
        configData.sampleRateMilliSec = time
    }

    fun updateConfig() {
        /*
         * TODO write to DataStore
         */
        _event.value = ConfigEvent.Success(configData)
    }

    fun resetFactory() {
        this.configData = ConfigData()
        _event.value = ConfigEvent.Success(configData)
    }
}

sealed class ConfigEvent() {
    class Success(val configData: ConfigData) : ConfigEvent()
    object InProgress : ConfigEvent()
    class Error(val msg: String) : ConfigEvent()
}

data class ConfigData(
    var jogDisMeter: Int? = 400,
    var runDisMeter: Int? = 400,
    var loopCount: Int? = 800,
    var sampleRateMilliSec: Int? = 500
)