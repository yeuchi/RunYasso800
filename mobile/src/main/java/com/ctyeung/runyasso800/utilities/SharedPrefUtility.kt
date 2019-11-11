package com.ctyeung.runyasso800.utilities

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color

/*
 * The only data persistence of this app for now
 */
object SharedPrefUtility
{
    val mypreference = "mypref"
    val keyColor1 = "color1"
    val keyColor2 = "color2"
    var keyInterval = "time"
    val INTERVAL_MIN:Int = 1
    val INTERVAL_MAX:Int = 10
    val INTERVAL_DEFAULT:Int = 1
    var INTERVAL_MULTIPLY = 1000

    open fun getInterval(context:Context):Int
    {
        val sharedPreferences = getSharedPref(context)
        return sharedPreferences.getInt(keyInterval, INTERVAL_DEFAULT)
    }

    open fun setInterval(context:Context, time:Int)
    {
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        editor.putInt(keyInterval, time)
        editor.commit()
    }

    open fun getColor(context:Context, colorName:String):Int
    {
        var defaultValue = Color.WHITE;
        if(colorName == keyColor2)
            defaultValue = Color.GREEN;

        val sharedPreferences = getSharedPref(context)
        return sharedPreferences.getInt(colorName, defaultValue)
    }

    open fun setColor(context:Context, colorName:String, color:Int)
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