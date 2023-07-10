package com.ctyeung.runyasso800.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ctyeung.runyasso800.data.preference.StoreRepository
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecapViewModel  @Inject constructor(
    storeRepository: StoreRepository,
    stepRepository: StepRepository,
    splitRepository: SplitRepository
) : ViewModel() {
    /*
     * Recap
     */

    var uiSettings = mutableStateOf(MapUiSettings(compassEnabled = false))
    var mapProperties = mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    var mapVisible = mutableStateOf(true)
    var myPlace: LatLng = LatLng(2.38, 103.97)
    var zoom:Float = 3f

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

}