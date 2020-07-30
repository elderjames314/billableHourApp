package com.techustle.billablehour.v1.backend.resource

import com.techustle.billablehour.v1.backend.utility.Utils
import org.springframework.beans.factory.annotation.Autowired
import java.text.SimpleDateFormat
import java.util.*

class TimesheetResource{
    var employeeId:Int = 0
    var amount:Double = 0.0
    var project:String = ""
    var date:Date = Date()
    var startTime:Date = Date()
    var endTime:Date = Date()
    var hourWorked:Int = 0
    var href: TimesheetLinkResource = TimesheetLinkResource()

    @Autowired
    lateinit var utils: Utils

    fun setStarttime(start_time: String) {
        this.startTime = utils.formatTime(start_time)
    }

    fun setEndtime(end_time: String) {
        this.endTime = utils.formatTime(end_time)

        //since we have known start and endtime from here, it will be nice
        //calculate the hour difference between start and end time
        this.hourWorked = utils.getHourDiffBetweenTimes(this.startTime, this.endTime)
    }
}




