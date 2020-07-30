package com.techustle.billablehour.v1.backend.resource

class TimesheetDataResource {
    var message:String ? = null
    var totalTimesheet: String? = null
    var data : MutableList<IncommingTimesheetResource> = mutableListOf<IncommingTimesheetResource>()
}