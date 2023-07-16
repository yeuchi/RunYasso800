package com.ctyeung.runyasso800.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctyeung.runyasso800.data.preference.StoreRepository
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecapViewModel  @Inject constructor(
    storeRepository: StoreRepository,
    stepRepository: StepRepository,
    splitRepository: SplitRepository
) : ViewModel() {

    var uiSettings = mutableStateOf(MapUiSettings(compassEnabled = false))
    var mapProperties = mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    var mapVisible = mutableStateOf(true)
    var myPlace: LatLng = LatLng(2.38, 103.97)
    var zoom:Float = 3f

    private val _event = MutableLiveData<MapEvent>()
    val event: LiveData<MapEvent> = _event

    private var markerState = mutableStateOf(MarkerState(myPlace))
    val markerStateValue: MarkerState
        get() {
            return markerState.value
        }

    private var cameraPositionState = mutableStateOf(
        CameraPositionState(
            CameraPosition.fromLatLngZoom(
                myPlace,
                zoom,
            ),
        ),
    )
    val cameraPositionStateValue: CameraPositionState
        get() {
            return cameraPositionState.value
        }

    fun updateMyPlace(place: LatLng?) {
        place?.let {
            myPlace = place
            markerState.value = MarkerState(myPlace)
            cameraPositionState.value = CameraPositionState(
                CameraPosition.fromLatLngZoom(
                    myPlace,
                    zoom,
                ),
            )
        }
    }

    init {
        initDbListener()
    }

    private fun initDbListener() {
       // _event.value = MapEvent.InProgress
        _event.value = MapEvent.Success(null)

        kotlin.runCatching {
            viewModelScope.launch {
                _event.value = MapEvent.Success(null)

//                myPlaceRepository.allPlaces.collect {
//                    var hasLocation = false
//                    for (place in it) {
//                        if (place.myLoc) {
//                            hasLocation = true
//                            _event.value = MapEvent.Success(place)
//                        }
//                    }
//                    if(!hasLocation) {
//                        // API response with place of no location
//                        _event.value = MapEvent.Success(null)
//                    }
//
//                    // initialization only
//                    this.cancel()
//                }
            }
        }.onFailure {
            _event.value = MapEvent.Error(it.toString())
            Log.e("LocationMapModel.initEventListener", it.toString())
        }
    }

    fun invalidate(newSelectPlace: LatLng) {
        _event.value = MapEvent.Success(newSelectPlace)
    }
}

sealed class MapEvent() {
    object InProgress : MapEvent()
    class Success(val myPlace: LatLng?) : MapEvent()
    class Confirm(val myPlace: LatLng?) : MapEvent()
    class Error(val msg: String) : MapEvent()
}