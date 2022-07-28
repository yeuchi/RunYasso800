package com.ctyeung.runyasso800.features.run

import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ctyeung.runyasso800.BaseActivity
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.UpdateCode
import com.ctyeung.runyasso800.features.run.services.LocationUpdateService
import com.ctyeung.runyasso800.features.run.stateMachine.RunStates
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RunActivity : BaseActivity() {
    lateinit var wakeLock: PowerManager.WakeLock

    companion object {
        var self:RunActivity? = null
    }

    var isPermitted:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run)
    }

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private var myReceiver: MyReceiver? = null

    // A reference to the service used to get location updates.
    private var mService: LocationUpdateService? = null

    // Tracks the bound state of the service.
    private var mBound = false

    // Monitors the state of the connection to the service.
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: LocationUpdateService.LocalBinder = service as LocationUpdateService.LocalBinder
            mService = binder.getService()
            mService?.startStateMachine()

            if(isPermitted)
                mService?.requestLocationUpdates()

            mBound = true
            registerReceiver()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mBound = false
        }
    }

    /**
     * Receiver for broadcasts sent by [LocationUpdatesService].
     */
    private class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val code = intent.extras?.get(LocationUpdateService.EXTRA_UPDATE_CODE) as UpdateCode
            self?.apply {
                val fragment:RunFragment = findViewById<View>(R.id.nav_host_fragment) as RunFragment

                fragment?.apply {
                    when (code) {
                        UpdateCode.CHANGE_SPLIT -> {
                            onChangedSplit()
                        }

                        UpdateCode.DONE -> {
                            onHandleYassoDone()
                        }

                        UpdateCode.LOCATION_UPDATE -> {
                            onHandleLocationUpdate()
                        }
                    }
                }
            }
        }
    }

    private fun onChangedSplit() {
    }

    private fun registerReceiver() {
        myReceiver = MyReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            myReceiver!!,
            IntentFilter(LocationUpdateService.ACTION_BROADCAST)
        )
    }

    private fun unregisterReceiver() {
        if(myReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver!!)
            myReceiver = null
        }
    }

    override fun gotoActivity(classType:Class<*>) {
        RemoveLocation()
        unregisterReceiver()
        super.gotoActivity(classType)
    }

    private fun RemoveLocation() {
        if(mService!=null)
            mService!!.removeLocationUpdates()

        if(wakeLock.isHeld)
            wakeLock.release()
    }
}