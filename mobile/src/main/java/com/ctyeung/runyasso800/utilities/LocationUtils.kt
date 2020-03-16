package com.ctyeung.runyasso800.utilities

/*
 * Code from article
 * https://medium.com/@gurwindersingh_37022/location-using-fused-api-client-provider-in-kotlin-df1f1f4a0610
 */

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import com.google.android.gms.location.*

object LocationUtils
{
    var locationRequest:LocationRequest ?= null
    var fusedLocationProviderClient: FusedLocationProviderClient ?= null
    var location : MutableLiveData<Location> = MutableLiveData()
    // every 30 seconds a reading -- might want to use dagger for dependency injection
    val DEFAULT_SAMPLE_RATE:Long = 30000
    //val DEFAULT_SAMPLE_RATE:Long = 5000
    val MIN_SAMPLE_RATE:Long = 5000
    //val FASTEST_INTERVAL:Long = 30000

    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (loc in locationResult.locations) {
                Log.i(
                    "MainActivity",
                    "Location: " + loc.latitude + " " + loc.longitude
                )
                // should cause observer in runActivity to invoke update
                location.value = loc
            }
        }
    }

    // using singleton pattern to get the locationProviderClient
    fun getInstance(appContext: Context): FusedLocationProviderClient{
        if(fusedLocationProviderClient == null)
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext)

        if(locationRequest == null) {
            val sampleRate = SharedPrefUtility.getGPSsampleRate()

            locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(sampleRate)
                .setFastestInterval(sampleRate)

            fusedLocationProviderClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            /*
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                locationRequest, this
            )*/
        }
        return fusedLocationProviderClient!!
    }

    fun getLocation() : LiveData<Location> {
        fusedLocationProviderClient!!.lastLocation
            .addOnSuccessListener {loc: Location? ->
                location.value = loc

            }

        return location
    }
}