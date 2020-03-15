package com.ctyeung.runyasso800.viewModels

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import androidx.core.content.FileProvider
import com.ctyeung.runyasso800.MainActivity
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.utilities.LocationUtils
import java.io.File

/*
 * The only data persistence of this app for now
 */
object SharedPrefUtility
{
    val mypreference = "mypref"
    val keyColor1 = "color1"
    val keyColor2 = "color2"
    val keyImageUri = "imageUri"
    val INTERVAL_MIN:Int = 1
    val INTERVAL_MAX:Int = 10
    val INTERVAL_DEFAULT:Int = 1
    var INTERVAL_MULTIPLY = 200
    val keyInterval = "time"
    val keyLatitude = "lat"
    val keyLongitude = "long"
    val keySprintDis = "sprintDis"
    val keyJogDis = "jogDis"
    val keyNumIterations = "iterations"
    val keyGPSsampleRate = "gps"
    val LAT_LONG_DEFAULT:String = "0"

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

    fun getColor(colorName:String):Int
    {
        var defaultValue = Color.WHITE;
        if(colorName == keyColor2)
            defaultValue = Color.GREEN;

        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        return sharedPreferences.getInt(colorName, defaultValue)
    }

    fun setColor(colorName:String, color:Int)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putInt(colorName, color)
        editor.commit()
    }

    private fun getSharedPref(context:Context):SharedPreferences
    {
        return context.getSharedPreferences(mypreference, Context.MODE_PRIVATE)
    }
}