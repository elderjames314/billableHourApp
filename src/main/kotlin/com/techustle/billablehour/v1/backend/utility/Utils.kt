package com.techustle.billablehour.v1.backend.utility

import java.text.SimpleDateFormat
import java.util.*

class Utils {

    fun formatTime(time: String) : Date {
        val sdf = SimpleDateFormat("HH:mm")
        val newTime = sdf.parse(time)
        return newTime
    }

    fun getHourDiffBetweenTimes(startTime:Date, endTime:Date): Int {
        var diffInSecond: Long = endTime.time - startTime.time
        diffInSecond = diffInSecond / 1000
        val diffInMinutes = diffInSecond.toInt() / 60
        val diffInHour = diffInMinutes / 60
        return diffInHour;
    }

    fun formateDate(dateIn : String) :Date {
        val sdf = SimpleDateFormat("yyyy-mm-dd")
        val datein = sdf.parse(dateIn)
        return datein
    }

}