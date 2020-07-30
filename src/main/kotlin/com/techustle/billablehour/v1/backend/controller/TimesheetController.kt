package com.techustle.billablehour.v1.backend.controller

import com.techustle.billablehour.v1.backend.resource.*
import com.techustle.billablehour.v1.backend.service.TimesheetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = arrayOf("http://localhost:8080"))
@RequestMapping("/billablehour/v1/timesheets")
class TimesheetController {

    @Autowired
    lateinit var timesheetService: TimesheetService
    /**
     * @return list of timesheets
     * @param List of timesheets
     */
    @PostMapping
    fun addTimesheet(@RequestBody incommingTimesheets: List<IncommingTimesheetResource>) : ResponseEntity<JobUploadResponseResource> {
        var total:Int = 0
        var jobUploadResponseResource : JobUploadResponseResource = JobUploadResponseResource()
        if(incommingTimesheets.isNotEmpty()) {
            // that there is at least one timesheet in the collection
            for(timesheet in incommingTimesheets) {
               total += 1
                // for every copy of timesheet, all the parameter are expected to be given
                //if at least one of them is missing, the system will not persist  such time sheet in database
                //however it will be nice to show the total number of timesheet successfully persisted to tdatabase
                //also check if rate is numeric
                if(timesheet.employeeId == null ||
                        timesheet.rate == null || timesheet.date ==null ||
                        timesheet.startTime==null || timesheet.endTime==null || timesheet.project==null || !isDouble(timesheet.rate)) {
                    // do nothing
                    //this timesheet will not be committed to the database
                    //because one of the parameters is missing.
                }else {
                    //if we got here, we will know all is well and this timesheet has fulfull all righteousness
                    //let try and persist it to database
                    saveTimesheet(timesheet)
                }
            }
        }else{
            //it will be nice if the system send a friendly message to user as per what went wrong
            jobUploadResponseResource.message = "Something went wrong, one of the timesheet " +
                    "parameters is not given, please check and try again"
            return ResponseEntity(jobUploadResponseResource, HttpStatus.BAD_REQUEST)
        }
        //let's return a friendly message to user indicating that the operation was a success
        jobUploadResponseResource.totalJobs = total
        jobUploadResponseResource.message = "Operation was successful, $total Timesheet(s) were processed successfully"
        return ResponseEntity(jobUploadResponseResource, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    // it takes jobId to return timesheet entity
    fun getTimesheetById(@PathVariable id:Int) : ResponseEntity<IncommingTimesheetResource>
    {
        var incommingTimesheet: IncommingTimesheetResource = timesheetService.getSingleTimesheet(id)
        return ResponseEntity(incommingTimesheet, HttpStatus.OK)
    }

    /**
     * @param employeeId : 1001
     * @from date format: 2019-01-01
     * @to data format: 2019-02-02
     * @return return list of timesheet for this employee
     */
    @PostMapping("/employee")
    fun generateTimesheetForLawyer(@RequestBody employeeTimesheetRequest: EmployeeTimesheetRequest) :
            ResponseEntity<TimesheetDataResource> {
        val timesheetList = mutableListOf<IncommingTimesheetResource>()
        var timesheetDataResource: TimesheetDataResource = TimesheetDataResource()
        if(employeeTimesheetRequest.employeeId == null || employeeTimesheetRequest.from == null || employeeTimesheetRequest.to==null) {
            // do nothing
            //response empty list
            //give user a friendly message that something went wrong
            timesheetDataResource.message = "Oops! Something went wrong, please check the fields, ensure all fill accordingly"
            timesheetDataResource.data = timesheetList
            return ResponseEntity(timesheetDataResource, HttpStatus.BAD_REQUEST)
        }
        timesheetDataResource =  timesheetService.getWeeklyTimesheetForEmployee(employeeTimesheetRequest)
        return ResponseEntity(timesheetDataResource, HttpStatus.OK)
    }

    /**
     * @param company name
     * @return this will return jobs done for this particular company
     */
    @GetMapping("/invoices/{company}")
    fun generateCompanyInvoice(@PathVariable company:String) :  ResponseEntity<CompanyInvoiceDataResource> {
        var companyInvoiceData = timesheetService.getCompanyInvoice(company)
        return ResponseEntity(companyInvoiceData, HttpStatus.OK)
    }


    private fun saveTimesheet(timesheet: IncommingTimesheetResource) : Unit {
        var theTimesheet = TimesheetResource();
        theTimesheet.employeeId = timesheet.employeeId
        theTimesheet.project = timesheet.project
        theTimesheet.amount = timesheet.rate.toDouble()
        theTimesheet.setStarttime(timesheet.startTime)
        theTimesheet.setEndtime(timesheet.endTime)
        theTimesheet.date = timesheet.date
//        println( "employee ID: ${theTimesheet.employeeId}," +
//                " rate:${theTimesheet.amount}, project: " +
//                "${theTimesheet.project}, date:" +
//                " ${theTimesheet.date}, startTime: ${theTimesheet.startTime}, endTime: ${theTimesheet.endTime} ")
        timesheetService.addNewTimesheets(theTimesheet)
    }

    private fun isDouble(str: String?) = str?.toDoubleOrNull()?.let { true } ?: false
}