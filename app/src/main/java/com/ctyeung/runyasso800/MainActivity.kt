package com.ctyeung.runyasso800

import android.annotation.SuppressLint
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
    private val viewModel:RunViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askPermissions()

        /*
                 * TODO something to say afte splash screen and before transition to active Activity
                 */
        startActivity(Intent(this@MainActivity, ConfigActivity::class.java))

//        setContent {
//            RunYasso800Theme {
//                MainScreenView()
//            }
//        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun MainScreenView(){
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigation(navController = navController) },
            content = {

            }
        )
//        {
//            NavigationGraph(navController = navController, this)
//        }
    }
    protected fun askPermissions() {
        val permissions = arrayOf(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
        )
        val requestCode = 200
        requestPermissions(permissions, requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(permission:String in permissions)
        {
            // result:0 is ok
            val result = ContextCompat.checkSelfPermission(this, permission)
            if (0!=result)
            {
                // not permitted to save or read -- !!! data-binding refactor
                return
            }
        }
    }
}



@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Config,
        BottomNavItem.Goal,
        BottomNavItem.Run,
        BottomNavItem.Recap,
        BottomNavItem.Share,
    )
    BottomNavigation(
        backgroundColor = Color.Blue,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)},
                label = { Text(text = item.title,
                    fontSize = 9.sp) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),

                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    if (currentRoute != item.screen_route) {
                        navController.navigate(item.screen_route)
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, activity:ComponentActivity) {
    NavHost(navController, startDestination = BottomNavItem.Config.screen_route) {
        composable(BottomNavItem.Config.screen_route) {
            activity.startActivity(Intent(activity, ConfigActivity::class.java))
        }
        composable(BottomNavItem.Goal.screen_route) {
            activity.startActivity(Intent(activity, GoalActivity::class.java))
        }
        composable(BottomNavItem.Run.screen_route) {
            activity.startActivity(Intent(activity, ExerciseActivity::class.java))
        }
        composable(BottomNavItem.Recap.screen_route) {
            activity.startActivity(Intent(activity, RecapActivity::class.java))
        }
        composable(BottomNavItem.Share.screen_route) {
            activity.startActivity(Intent(activity, ShareActivity::class.java))
        }
    }
}