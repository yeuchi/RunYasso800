package com.ctyeung.runyasso800

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class ExerciseScreen(val viewModel: RunViewModel) {
    /*
     * TODO consolidate all styling into theme
     */
    @Composable
    fun Render() {
        Column(
            // in this column we are specifying modifier
            // and aligning it center of the screen on below lines.
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
                .padding(15.dp)
                .clickable { },
            elevation = 10.dp
        ) {
            Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "location",
                    modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                )
                Text(
                    text = "lat:",
                    modifier = Modifier
                        .width(50.dp)
                        .padding(10.dp, 10.dp, 10.dp, 10.dp)
                )
                Text(
                    text = viewModel.lat.value.toString(),
                    modifier = Modifier
                        .width(80.dp)
                        .padding(10.dp, 10.dp, 10.dp, 10.dp)
                )
                Text(
                    text = "lon:",
                    modifier = Modifier
                        .width(50.dp)
                        .padding(10.dp, 10.dp, 10.dp, 10.dp)
                )
                Text(
                    text = viewModel.lon.value.toString(),
                    modifier = Modifier
                        .width(80.dp)
                        .padding(10.dp, 10.dp, 10.dp, 10.dp)
                )
            }
        }
    }

    @Composable
    fun ComposeDetail() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable { },
            elevation = 10.dp
        ) {
            Column(modifier = Modifier.background(viewModel.exerciseStateColor)) {

                Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                    Image(
                        painter = painterResource(id = viewModel.exerciseStateIcon),
                        contentDescription = "exercise",
                        modifier = Modifier
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                            .height(30.dp)
                    )
                    Text(
                        text = "Split:",
                        modifier = Modifier
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                            .width(70.dp)
                    )
                    Text(
                        text = viewModel.currentSplit.value.toString(),
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                    Text(
                        text = "Total:",
                        modifier = Modifier
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                            .width(70.dp)
                    )
                    Text(
                        text = viewModel.SplitCount.value.toString(),
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                }

                Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_assignment),
                        contentDescription = "assignment",
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                    Text(
                        text = viewModel.exerciseStateName,
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                }

                Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_access_time),
                        contentDescription = "Time",
                        modifier = Modifier
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                    Text(
                        text = "Time:", modifier = Modifier
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                            .width(70.dp)
                    )
                    /*
                     * TODO Formater time
                     */
                    Text(
                        text = viewModel.currentSplitTime.value.toString(),
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                    Text(
                        text = "Total:",
                        modifier = Modifier
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                            .width(70.dp)
                    )
                    Text(
                        text = viewModel.totalTime.value.toString(),
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                }

                Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_distance),
                        contentDescription = "distance",
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                    Text(
                        text = "Distance:",
                        modifier = Modifier
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                            .width(70.dp)
                    )
                    Text(
                        text = viewModel.currentSplitDistance.value.toString(),
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                    Text(
                        text = "Total:",
                        modifier = Modifier
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                            .width(70.dp)
                    )
                    Text(
                        text = viewModel.totalDistance.value.toString(),
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun ComposeFABs() {
        Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {

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

