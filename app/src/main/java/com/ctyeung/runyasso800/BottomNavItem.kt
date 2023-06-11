package com.ctyeung.runyasso800

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(var title:String, var icon: ImageVector, var screen_route:String) {

    object Config : BottomNavItem("Config", Icons.Default.Settings, "config")
    object Goal : BottomNavItem("Goal", Icons.Default.Star, "goal")
    object Run : BottomNavItem("Run", Icons.Default.Face, "run")
    object Recap : BottomNavItem("Recap", Icons.Default.List, "recap")
    object Share : BottomNavItem("Share", Icons.Default.Share, "share")
}
