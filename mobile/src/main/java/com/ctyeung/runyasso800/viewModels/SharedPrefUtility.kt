package com.ctyeung.runyasso800.viewModels

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.core.content.FileProvider
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.stateMachine.StateError
import com.ctyeung.runyasso800.utilities.LocationUpdateService
import java.io.File
import java.lang.Exception
import java.lang.reflect.Type

/*
 * TODO: Refactor with inline and Reified functions
 */
object SharedPrefUtility
{
    const val mypreference = "mypref"
    const val keyImageUri = "imageUri"
    const val keySprintDis = "sprintDis"
    const val keyJogDis = "jogDis"
    const val keyNumIterations = "iterations"
    const val keyGPSsampleRate = "gps"
    const val keyName = "name"
    const val keySprintGoal = "sprintGoal"
    const val keyRaceGoal = "raceGoal"
    const val keySplitDistance = "splitDistance"
    const val keyStepIndex = "stepIndex"
    const val keySplitIndex = "splitIndex"
    const val keyRunState = "runstate"
    const val keyLastLatitutde = "lastLatitude"
    const val keyLastLongitude = "lastLongitude"

    fun getLastLocation(key:String):String
    {
        var defaultLocation:String = "--"
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val str = sharedPreferences.getString(key, defaultLocation)
        return str
    }

    fun setLastLocation(key:String, location:Double)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putString(key, location.toString())
        editor.commit()
    }

    fun getRunState():Type
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val stateString = sharedPreferences.getString(keyRunState, "")
        if(stateString!=null && stateString.length>0) {
            try{
                return Class.forName(stateString)
            }
            catch (ex:Exception){
                return StateError::class.java
            }
        }
        return StateError::class.java
    }

    fun setRunState(stateClass:Type)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putString(keyRunState, stateClass.toString().removePrefix("class "))
        editor.commit()
    }

    fun getName():String
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val defaultName = MainApplication.applicationContext().resources.getString(R.string.run_yasso_800)
        val name = sharedPreferences.getString(keyName, defaultName)
        if(name!=null && name.length>0)
            return name

        return defaultName
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
            keySprintGoal,
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

    // --- Run Activity -----

    fun getIndex(key:String):Int
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val i = sharedPreferences.getInt(key, 0)
        return i
    }

    fun setIndex(key:String, i:Int)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putInt(key, i)
        editor.commit()
    }

    fun getSplitDistance():Double
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val i = sharedPreferences.getFloat(keySplitDistance, 0f)
        return i.toDouble()
    }

    fun setSplitDistance(meters:Double)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putFloat(keySplitDistance, meters.toFloat())
        editor.commit()
    }

    fun getGPSsampleRate():Long
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val i = sharedPreferences.getLong(keyGPSsampleRate, LocationUpdateService.DEFAULT_SAMPLE_RATE)
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

    fun setNumIterations(num:Int)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putInt(keyNumIterations, num)
        editor.commit()
    }

    fun getDistance(key:String):Int
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val i = sharedPreferences.getInt(key, Split.DEFAULT_SPLIT_DISTANCE.toInt())
        return i
    }

    fun setDistance(key:String, distance:Int)
    {
        val sharedPreferences = getSharedPref(MainApplication.applicationContext())
        val editor = sharedPreferences.edit()
        editor.putInt(key, distance)
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