package com.ctyeung.runyasso800

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.ctyeung.runyasso800.data.ExerciseData
import com.ctyeung.runyasso800.data.ExerciseState
import com.ctyeung.runyasso800.ui.theme.RunYasso800Theme
import com.ctyeung.runyasso800.viewmodels.ConfigData
import com.ctyeung.runyasso800.viewmodels.ConfigEvent
import com.ctyeung.runyasso800.viewmodels.ExModelEvent
import com.ctyeung.runyasso800.viewmodels.ExerciseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseActivity : ComponentActivity() {
    private val viewModel: ExerciseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.event.observe(this, Observer(::onEventChange))
    }

    private fun onEventChange(event: ExModelEvent) {
        setContent {
            RunYasso800Theme {
                when (event) {
                    is ExModelEvent.Idle -> ComposeScreen(event.exerciseData)

                    is ExModelEvent.Run -> ComposeScreen(event.exerciseData)

                    is ExModelEvent.Jog -> ComposeScreen(event.exerciseData)

                    is ExModelEvent.Pause -> ComposeScreen(event.exerciseData)

                    is ExModelEvent.Done -> ComposeScreen(event.exerciseData)

                    is ExModelEvent.Error -> ComposeError(error = event.msg)
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun ComposeScreen(exData: ExerciseData) {
        Scaffold(
            bottomBar = { BottomNavigation(BottomNavItem.Run.screen_route, this) },
            content = { Render(exData) }
        )
    }

    @Composable
    fun Render(exData: ExerciseData) {
        Column(
            // in this column we are specifying modifier
            // and aligning it center of the screen on below lines.
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ComposeLatLon(exData)
            ComposeDetail(exData)
            ComposeFABs(exData)
        }
    }

    @Composable
    fun ComposeStateIdle() {
        // initial state after cleared
    }

    @Composable
    fun ComposeStateRun() {
    }

    @Composable
    fun ComposeStateJog() {
    }

    @Composable
    fun ComposeStatePause() {
        // run or jog -> paused
    }

    @Composable
    fun ComposeStateClear() {
        // never seen in UI - a transition state in state machine
    }

    @Composable
    fun ComposeStateDone() {
        // run or jog -> done
    }

    @Composable
    fun ComposeLatLon(exData: ExerciseData) {
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
                    text = exData.lat.toString(),
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
                    text = exData.lon.toString(),
                    modifier = Modifier
                        .width(80.dp)
                        .padding(10.dp, 10.dp, 10.dp, 10.dp)
                )
            }
        }
    }

    @Composable
    fun ComposeDetail(exData: ExerciseData) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable { },
            elevation = 10.dp
        ) {
            val state = exData.state?:ExerciseState.Idle
            Column(modifier = androidx.compose.ui.Modifier.background(viewModel.getColor(state))) {

                Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                    Image(
                        painter = painterResource(id = viewModel.getIcon(state)),
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
                        text = exData.split.toString(),
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                    Text(
                        text = "Total:",
                        modifier = Modifier
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                            .width(70.dp)
                    )
                    Text(
                        text = exData.splitTargetTotal.toString(),
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
                        text = state.toString(),
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
                        text = exData.time.toString(),
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                    Text(
                        text = "Total:",
                        modifier = Modifier
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                            .width(70.dp)
                    )
                    Text(
                        text = exData.time.toString(),
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
                        text = exData.distance.toString(),
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                    Text(
                        text = "Total:",
                        modifier = Modifier
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                            .width(70.dp)
                    )
                    Text(
                        text = exData.distanceTotal.toString(),
                        modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun ComposeFABs(exData: ExerciseData) {
        Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {

            when (exData.state) {
                ExerciseState.Idle -> {
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

                ExerciseState.Run,
                ExerciseState.Jog -> {
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

                ExerciseState.Pause -> {
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