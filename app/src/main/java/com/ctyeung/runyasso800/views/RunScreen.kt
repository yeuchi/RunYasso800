package com.ctyeung.runyasso800

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
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
import com.ctyeung.runyasso800.viewmodels.RunViewModel

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
                Image(painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "location")
                Text(text = "lat:")
                Text(text = viewModel.lat.value.toString())
                Text(text = "lon:")
                Text(text = viewModel.lon.value.toString())
            }
        }
    }
}

sealed class ExerciseState {
    object Stop : ExerciseState()   // default Stop -> Run / Jog
    object Run : ExerciseState()    // active Run -> Pause / Jog
    object Jog : ExerciseState()    // active Jog -> Pause / Run
    object Pause : ExerciseState()  // interrupt Pause -> Resume -> Run / Jog
    object Resume : ExerciseState() // go back to Run / Jog
}