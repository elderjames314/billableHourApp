package com.techustle.billablehour.v1.backend.controller

import com.techustle.billablehour.v1.backend.resource.*
import com.techustle.billablehour.v1.backend.service.TimesheetService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
    @ApiOperation( tags = arrayOf("Add timesheets", "Upload lawyer timesheets"), value = "Upload employee/lawyer timesheets. takes array of timesheets. POST: localhost:8080/billablehour/v1/timesheets"
    , response = JobUploadResponseResource::class,
            notes = "Please note for every timesheet, all parameters must be filled. example of request is " +
                    "[\n" +
                        "    {\n" +
                        "        \"employeeId\" : 5001,\n" +
                        "        \"rate\":300,\n" +
                        "        \"project\": \"MTN\",\n" +
                        "        \"date\" : \"2019-09-09\",\n" +
                        "        \"startTime\": \"09:00\",\n" +
                        "        \"endTime\" : \"17:00\"\n" +
                        "    },\n" +
                        "     {\n" +
                        "        \"employeeId\" : 5001,\n" +
                        "        \"rate\":300,\n" +
                        "        \"project\": \"Fidelity\",\n" +
                        "        \"date\" : \"2020-01-01\",\n" +
                        "        \"startTime\": \"09:00\",\n" +
                        "        \"endTime\" : \"17:00\"\n" +
                        "    }\n" +
                    "     \n" +
                    "]")
    fun addTimesheet(@Valid @RequestBody timesheets: List<Timesheet>) : ResponseEntity<JobUploadResponseResource> {
        var total:Int = 0
        var jobUploadResponseResource  = JobUploadResponseResource()

        if(timesheets.isNotEmpty()) {
            for(theTimesheet in timesheets) {
                total += saveTimesheet(theTimesheet)
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

    @ApiOperation(tags = arrayOf("get timesheet queries", "Single timesheet queries"), value = "This is API endpoint that responsibles for getting a timesheet " +
            "given job ID. GET: localhost:8080/billablehour/v1/timesheets/{id}",
            notes = "Please note that ID must not be zero and if ID is not in our database, " +
                    "it will return null.", response = IncommingTimesheetResource::class)
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
    @ApiOperation(tags = arrayOf("generate timesheet queries", "Lawyer timesheet queries"), value = "Generating weekly employee timesheet given employeeID, start and end date " +
            "POST: localhost:8080/billablehour/v1/timesheets/employee",
            notes = "Please note all the parameters must be filled,  it will give error if one of the parameters is missing/empty" +
                    "\n{\n" +
                    "    \"employeeId\": 1001,\n" +
                    "    \"from\" : \"2019-01-12\",\n" +
                    "    \"to\" : \"2019-01-12\"\n" +
                    "}" +
                    "", response = TimesheetDataResource::class)
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
    @ApiOperation(tags = arrayOf("Company invoice queries"), value = "Generating company invoices given company name" +
            "GET: localhost:8080/billablehour/v1/timesheets/invoices/{companyName}",
            notes = "Please note that it will turn empty if company name is not found in the database or empty " +
                    "", response = CompanyInvoiceDataResource::class)
    @GetMapping("/invoices/{company}")
    fun generateCompanyInvoice(@PathVariable company:String) :  ResponseEntity<CompanyInvoiceDataResource> {
        var companyInvoiceData = timesheetService.getCompanyInvoice(company)
        if(companyInvoiceData.data.isEmpty())
            return ResponseEntity(companyInvoiceData, HttpStatus.NOT_FOUND)
        return ResponseEntity(companyInvoiceData, HttpStatus.OK)
    }


    private fun saveTimesheet(timesheet: Timesheet) : Int {
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

