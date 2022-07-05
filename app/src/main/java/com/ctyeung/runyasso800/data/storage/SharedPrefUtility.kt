package com.ctyeung.runyasso800.storage

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.ctyeung.runyasso800.MainApplication
import java.io.File
import java.lang.reflect.Type

/*
 * TODO:
 *  1. Improve type safety
 *
 * Description: Exercise using inline and reified type.
 * However, I am sacrificing the type safety of kotlin language.
 */
object SharedPrefUtility
{
    const val mypreference = "mypref"
    const val keyImageUri = "imageUri"                  // not used -- image uri
    const val keySprintLength = "sprintLength"          // 800 meter default
    const val keyJogLength = "jogLength"                // 800 meter default
    const val keyNumIterations = "iterations"           // 10 splits default
    const val keyGPSsampleRate = "gps"                  // 20 seconds default
    const val keyName = "name"                          // Yasso800 default
    const val keySprintGoal = "sprintGoal"              // 0 no default
    const val keyRaceGoal = "raceGoal"                  // 0 no default
    const val keySplitDistance = "splitDistance"        // temp storage of distance run in this split/jog or sprint
    const val keyStepIndex = "stepIndex"                // auto incrementer for step row
    const val keySplitIndex = "splitIndex"              // split index
    const val keyRunState = "runstate"                  // State Machine -- current run state
    const val keyLastLatitutde = "lastLatitude"         // last known latitude
    const val keyLastLongitude = "lastLongitude"        // last know longitude

    inline fun<reified T> get(key:String, defValue:T):T {
        val pref = getSharedPref()
        val k = key
        when (defValue) {
            is Int -> {
                if(k == keySprintLength ||
                    k == keyJogLength ||
                    k == keyNumIterations ||
                    k == keySplitIndex ||
                    k == keyStepIndex ) {
                    return pref.getInt(k, defValue as Int) as T
                }
            }

            is Long -> {
                if(k == keyGPSsampleRate ||
                    k == keySprintGoal ||
                    k == keyRaceGoal) {
                    return pref.getLong(k, defValue as Long) as T
                }
            }

            is Float -> {
                if(k == keySplitDistance)
                    return pref.getFloat(k, defValue as Float) as T
            }

            is String -> {
                if(k == keyName ||
                    k == keyLastLongitude ||
                    k == keyLastLatitutde ) {
                    return pref.getString(k, defValue as String) as T
                }
            }

            is Type -> {
//                if(k == keyRunState ) {
//                    val def = type2String(defValue as Type)
//                    var str = pref.getString(k, def)
//                    if(str==null)
//                        str = type2String(StateError::class.java)
//
//                    return Class.forName(str) as T
//                }
            }
            // else -> return defValue
        }
        return defValue
    }

    inline fun<reified T> set(key:String, value:T)
    {
        val pref = getSharedPref()
        val editor = pref.edit()

        val k = key
        when (value) {

            is Int -> {
                if(k == keySprintLength ||
                    k == keyJogLength ||
                    k == keyNumIterations ||
                    k == keySplitIndex ||
                    k == keyStepIndex ) {
                    editor.putInt(k, value as Int)
                }
            }

            is Long -> {
                if(k == keyGPSsampleRate ||
                    k == keySprintGoal ||
                    k == keyRaceGoal) {
                    editor.putLong(k, value as Long)
                }
            }

            is Float -> {
                if( k == keySplitDistance) {
                    editor.putFloat(k, value as Float)
                }
            }

            is String -> {
                if(k == keyName ||
                    k == keyLastLongitude ||
                    k == keyLastLatitutde ) {
                    editor.putString(k, value as String)
                }
            }

            is Type -> {
                if(k == keyRunState) {
                    val str = type2String(value as Type)
                    editor.putString(k, str)
                }
            }
            else -> {
                Log.d("SharedPrefUtility.set()", "failed")
                return
            }
        }
        editor.commit()
    }

    fun type2String(type:Type):String {
        return type.toString().removePrefix("class ")
    }

    fun getSharedPref(context:Context = MainApplication.applicationContext()):SharedPreferences
    {
        return context.getSharedPreferences(mypreference, Context.MODE_PRIVATE)
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
}