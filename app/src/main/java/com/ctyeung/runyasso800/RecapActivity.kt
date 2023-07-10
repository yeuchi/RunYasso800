package com.ctyeung.runyasso800

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ctyeung.runyasso800.RecapActivity.Companion.TAG
import com.ctyeung.runyasso800.viewmodels.RecapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/*
 * Reference:
 * https://github.com/googlemaps/android-maps-compose
 */

@AndroidEntryPoint
class RecapActivity : ComponentActivity() {
    companion object {
        val TAG = "RecapActivity"
    }

    private val viewModel: RecapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreenView()
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun MainScreenView() {
        Scaffold(
            bottomBar = { BottomNavigation(BottomNavItem.Recap.screen_route, this) },
            content = { Render() }
        )
    }

    @Composable
    fun Render() {
        var isMapLoaded by remember { mutableStateOf(true) }

        Box(Modifier.fillMaxSize())
        {
            GoogleMapView(
                modifier = Modifier.matchParentSize(),
                onMapLoaded = {
                    isMapLoaded = true
                },
            )
            if (!isMapLoaded) {
                AnimatedVisibility(
                    modifier = Modifier
                        .matchParentSize(),
                    visible = !isMapLoaded,
                    enter = EnterTransition.None,
                    exit = fadeOut()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .wrapContentSize()
                    )
                }
            }
        }
    }


    @Composable
    fun GoogleMapView(
        modifier: Modifier = Modifier,
        onMapLoaded: () -> Unit = {},
        content: @Composable () -> Unit = {}
    ) {
        if (viewModel.mapVisible.value) {
            GoogleMap(
                modifier = modifier,
                cameraPositionState = viewModel.cameraPositionStateValue,
                properties = viewModel.mapProperties.value,
                uiSettings = viewModel.uiSettings.value,
                onMapLoaded = onMapLoaded,
                onPOIClick = {
                    Log.d(TAG, "POI clicked: ${it.name}")
                }
            ) {
                // Drawing on the map is accomplished with a child-based API
                val markerClick: (Marker) -> Boolean = {
                    Log.d(TAG, "${it.title} was clicked")
                    viewModel.cameraPositionStateValue.projection?.let { projection ->
                        Log.d(TAG, "The current projection is: $projection")
                    }
                    false
                }
                MarkerInfoWindowContent(
                    state = viewModel.markerStateValue,
                    title = "title here",
                    onClick = markerClick,
                    draggable = true,
                ) {
                    // Text(text = it.title ?: "Title", color = Color.RED)
                }
                content()
            }
        }
    }
}


@Composable
private fun MapTypeControls(
    onMapTypeClick: (MapType) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(state = ScrollState(0)),
        horizontalArrangement = Arrangement.Center
    ) {
        MapType.values().forEach {
            MapTypeButton(type = it) { onMapTypeClick(it) }
        }
    }
}

@Composable
private fun MapTypeButton(type: MapType, onClick: () -> Unit) =
    MapButton(text = type.toString(), onClick = onClick)

@Composable
private fun ZoomControls(
    isCameraAnimationChecked: Boolean,
    isZoomControlsEnabledChecked: Boolean,
    onZoomOut: () -> Unit,
    onZoomIn: () -> Unit,
    onCameraAnimationCheckedChange: (Boolean) -> Unit,
    onZoomControlsCheckedChange: (Boolean) -> Unit,
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        MapButton("-", onClick = { onZoomOut() })
        MapButton("+", onClick = { onZoomIn() })
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Camera Animations On?")
            Switch(
                isCameraAnimationChecked,
                onCheckedChange = onCameraAnimationCheckedChange,
                modifier = Modifier.testTag("cameraAnimations"),
            )
            Text(text = "Zoom Controls On?")
            Switch(
                isZoomControlsEnabledChecked,
                onCheckedChange = onZoomControlsCheckedChange
            )
        }
    }
}

@Composable
private fun MapButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier.padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onPrimary,
            contentColor = MaterialTheme.colors.primary
        ),
        onClick = onClick
    ) {
        Text(text = text, style = MaterialTheme.typography.body1)
    }
}

@Composable
private fun DebugView(
    cameraPositionState: CameraPositionState,
    markerState: MarkerState
) {
    Column(
        Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        val moving =
            if (cameraPositionState.isMoving) "moving" else "not moving"
        Text(text = "Camera is $moving")
        Text(text = "Camera position is ${cameraPositionState.position}")
        Spacer(modifier = Modifier.height(4.dp))
        val dragging =
            if (markerState.dragState == DragState.DRAG) "dragging" else "not dragging"
        Text(text = "Marker is $dragging")
        Text(text = "Marker position is ${markerState.position}")
    }
}


//@Preview
//@Composable
//fun GoogleMapViewPreview() {
//    MapsComposeSampleTheme {
//        GoogleMapView(Modifier.fillMaxSize())
//    }
//}