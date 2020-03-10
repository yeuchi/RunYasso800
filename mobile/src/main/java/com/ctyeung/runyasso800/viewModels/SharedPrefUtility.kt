package com.ctyeung.runyasso800.viewModels

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import androidx.core.content.FileProvider
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
    var keyInterval = "time"
    var keyLatitude = "lat"
    var keyLongitude = "long"
    var LAT_LONG_DEFAULT:String = "0"

     fun getLatitude(context:Context):Double
    {
        val sharedPreferences = getSharedPref(context)
        val str = sharedPreferences.getString(keyLatitude, LAT_LONG_DEFAULT)
        return str.toDouble()
    }

     fun setLatitude(context:Context, latitude:Double)
    {
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        editor.putString(keyLatitude, latitude.toString())
        editor.commit()
    }

     fun getLongitude(context:Context):Double
    {
        val sharedPreferences = getSharedPref(context)
        val str = sharedPreferences.getString(keyLongitude, LAT_LONG_DEFAULT)
        return str.toDouble()
    }

     fun setLongitude(context:Context, longitude:Double)
    {
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        editor.putString(keyLongitude, longitude.toString())
        editor.commit()
    }

     fun getInterval(context:Context):Int
    {
        val sharedPreferences = getSharedPref(context)
        return sharedPreferences.getInt(
            keyInterval,
            INTERVAL_DEFAULT
        )
    }

     fun setInterval(context:Context, time:Int)
    {
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        editor.putInt(keyInterval, time)
        editor.commit()
    }

    /*
     * uri to last random dot images
     */
    fun getImageUri(context: Context): Uri
    {
        val defaultValue = "someDefaultUri"
        val sharedPreferences = getSharedPref(context)
        val path = sharedPreferences.getString(keyImageUri, defaultValue)
        val file = File(path!!)
        return FileProvider.getUriForFile(
            context,
            "com.example.ctyeung.runyasso800.fileprovider",
            file
        )
    }
     fun getColor(context:Context, colorName:String):Int
    {
        var defaultValue = Color.WHITE;
        if(colorName == keyColor2)
            defaultValue = Color.GREEN;

        val sharedPreferences = getSharedPref(context)
        return sharedPreferences.getInt(colorName, defaultValue)
    }

     fun setColor(context:Context, colorName:String, color:Int)
    {
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        editor.putInt(colorName, color)
        editor.commit()
    }

    fun getSharedPref(context:Context):SharedPreferences
    {
        return context.getSharedPreferences(mypreference, Context.MODE_PRIVATE)
    }
}