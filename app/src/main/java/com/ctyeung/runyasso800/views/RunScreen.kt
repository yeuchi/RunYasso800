package com.ctyeung.runyasso800

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ctyeung.runyasso800.RunViewModel

class RunScreen(val viewModel: RunViewModel) {

    @Composable
    fun Render() {
        Column(
            // in this column we are specifying modifier
            // and aligning it center of the screen on below lines.
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // in this column we are specifying the text
            Text(
                // on below line we are specifying the text message
                text = "RunScreen",

                // on below line we are specifying the text style.
                style = MaterialTheme.typography.h5,

                // on below line we are specifying the text color
                color = Color.Green,

                // on below line we are specifying the font weight
                fontWeight = FontWeight.Bold,

                //on below line we are specifying the text alignment.
                textAlign = TextAlign.Center
            )

            ComposeLatLon()
            ComposeDetail()
            ComposeFABs()
        }
    }

    @Composable
    fun ComposeLatLon() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(15.dp)
                .clickable { },
            elevation = 10.dp
        ) {
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "location"
                )
                Text(text = "lat:")
                Text(text = viewModel.lat.value.toString())
                Text(text = "lon:")
                Text(text = viewModel.lon.value.toString())
            }
        }
    }

    @Composable
    fun ComposeDetail() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(15.dp)
                .clickable { },
            elevation = 10.dp
        ) {
            Column(modifier = Modifier.background(viewModel.exerciseStateColor)) {

                Row() {
                    Image(
                        painter = painterResource(id = viewModel.exerciseStateIcon),
                        contentDescription = "exercise"
                    )
                    Text(text = "Split:")
                    Text(text = viewModel.currentSplit.value.toString())
                    Text(text = "Total:")
                    Text(text = viewModel.SplitCount.value.toString())
                }

                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.ic_assignment),
                        contentDescription = "assignment"
                    )
                    Text(text = viewModel.exerciseStateName)
                }

                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.ic_access_time),
                        contentDescription = "Time"
                    )
                    Text(text = "Time:")
                    /*
                     * TODO Formater time
                     */
                    Text(text = viewModel.currentSplitTime.value.toString())
                    Text(text = "Total:")
                    Text(text = viewModel.totalTime.value.toString())
                }

                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.ic_distance),
                        contentDescription = "distance"
                    )
                    Text(text = "Distance:")
                    Text(text = viewModel.currentSplitDistance.value.toString())
                    Text(text = "Total:")
                    Text(text = viewModel.totalDistance.value.toString())
                }
            }
        }
    }

    @Composable
    fun ComposeFABs() {
        Row() {

            when (viewModel.exerciseState.value) {
                is ExerciseState.IDLE -> {
                    FloatingActionButton(onClick = {
                    /*
                     * TODO PLAY
                     */
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_play_arrow),
                            contentDescription = "Play"
                        )
                    }
                }
                is ExerciseState.Run,
                is ExerciseState.Jog -> {
                    FloatingActionButton(onClick = {
                        /*
                         * TODO PAUSE
                         */
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_pause),
                            contentDescription = "Play"
                        )
                    }
                }
                is ExerciseState.Pause -> {
                    FloatingActionButton(onClick = {
                        /*
                         * TODO PLAY
                         */
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_play_arrow),
                            contentDescription = "Play"
                        )
                    }

                    FloatingActionButton(onClick = {
                        /*
                         * TODO STOP
                         */
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_stop),
                            contentDescription = "Stop"
                        )
                    }

                    FloatingActionButton(onClick = {
                        /*
                         * TODO DONE
                         */
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_navigate_next),
                            contentDescription = "Done"
                        )
                    }
                }
                else -> {

                }
            }
        }
    }
}

