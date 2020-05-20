package com.ctyeung.runyasso800.utilities

/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// package com.google.android.gms.location.sample.locationupdatesforegroundservice;

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ctyeung.runyasso800.MainActivity
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.dagger.DaggerComponent
import com.ctyeung.runyasso800.stateMachine.StateMachine
import com.ctyeung.runyasso800.viewModels.IRunStatsCallBack
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.ctyeung.runyasso800.viewModels.UpdateCode
import com.google.android.gms.location.*
import javax.inject.Inject
import javax.inject.Named


/**
 * A bound and started service that is promoted to a foreground service when location updates have
 * been requested and all clients unbind.
 *
 * For apps running in the background on "O" devices, location is computed only once every 10
 * minutes and delivered batched every 30 minutes. This restriction applies even to apps
 * targeting "N" or lower which are run on "O" devices.
 *
 * This sample show how to use a long-running service for location updates. When an activity is
 * bound to this service, frequent location updates are permitted. When the activity is removed
 * from the foreground, the service promotes itself to a foreground service, and location updates
 * continue. When the activity comes back to the foreground, the foreground service stops, and the
 * notification associated with that service is removed.
 */
class LocationUpdateService : Service(), IRunStatsCallBack {
    private val mBinder: IBinder = LocalBinder()

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private var mChangingConfiguration = false
    private var mNotificationManager: NotificationManager? = null

    /**
     * Contains parameters used by [com.google.android.gms.location.FusedLocationProviderApi].
     */
    private var mLocationRequest: LocationRequest? = null

    /**
     * Provides access to the Fused Location Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    /**
     * Callback for changes in location.
     */
    private var mLocationCallback: LocationCallback? = null
    private var mServiceHandler: Handler? = null

    /**
     * The current location.
     */
    private var mLocation: Location? = null
    private lateinit var stateMachine:StateMachine

    override fun onCreate() {
        instance = this

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
            }
        }
        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            // Create the channel for the notification
            val mChannel = NotificationChannel(
                CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager!!.createNotificationChannel(mChannel)
        }
    }

    fun startStateMachine() {
        stateMachine = StateMachine(this)

        // start with a clean slate
        stateMachine.interruptClear()
        createLocationRequest()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(TAG, "Service started")
        val startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false)

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates()
            stopSelf()
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mChangingConfiguration = true
    }

    override fun onBind(intent: Intent): IBinder {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()")
        stopForeground(true)
        mChangingConfiguration = false
        return mBinder
    }

    override fun onRebind(intent: Intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()")
        stopForeground(true)
        mChangingConfiguration = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.i(
            TAG,
            "Last client unbound from service"
        )

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(
                this
            )
        ) {
            Log.i(
                TAG,
                "Starting foreground service"
            )
            startForeground(
                NOTIFICATION_ID,
                getNotification()
            )
        }
        return true // Ensures onRebind() is called when a client re-binds.
    }

    override fun onDestroy() {
        mServiceHandler!!.removeCallbacksAndMessages(null)
    }

    fun runStart() {
        stateMachine.interruptStart()
    }

    fun runPause() {
        stateMachine.interruptPause()
    }

    fun runClear() {
        stateMachine.interruptClear()
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * [SecurityException].
     */
    fun requestLocationUpdates() {
        Log.i(
            TAG,
            "Requesting location updates"
        )
        Utils.setRequestingLocationUpdates(this, true)
        startService(
            Intent(
                applicationContext,
                LocationUpdateService::class.java
            )
        )
        try {
            mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback, Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            Utils.setRequestingLocationUpdates(this, false)
            Log.e(
                TAG,
                "Lost location permission. Could not request updates. $unlikely"
            )
        }
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * [SecurityException].
     */
    fun removeLocationUpdates() {
        Log.i(
            TAG,
            "Removing location updates"
        )
        try {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
            Utils.setRequestingLocationUpdates(this, false)
            stopSelf()
        } catch (unlikely: SecurityException) {
            Utils.setRequestingLocationUpdates(this, true)
            Log.e(
                TAG,
                "Lost location permission. Could not remove updates. $unlikely"
            )
        }
    }// Channel ID// Extra to help us figure out if we arrived in onStartCommand via the notification or not.

    // The PendingIntent that leads to a call to onStartCommand() in this service.

    // The PendingIntent to launch activity.

    // Set the Channel ID for Android O.

    private val lastLocation: Location?
        private get() {
            try {
                mFusedLocationClient!!.lastLocation
                    .addOnSuccessListener {loc: Location? ->
                        mLocation = loc
                    }

            } catch (unlikely: SecurityException) {
                Log.e(
                    TAG,
                    "Lost location permission.$unlikely"
                )
            }
            return mLocation
        }

    fun broadcast2RunActivity(msg:UpdateCode) {
        // Notify anyone listening for broadcasts about the new location.
        var intent:Intent = Intent(ACTION_BROADCAST);
        intent.putExtra(EXTRA_UPDATE_CODE, msg);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    // State machine callback -- data update
    override fun onHandleLocationUpdate() {
        broadcast2RunActivity(UpdateCode.LOCATION_UPDATE)
    }

    // State machine callback -- background update, vibrate, beep
    override fun onChangedSplit() {
        broadcast2RunActivity(UpdateCode.CHANGE_SPLIT)
    }

    // State machine callback
    override fun onHandleYassoDone() {
        broadcast2RunActivity(UpdateCode.DONE)
    }

    private fun onNewLocation(location: Location) {
        Log.i(
            TAG,
            "New location: $location"
        )
        mLocation = location
        stateMachine.update(location)

        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {
            if(mNotificationManager != null)
            mNotificationManager!!.notify(NOTIFICATION_ID, getNotification());
        }
    }

    /**
     * Returns the [NotificationCompat] used as part of the foreground service.
     */
     fun getNotification(): Notification {
        val intent = Intent(this, LocationUpdateService::class.java)
        val text: CharSequence =
            Utils.getLocationText(mLocation)

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_UPDATE_CODE, true)

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        val servicePendingIntent = PendingIntent.getService(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // The PendingIntent to launch activity.
        val activityPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java), 0
        )
        val builder =
            NotificationCompat.Builder(this)
                .addAction(
                    R.drawable.ic_launch, getString(R.string.launch_activity),
                    activityPendingIntent
                )
                .addAction(
                    R.drawable.ic_cancel, getString(R.string.remove_location_updates),
                    servicePendingIntent
                )
                .setContentText(text)
                .setContentTitle(Utils.getLocationTitle(this))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID) // Channel ID
        }
        return builder.build()
    }

    /**
     * Sets the location request parameters.
     */
    private fun createLocationRequest() {
        val interval = SharedPrefUtility.get(SharedPrefUtility.keyGPSsampleRate, DEFAULT_SAMPLE_RATE)
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = interval
        mLocationRequest!!.fastestInterval = interval / 2
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        fun getService(): LocationUpdateService? {
            return LocationUpdateService.instance
        }
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The [Context].
     */
    fun serviceIsRunningInForeground(context: Context): Boolean {
        val manager = context.getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    companion object {
        private const val PACKAGE_NAME =
            "com.ctyeung.runyasso800.utilities.locationupdatesforegroundservice"
        private val TAG =
            LocationUpdateService::class.java.simpleName

        /**
         * The name of the channel for notifications.
         */
        private const val CHANNEL_ID = "channel_01"
        const val ACTION_BROADCAST = "$PACKAGE_NAME.broadcast"
        const val EXTRA_LOCATION = "$PACKAGE_NAME.location"
        const val EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME + ".started_from_notification"
        const val EXTRA_UPDATE_CODE = "updateCode"

        const val MAX_SAMPLE_RATE:Long = 80000
        const val DEFAULT_SAMPLE_RATE:Long = 20000
        const val MIN_SAMPLE_RATE:Long = 3000

        /**
         * The identifier for the notification displayed for the foreground service.
         */
        private const val NOTIFICATION_ID = 12345678
        var instance:LocationUpdateService?=null
    }
}