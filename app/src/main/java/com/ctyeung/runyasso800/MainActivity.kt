package com.ctyeung.runyasso800

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ctyeung.runyasso800.ui.theme.RunYasso800Theme
import com.ctyeung.runyasso800.views.GoalScreen
import com.ctyeung.runyasso800.views.RecapScreen
import com.google.accompanist.pager.*
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.navigation.compose.rememberNavController

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
    lateinit var viewModel: RunViewModel
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askPermissions()

        viewModel = ViewModelProvider(this)[RunViewModel::class.java]
        setContent {
            RunYasso800Theme {
                // A surface container using the 'background' color from the theme
                MainScreenView()
//                TabLayout(viewModel)
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun MainScreenView(){
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigation(navController = navController) }
        ) {
            NavigationGraph(navController = navController)
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
    fun NavigationGraph(navController: NavHostController) {
        viewModel = ViewModelProvider(this)[RunViewModel::class.java]

        NavHost(navController, startDestination = BottomNavItem.Config.screen_route) {
            composable(BottomNavItem.Config.screen_route) {
                ConfigScreen(viewModel).Render()
            }
            composable(BottomNavItem.Goal.screen_route) {
                GoalScreen(viewModel).Render()
            }
            composable(BottomNavItem.Run.screen_route) {
                ExerciseScreen(viewModel).Render()
            }
            composable(BottomNavItem.Recap.screen_route) {
                RecapScreen(viewModel).Render()
            }
            composable(BottomNavItem.Share.screen_route) {
                ShareScreen(viewModel).Render()
            }
        }
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