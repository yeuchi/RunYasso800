package com.ctyeung.runyasso800

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap

class SettingScreen(val viewModel: RunViewModel) {

    @Composable
    fun Render() {
        Dialog(
            onDismissRequest = { viewModel.showDetailDlg.value = false },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Card() {
                Column(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_jog),
                            contentDescription = "Play",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        Text(
                            text = "Jog",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                    }

                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_run),
                            contentDescription = "Play",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        Text(
                            text = "Run",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                    }

                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_replay),
                            contentDescription = "Play",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        Text(
                            text = "Loops",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                    }

                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_input),
                            contentDescription = "Sample Rate",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        Text(
                            text = "Sample Rate",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                    }

                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        ContextCompat.getDrawable(LocalContext.current, R.mipmap.ic_launcher)?.let {
                            Image(
                                bitmap = it.toBitmap().asImageBitmap(),
                                contentDescription = "Version",
                                modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                            )
                        }
                        Text(
                            text = "Version",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        ) {
                            Text(text = "Factory Reset")
                        }

                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        ) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    }
}
