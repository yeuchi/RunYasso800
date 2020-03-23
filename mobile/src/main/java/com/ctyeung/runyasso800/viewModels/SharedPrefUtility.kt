package com.ctyeung.runyasso800.viewModels

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import androidx.core.content.FileProvider
import com.ctyeung.runyasso800.MainActivity
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.utilities.LocationUtils
import java.io.File

/*
 * The only data persistence of this app for now
 */
object SharedPrefUtility
{
    val mypreference = "mypref"
    val keyImageUri = "imageUri"
    val INTERVAL_DEFAULT:Int = 1
    val keyInterval = "time"
    val keyLatitude = "lat"
    val keyLongitude = "long"
    val keySprintDis = "sprintDis"
    val keyJogDis = "jogDis"
    val keyNumIterations = "iterations"
    val keyGPSsampleRate = "gps"
    val keyName = "name"
    val keySprintGoal = "sprintGoal"
    val keyRaceGoal = "raceGoal"
    val LAT_LONG_DEFAULT:String = "0"

    fun initDefaults() {
        setName(MainApplication.applicationContext().resources.getString(R.string.run_yasso_800))
        setGoal(keyRaceGoal, 0)
        setGoal(keySprintGoal, 0)
    }

    fun getName():String
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val defaultName = MainApplication.applicationContext().resources.getString(R.string.run_yasso_800)
        return sharedPreferences.getString(keyName, defaultName)
    }

    fun setName(str:String)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putString(keyName, str)
        editor.commit()
    }

    fun getGoal(key:String):Long
    {
        var defaultSeconds:Long = 0
        when(key) {
            keySprintGoal -> {
                defaultSeconds /= 60
            }
            keyRaceGoal -> { /* default */ }
        }
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val seconds = sharedPreferences.getLong(key, defaultSeconds)
        return seconds
    }

    fun setGoal(key:String, seconds:Long)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putLong(key, seconds)
        editor.commit()
    }


    fun getGPSsampleRate():Long
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val i = sharedPreferences.getLong(keyGPSsampleRate, LocationUtils.DEFAULT_SAMPLE_RATE)
        return i
    }

    fun setGPSsampleRate(rate:Long)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putLong(keyGPSsampleRate, rate)
        editor.commit()
    }

    fun getNumIterations():Int
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val i = sharedPreferences.getInt(keyNumIterations, Split.DEFAULT_SPLIT_ITERATIONS)
        return i
    }

    fun setNumIterations(distance:Int)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putInt(keyNumIterations, distance)
        editor.commit()
    }

    fun getDistance(key:String):Int
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val i = sharedPreferences.getInt(key, Split.DEFAULT_SPLIT_ITERATIONS.toInt())
        return i
    }

    fun setDistance(key:String, distance:Int)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putInt(key, distance)
        editor.commit()
    }

    fun getLatitude():Double
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val str = sharedPreferences.getString(keyLatitude, LAT_LONG_DEFAULT)
        return str.toDouble()
    }

    fun setLatitude(latitude:Double)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putString(keyLatitude, latitude.toString())
        editor.commit()
    }

    fun getLongitude():Double
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val str = sharedPreferences.getString(keyLongitude, LAT_LONG_DEFAULT)
        return str.toDouble()
    }

    fun setLongitude(longitude:Double)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putString(keyLongitude, longitude.toString())
        editor.commit()
    }

    fun getInterval():Int
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        return sharedPreferences.getInt(
            keyInterval,
            INTERVAL_DEFAULT
        )
    }

    fun setInterval(time:Int)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putInt(keyInterval, time)
        editor.commit()
    }

    /*
     * uri to last random dot images
     */
    fun getImageUri(): Uri
    {
        val defaultValue = "someDefaultUri"
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val path = sharedPreferences.getString(keyImageUri, defaultValue)
        val file = File(path!!)
        return FileProvider.getUriForFile(
            MainApplication.applicationContext(),
            "com.example.ctyeung.runyasso800.fileprovider",
            file
        )
    }

    private fun getSharedPref(context:Context):SharedPreferences
    {
        return context.getSharedPreferences(mypreference, Context.MODE_PRIVATE)
    }
}