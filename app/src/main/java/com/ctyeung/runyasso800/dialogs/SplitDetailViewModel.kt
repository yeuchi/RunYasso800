package com.ctyeung.runyasso800.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.R
import com.ctyeung.runyasso800.utilities.TimeFormatter

class SplitDetailViewModel : AndroidViewModel {

    lateinit var txtIndex:String
    lateinit var txtType:String
    lateinit var txtTime:String
    lateinit var txtDistance:String

    constructor(application: Application):super(application){}

    fun render(index:Int, runType:String, duration:Long, distance:Double) {
        txtIndex = "${getResourceString(R.string.detail_split)} ${index}"
        txtType = "${getResourceString(R.string.detail_type)} ${runType}"
        txtTime = "${getResourceString(R.string.detail_duration)}: ${TimeFormatter.printTime(duration)}"
        txtDistance = "${getResourceString(R.string.detail_distance)}: ${Math.round(distance)} m"
    }

    fun getResourceString(id:Int):String {
        return MainApplication.applicationContext().resources.getString(id)
    }
}