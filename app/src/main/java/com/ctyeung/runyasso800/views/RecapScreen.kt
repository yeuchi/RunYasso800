package com.ctyeung.runyasso800.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ctyeung.runyasso800.RunViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

/*
 * TODO check out this demo
 *  https://github.com/googlemaps/android-maps-compose/blob/main/app/src/main/java/com/google/maps/android/compose/BasicMapActivity.kt
 */
class RecapScreen(val viewModel: RunViewModel) {

    @Composable
    fun Render() {
        Column(
            // in this column we are specifying modifier
            // and aligning it center of the screen on below lines.
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            /*
             * TODO finish google map
             *  https://developers.google.com/maps/documentation/android-sdk/maps-compose
             */
            val singapore = LatLng(1.35, 103.87)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(singapore, 10f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                drawMakers()
            }
            if (viewModel.showDetailDlg.value) {
                ComposeDetailDialog()
            }
        }
    }

    @Composable
    fun drawMakers() {
        val singapore = LatLng(1.35, 103.87)
        Marker(
            state = MarkerState(position = singapore),
            title = "Singapore",
            snippet = "Marker in Singapore",
            onClick = onMarkerClick
        )
    }

    val onMarkerClick:(marker: Marker)->Boolean = {
        false
    }

    @Composable
    fun ComposeDetailDialog() {
        Dialog(
            onDismissRequest = { viewModel.showDetailDlg.value = false },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Card() {
                Column(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Text(text = "Split:",
                        modifier = Modifier.width(80.dp))
                        Text(text = viewModel.detailSplitIndex.value.toString())
                    }
                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Text(text = "Action:",
                            modifier = Modifier.width(80.dp))
                        Text(text = viewModel.detailAction)
                    }
                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Text(text = "Time:",
                            modifier = Modifier.width(80.dp))
                        Text(text = viewModel.detailTime)
                    }
                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Text(text = "Distance:",
                            modifier = Modifier.width(80.dp))
                        Text(text = "800m")
                    }

                    Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = { viewModel.showDetailDlg.value = false }) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}