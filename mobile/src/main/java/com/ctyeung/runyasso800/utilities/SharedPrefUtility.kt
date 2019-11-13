package com.ctyeung.runyasso800.utilities

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