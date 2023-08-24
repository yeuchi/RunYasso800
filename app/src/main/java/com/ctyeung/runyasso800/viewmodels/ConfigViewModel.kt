package com.ctyeung.runyasso800.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.data.preference.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    val storeRepository: StoreRepository
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
                storeRepository.apply {
                    getInt(StoreRepository.CONFIG_JOG_DIS).collect() {
                        it?.let {
                            configData.jogDisMeter = it
                        }
                        _event.value = ConfigEvent.Success(configData)
                    }
                    getInt(StoreRepository.CONFIG_RUN_DIS).collect() {
                        it?.let {
                            configData.runDisMeter = it
                        }
                        _event.value = ConfigEvent.Success(configData)
                    }
                    getInt(StoreRepository.CONFIG_LOOP_COUNT).collect() {
                        it?.let {
                            configData.loopCount = it
                        }
                        _event.value = ConfigEvent.Success(configData)
                    }
                    getInt(StoreRepository.CONFIG_SAMPLE_RATE).collect() {
                        it?.let {
                            configData.sampleRateMilliSec = it
                        }
                        _event.value = ConfigEvent.Success(configData)
                    }
                }
            }
        }.onFailure {
            Log.e("ConfigViewModel failed", it.toString())
            _event.value = ConfigEvent.Error("ConfigViewModel failed")
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
        viewModelScope.launch {
            storeRepository.apply {
                configData.jogDisMeter?.let {
                    setInt(StoreRepository.CONFIG_JOG_DIS, it)
                }
                configData.runDisMeter?.let {
                    setInt(StoreRepository.CONFIG_RUN_DIS, it)
                }
                configData.loopCount?.let {
                    setInt(StoreRepository.CONFIG_LOOP_COUNT, it)
                }
                configData.sampleRateMilliSec?.let {
                    setInt(StoreRepository.CONFIG_SAMPLE_RATE, it)
                }
            }
            _event.value = ConfigEvent.Success(configData)
        }
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
    var runDisMeter: Int? = 800,
    var loopCount: Int? = 8,
    var sampleRateMilliSec: Int? = 500
)