package com.ctyeung.runyasso800

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.ctyeung.runyasso800.ui.theme.RunYasso800Theme
import com.ctyeung.runyasso800.viewmodels.GoalData
import com.ctyeung.runyasso800.viewmodels.GoalEvent
import com.ctyeung.runyasso800.viewmodels.GoalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalActivity : ComponentActivity() {
    private val viewModel: GoalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.event.observe(this, Observer(::onEventChange))
    }

    private fun onEventChange(event: GoalEvent) {
        setContent {
            RunYasso800Theme {
                when (event) {
                    is GoalEvent.InProgress -> ComposeSpinner()
                    is GoalEvent.Success -> ComposeScreen(event.goalData)
                    is GoalEvent.Error -> ComposeError(error = event.msg)
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun ComposeScreen(goalData: GoalData) {
        Scaffold(
            bottomBar = { BottomNavigation(BottomNavItem.Goal.screen_route, this) },
            content = { Render(goalData) }
        )
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Render(goalData: GoalData) {
        Column(
            // in this column we are specifying modifier
            // and aligning it center of the screen on below lines.
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            goalData.apply {
                ComposeName(name ?: GoalData.DEFAULT_NAME)
                ComposeMarathonGoal(goalMarathonInSeconds ?: GoalData.DEFAULT_MARATHON_GOAL)
                Compose800mGoal(goal800mCalculated ?: GoalData.DEFAULT_800M_GOAL)
                ComposeButtons()
            }
        }
    }

    @Composable
    fun ComposeButtons() {
        Row(
            modifier = Modifier
                .padding(10.dp, 10.dp, 10.dp, 10.dp)
            //.align(Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = {
                    viewModel.resetFactory()
                },
                modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
            ) {
                Text(text = "Factory Reset")
            }

            val focusMgr = LocalFocusManager.current
            Button(
                onClick = {
                    viewModel.updateGoal()
                    focusMgr.clearFocus(true)
                },
                modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
            ) {
                Text(text = "Save")
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ComposeName(name: String) {
        val keyboardController = LocalSoftwareKeyboardController.current

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(15.dp)
                .clickable { },
            elevation = 10.dp
        ) {
            Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                Text(text = "Run name")

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterVertically)
                ) {
                    val nameString = remember {
                        mutableStateOf(
                            TextFieldValue(
                                text = name,
                                selection = TextRange(name.length),
                            ),
                        )
                    }

                    TextField(modifier = Modifier
                        .padding(0.dp, 30.dp)
                        .align(Alignment.Center),
                        value = nameString.value,
                        onValueChange = {
                            if (it.text.contains('\n')) {
                                val newName = it.text.replace("\n", "")
                                keyboardController?.hide()
                                viewModel.updateName(newName)
                            } else {
                                nameString.value = it
                            }
                        },
                        label = { Text("My first run") }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ComposeMarathonGoal(goalMarathonInSec: Long) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(15.dp)
                .clickable { },
            elevation = 10.dp
        ) {
            // Declaring and initializing a calendar
            val timeInMinutes = goalMarathonInSec / 60
            val hr = (timeInMinutes / 60).toInt()
            val min = (timeInMinutes % 60).toInt()
            // Value for storing time as a string
            val mTime = remember {
                mutableStateOf(
                    String.format("%02d", hr)
                            + ":" +
                            String.format("%02d", min)
                )
            }

            // Creating a TimePicker dialod
            val mTimePickerDialog = TimePickerDialog(
                LocalContext.current,
                { _, mHour: Int, mMinute: Int ->
                    mTime.value = "$hr:$min"
                },
                hr, min, true,
            )

            Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                Text(text = "Marathon Goal (HH:MM)")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterVertically)
                ) {
                    Button(modifier = Modifier
                        .padding(0.dp, 30.dp)
                        .align(Alignment.Center),
                        onClick = {
                            mTimePickerDialog.show()
                        }) {
                        Text(text = mTime.value)

                        /*
                         * TODO Handle change event
                         */
                    }
                }
            }
        }
    }

    @Composable
    fun Compose800mGoal(goal800mInSec: Long) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(15.dp)
                .clickable { },
            elevation = 10.dp
        ) {
            Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                Text(text = "800m Goal (MM:SS)")

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterVertically)
                ) {
                    val min = String.format("%02d",goal800mInSec / 60)
                    val sec = String.format("%02d",goal800mInSec % 60)
                    Text(
                        text = "${min}:${sec}",
                        modifier = Modifier
                            .padding(0.dp, 30.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}