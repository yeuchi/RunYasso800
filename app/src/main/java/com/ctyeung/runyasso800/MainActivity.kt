package com.ctyeung.runyasso800

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ctyeung.runyasso800.ui.theme.RunYasso800Theme
import com.google.accompanist.pager.*
import dagger.hilt.android.AndroidEntryPoint

/*
 * Reference
 * Tab Layout in Android using Jetpack Compose
 * https://www.geeksforgeeks.org/tab-layout-in-android-using-jetpack-compose/
 *
 * Bottom navigation
 * https://proandroiddev.com/implement-bottom-bar-navigation-in-jetpack-compose-b530b1cd9ee2
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: RunViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askPermissions()
    }

    protected fun askPermissions() {
        val permissions = arrayOf(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
        )
        val requestCode = 200
        requestPermissions(permissions, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permission: String in permissions) {
            // result:0 is ok
            val result = ContextCompat.checkSelfPermission(this, permission)
            if (0 != result) {
                // not permitted to save or read -- !!! data-binding refactor
                return
            }
            else {
                startActivity(Intent(this@MainActivity, ConfigActivity::class.java))
            }
        }
    }
}


@Composable
fun BottomNavigation(currentSelection: String, activity: Activity) {
    val items = listOf(
        BottomNavItem.Config,
        BottomNavItem.Goal,
        BottomNavItem.Run,
        BottomNavItem.Recap,
        BottomNavItem.Share,
    )
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 9.sp
                    )
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),

                alwaysShowLabel = true,
                selected = currentSelection == item.screen_route,
                onClick = {
                    if (currentSelection != item.screen_route) {
                        //navController.navigate(item.screen_route)
                        when (item.screen_route) {
                            BottomNavItem.Config.screen_route -> {
                                activity.startActivity(Intent(activity, ConfigActivity::class.java))
                            }

                            BottomNavItem.Goal.screen_route -> {
                                activity.startActivity(Intent(activity, GoalActivity::class.java))
                            }

                            BottomNavItem.Share.screen_route -> {
                                activity.startActivity(Intent(activity, ShareActivity::class.java))
                            }

                            BottomNavItem.Run.screen_route -> {
                                activity.startActivity(
                                    Intent(
                                        activity,
                                        ExerciseActivity::class.java
                                    )
                                )
                            }

                            BottomNavItem.Recap.screen_route -> {
                                activity.startActivity(Intent(activity, RecapActivity::class.java))
                            }
                        }
                    }
                }
            )
        }
    }
}