package com.ctyeung.runyasso800

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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

    private fun askPermissions() {
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