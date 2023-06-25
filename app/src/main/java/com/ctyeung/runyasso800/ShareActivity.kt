package com.ctyeung.runyasso800

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController

class ShareActivity : ComponentActivity() {
    private val viewModel:RunViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreenView()
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun MainScreenView(){
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigation(navController = navController) },
            content = {Render()}
        )
//        {
//            NavigationGraph(navController = navController, this)
//        }
    }

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
                text = "Share",

                // on below line we are specifying the text style.
                style = MaterialTheme.typography.h5,

                // on below line we are specifying the text color
                color = Color.Green,

                // on below line we are specifying the font weight
                fontWeight = FontWeight.Bold,

                //on below line we are specifying the text alignment.
                textAlign = TextAlign.Center
            )
            Card() {
                Row() {
                    Text(text = "Header")
                    OutlinedTextField(modifier = Modifier
                        .padding(0.dp, 30.dp),
                        value = viewModel.shareHeader,
                        onValueChange = { viewModel.shareHeader = it },
                        label = { Text("Label") }
                    )
                }
            }

            Card() {
                Row() {
                    Text(text = "Footer")
                    OutlinedTextField(modifier = Modifier
                        .padding(0.dp, 30.dp),
                        value = viewModel.shareFooter,
                        onValueChange = { viewModel.shareFooter = it },
                        label = { Text("Label") }
                    )
                }
            }
            ComposeFAB()
        }
    }

    @Composable
    fun ComposeFAB() {
        FloatingActionButton(onClick = {
            /*
             * TODO DONE
             */
        }) {
            Image(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = "Share"
            )
        }
    }
}