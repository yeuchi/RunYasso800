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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
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
                            text = "Jog",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        TextField(
                            value = "${configData.jogDisMeter}m",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                viewModel.updateJogDistance(it.toInt())
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
                            text = "Run",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        TextField(
                            value = "${configData.runDisMeter}m",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                viewModel.updateRunDistance(it.toInt())
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
                            text = "Loops",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        TextField(
                            value = "${configData.loopCount}X",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                viewModel.updateLoop(it.toInt())
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
                            text = "Sample Rate",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        TextField(
                            value = "${configData.sampleRateMilliSec}ms",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                viewModel.updateSampleRate(it.toInt())
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