package com.ctyeung.runyasso800.views

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.widget.EditText
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ctyeung.runyasso800.RunViewModel

class GoalScreen(val viewModel: RunViewModel) {

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
            ComposeRunname()
            ComposeMarathonGoal()
            Compose800mGoal()
        }
    }

    @Composable
    fun ComposeRunname() {
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
                    OutlinedTextField(modifier = Modifier
                        .padding(0.dp, 30.dp)
                        .align(Alignment.Center),
                        value = viewModel.runName,
                        onValueChange = { viewModel.runName = it },
                        label = { Text("My first run") }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ComposeMarathonGoal() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(15.dp)
                .clickable { },
            elevation = 10.dp
        ) {
            // Declaring and initializing a calendar
            val mCalendar = Calendar.getInstance()
            val mHour = mCalendar[Calendar.HOUR_OF_DAY]
            val mMinute = mCalendar[Calendar.MINUTE]

            // Value for storing time as a string
            val mTime = remember { mutableStateOf("") }

            // Creating a TimePicker dialod
            val mTimePickerDialog = TimePickerDialog(
                LocalContext.current,
                { _, mHour: Int, mMinute: Int ->
                    mTime.value = "$mHour:$mMinute"
                }, mHour, mMinute, false
            )

            Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                Text(text = "Marathon Goal")
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
                        Text(text = viewModel.goalMarathon)
                    }
                }
            }
        }
    }

    @Composable
    fun Compose800mGoal() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(15.dp)
                .clickable { },
            elevation = 10.dp
        ) {
            Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                Text(text = "800m Goal")

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterVertically)
                ) {
                    Text(text = viewModel.goal800m,
                        modifier = Modifier
                        .padding(0.dp, 30.dp)
                        .align(Alignment.Center))
                }
            }
        }
    }
}