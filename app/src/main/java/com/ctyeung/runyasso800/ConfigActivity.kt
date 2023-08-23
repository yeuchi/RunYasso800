package com.ctyeung.runyasso800

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import com.ctyeung.runyasso800.ui.theme.RunYasso800Theme
import com.ctyeung.runyasso800.viewmodels.ConfigData
import com.ctyeung.runyasso800.viewmodels.ConfigEvent
import com.ctyeung.runyasso800.viewmodels.ConfigViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigActivity : ComponentActivity() {
    private val viewModel: ConfigViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.event.observe(this, Observer(::onEventChange))
    }

    private fun onEventChange(event: ConfigEvent) {
        setContent {
            RunYasso800Theme {
                when (event) {
                    is ConfigEvent.InProgress -> ComposeSpinner()
                    is ConfigEvent.Success -> ComposeScreen(event.configData)
                    is ConfigEvent.Error -> ComposeError(error = event.msg)
                }
            }
        }

    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun ComposeScreen(configData: ConfigData) {
        Scaffold(
            bottomBar = { BottomNavigation(BottomNavItem.Config.screen_route, this) },
            content = { Render(configData) }
        )
    }

    @Composable
    fun Render(configData: ConfigData) {
        Column(
            // in this column we are specifying modifier
            // and aligning it center of the screen on below lines.
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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
                            text = "Jog (meters)",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        val jogTextString = remember {
                            mutableStateOf(
                                TextFieldValue(
                                    text = configData.jogDisMeter.toString(),
                                    selection = TextRange(configData.jogDisMeter.toString().length),
                                ),
                            )
                        }
                        TextField(
                            value = jogTextString.value,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                if (it.text.contains('\n')) {
                                    val num = it.text.replace("\n", "").toInt()
                                    viewModel.updateJogDistance(num)
                                } else {
                                    jogTextString.value = it
                                }
                            }
                        )
                    }

                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_run),
                            contentDescription = "Play",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        Text(
                            text = "Run (meters)",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        val runTextString = remember {
                            mutableStateOf(
                                TextFieldValue(
                                    text = configData.runDisMeter.toString(),
                                    selection = TextRange(configData.runDisMeter.toString().length),
                                ),
                            )
                        }
                        TextField(
                            value = runTextString.value,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                if (it.text.contains('\n')) {
                                    val num = it.text.replace("\n", "").toInt()
                                    viewModel.updateRunDistance(num)
                                } else {
                                    runTextString.value = it
                                }
                            }
                        )
                    }

                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_replay),
                            contentDescription = "Play",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        Text(
                            text = "Loops (x)",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        val loopTextString = remember {
                            mutableStateOf(
                                TextFieldValue(
                                    text = configData.loopCount.toString(),
                                    selection = TextRange(configData.loopCount.toString().length),
                                ),
                            )
                        }
                        TextField(
                            value = loopTextString.value,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                if (it.text.contains('\n')) {
                                    val num = it.text.replace("\n", "").toInt()
                                    viewModel.updateLoop(num)
                                } else {
                                    loopTextString.value = it
                                }
                            }
                        )
                    }

                    Row(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_input),
                            contentDescription = "Sample Rate",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        Text(
                            text = "Sample Rate (meter/sec)",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        val sampleRateTextString = remember {
                            mutableStateOf(
                                TextFieldValue(
                                    text = configData.sampleRateMilliSec.toString(),
                                    selection = TextRange(configData.sampleRateMilliSec.toString().length),
                                ),
                            )
                        }
                        TextField(
                            value = sampleRateTextString.value,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                if (it.text.contains('\n')) {
                                    val num = it.text.replace("\n", "").toInt()
                                    viewModel.updateJogDistance(num)
                                } else {
                                    sampleRateTextString.value = it
                                }
                            }
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
                            text = "Version 2.0",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(10.dp, 10.dp, 10.dp, 10.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Button(
                            onClick = {
                                viewModel.resetFactory()
                            },
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        ) {
                            Text(text = "Factory Reset")
                        }

                        Button(
                            onClick = {
                                viewModel.updateConfig()
                            },
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