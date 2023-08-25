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
                    getString(StoreRepository.CONFIG).collect() {
                        it.let {
                            configData.deserialize(it)
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
            storeRepository.setString(StoreRepository.CONFIG, configData.serialize())
        }
        _event.value = ConfigEvent.Success(configData)
    }

    fun resetFactory() {
        configData = ConfigData()
        updateConfig()
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
) {
    fun serialize() = "${jogDisMeter},${runDisMeter},${loopCount},${sampleRateMilliSec}"

    fun deserialize(str: String) {
        val list = str.split(',')
        if (list.size == 4) {
            jogDisMeter = list[0].toInt()
            runDisMeter = list[1].toInt()
            loopCount = list[2].toInt()
            sampleRateMilliSec = list[3].toInt()
        }
    }
}