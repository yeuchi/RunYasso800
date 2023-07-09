package com.ctyeung.runyasso800

import android.app.Activity
import android.content.Intent
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigation(currentSelection: String, activity: Activity) {
    val items = listOf(
        BottomNavItem.Config,
        BottomNavItem.Goal,
        BottomNavItem.Run,
        BottomNavItem.Recap,
        BottomNavItem.Share,
    )
    androidx.compose.material.BottomNavigation(
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
                                activity.startActivity(
                                    Intent(activity, ConfigActivity::class.java)
                                )
                            }

                            BottomNavItem.Goal.screen_route -> {
                                activity.startActivity(
                                    Intent(activity, GoalActivity::class.java)
                                )
                            }

                            BottomNavItem.Share.screen_route -> {
                                activity.startActivity(
                                    Intent(activity, ShareActivity::class.java)
                                )
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
                                activity.startActivity(
                                    Intent(activity, RecapActivity::class.java)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}