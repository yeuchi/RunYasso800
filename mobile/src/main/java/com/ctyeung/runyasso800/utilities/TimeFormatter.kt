package com.ctyeung.runyasso800.utilities

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

object TimeFormatter {

    fun printTime(seconds:Long):String
    {
        val df = SimpleDateFormat("HH:mm:ss") // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"))
        val timeString: String = df.format(seconds)
        return timeString
    }
}