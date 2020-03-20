package com.ctyeung.runyasso800.utilities

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

object TimeFormatter {

    /*
     * Print RunActivity times from Date().getTime()
     */
    fun printDateTime(seconds:Long):String
    {
        val df = SimpleDateFormat("HH:mm:ss") // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"))
        val timeString: String = df.format(seconds)
        return timeString
    }

    /*
     * Print Race / Sprint Goals from seconds
     */
    fun printTime(seconds:Long):String {
        val HH = seconds / 60 / 60
        val hrInSec = HH * 60 * 60
        val mm = (seconds - hrInSec) / 60
        val ss = (seconds - hrInSec) % 60
        return HH.toString() + "hours " + mm.toString() + "minutes " + ss.toString() + "seconds"
    }

    /*
     * Convert to seconds
     */
    fun convertHHmmss(hours:Int, minutes:Int, seconds:Int):Long {
        val totalSeconds:Long = hours.toLong() * 60 * 60 + minutes.toLong() * 60 + seconds
        return totalSeconds
    }
}