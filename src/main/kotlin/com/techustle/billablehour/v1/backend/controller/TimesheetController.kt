package com.techustle.billablehour.v1.backend.controller

import com.techustle.billablehour.v1.backend.resource.*
import com.techustle.billablehour.v1.backend.service.TimesheetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.AbstractBindingResult
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.ConstraintViolationException
import javax.validation.Valid


@RestController
@CrossOrigin(origins = arrayOf("http://localhost:8080"))
@RequestMapping("/timesheets")
class TimesheetController {

    @Autowired
    lateinit var timesheetService: TimesheetService
    /**
     * @return list of timesheets
     * @param List of timesheets
     */
    @PostMapping
    fun addTimesheet(@Valid @RequestBody incommingTimesheets: List<IncommingTimesheetResource>) : ResponseEntity<JobUploadResponseResource> {
        var total:Int = 0
        var jobUploadResponseResource  = JobUploadResponseResource()

        if(incommingTimesheets.isNotEmpty()) {
            for(timesheet in incommingTimesheets) {
                total += saveTimesheet(timesheet)
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
        var timesheetDataResource = TimesheetDataResource()
        if(employeeTimesheetRequest.employeeId == null || employeeTimesheetRequest.from == null || employeeTimesheetRequest.to==null)
            return ResponseEntity(timesheetDataResource, HttpStatus.BAD_REQUEST)
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
        if(companyInvoiceData.data.isEmpty())
            return ResponseEntity(companyInvoiceData, HttpStatus.NOT_FOUND)
        return ResponseEntity(companyInvoiceData, HttpStatus.OK)
    }


    private fun saveTimesheet(timesheet: IncommingTimesheetResource) : Int {
        var theTimesheet = TimesheetResource();
        theTimesheet.employeeId = timesheet.employeeId
        theTimesheet.project = timesheet.project
        theTimesheet.amount = timesheet.rate.toBigDecimal()
        theTimesheet.setStarttime(timesheet.startTime)
        theTimesheet.setEndtime(timesheet.endTime)
        theTimesheet.date = timesheet.date
//        println( "employee ID: ${theTimesheet.employeeId}," +
//                " rate:${theTimesheet.amount}, project: " +
//                "${theTimesheet.project}, date:" +
//                " ${theTimesheet.date}, startTime: ${theTimesheet.startTime}, endTime: ${theTimesheet.endTime} ")
        return timesheetService.addNewTimesheets(theTimesheet)
    }


}

