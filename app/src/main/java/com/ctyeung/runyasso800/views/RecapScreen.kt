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
            // in this column we are specifying the text
            Text(
                // on below line we are specifying the text message
                text = "Summary",

                // on below line we are specifying the text style.
                style = MaterialTheme.typography.h5,

                // on below line we are specifying the text color
                color = Color.Green,

                // on below line we are specifying the font weight
                fontWeight = FontWeight.Bold,

                //on below line we are specifying the text alignment.
                textAlign = TextAlign.Center
            )

            if (viewModel.showDetailDlg.value) {
                ComposeDetailDialog()
            }
        }
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
                        Text(text = "Split:")
                        Text(text = viewModel.detailSplitIndex.value.toString())
                    }
                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Text(text = "Action:")
                        Text(text = viewModel.detailAction)
                    }
                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Text(text = "Time:")
                        Text(text = viewModel.detailTime)
                    }
                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Text(text = "Distance:800m")
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