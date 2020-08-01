package com.techustle.billablehour.v1.backend.controller

import com.techustle.billablehour.v1.backend.model.Job
import com.techustle.billablehour.v1.backend.resource.*
import com.techustle.billablehour.v1.backend.service.TimesheetService
import com.techustle.billablehour.v1.backend.utility.Utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.mockito.ArgumentMatchers.anyString
import org.mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat
import java.util.*
import org.mockito.ArgumentMatchers.any

@Configuration
class TimesheetControllerTests {

	@InjectMocks
	lateinit var timesheetController: TimesheetController

	@Mock
	lateinit var timesheetService: TimesheetService

	lateinit var incomingTimesheetResource: IncommingTimesheetResource

	lateinit var timesheetResources: MutableList<Timesheet>

	lateinit var timesheetDataResource: TimesheetDataResource

	lateinit var employeeTimesheetRequest: EmployeeTimesheetRequest

	lateinit var companyInvoiceDataResource: CompanyInvoiceDataResource

	lateinit var timesheet: Timesheet



	@BeforeEach
	fun setup() {
		var utils = Utils()
		MockitoAnnotations.initMocks(this)
		incomingTimesheetResource = IncommingTimesheetResource()
		incomingTimesheetResource.employeeId = 1001
		incomingTimesheetResource.rate = "300.0"
		incomingTimesheetResource.project = "Fidelity"
		incomingTimesheetResource.date = utils.formateDate("2019-01-01")
		incomingTimesheetResource.startTime = "09:00"
		incomingTimesheetResource.endTime = "17:00"
		incomingTimesheetResource.hourWorked = 8
		incomingTimesheetResource.total = "2400.0"
		incomingTimesheetResource.href.link="/billablehour/v1/timesheets/1"


		var incomingTimesheetResource2 = IncommingTimesheetResource()
		incomingTimesheetResource2.employeeId = 1001
		incomingTimesheetResource2.rate = "300.0"
		incomingTimesheetResource2.project = "Fidelity"
		incomingTimesheetResource2.date = utils.formateDate("2019-01-01")
		incomingTimesheetResource2.startTime = "09:00"
		incomingTimesheetResource2.endTime = "17:00"
		incomingTimesheetResource2.hourWorked = 8
		incomingTimesheetResource2.total = "2400.0"
		incomingTimesheetResource2.href.link="/billablehour/v1/timesheets/1"

		timesheetDataResource.data.add(incomingTimesheetResource)
		timesheetDataResource.data.add(incomingTimesheetResource2)







		employeeTimesheetRequest = EmployeeTimesheetRequest()
		employeeTimesheetRequest.employeeId=1001
		employeeTimesheetRequest.from = utils.formateDate("2019-01-12")
		employeeTimesheetRequest.to = utils.formateDate("2019-01-12")






		timesheetDataResource = TimesheetDataResource()
		timesheetDataResource.message = "Operation was successful"
		timesheetDataResource.totalTimesheet = "1 timesheet(s) were found"
		timesheetResources = ArrayList()
		var timesheetResource = Timesheet()
		timesheetResource.employeeId = 1001
		timesheetResource.rate = "300.0"
		timesheetResource.project = "MTN"
		timesheetResource.date = utils.formateDate("2019-01-02")
		timesheetResource.startTime = "09:00"
		timesheetResource.endTime = "18:00"
		timesheetResources.add(timesheetResource)



		var timesheetResource2 = Timesheet()
		timesheetResource2.employeeId = 1001
		timesheetResource2.rate = "250.0"
		timesheetResource2.project = "Fidelity"
		timesheetResource2.date = utils.formateDate("2019-06-12")
		timesheetResource2.startTime =  "09:00"
		timesheetResource2.endTime = "14:00"
		timesheetResources.add(timesheetResource2)



		companyInvoiceDataResource = CompanyInvoiceDataResource()
		companyInvoiceDataResource.company="MTN"
		companyInvoiceDataResource.total = "1 job(s) were found"
		companyInvoiceDataResource.message = "Operation was successful"

		var companyInvoiceResource = CompanyInvoiceResource()
		companyInvoiceResource.employeeId =  1001
		companyInvoiceResource.numberOfHours =  9
		companyInvoiceResource.unitPrice = "300.00"
		companyInvoiceResource.cost = "2700.00"
		companyInvoiceDataResource.data.add(companyInvoiceResource)

	}

	@Test
	fun getTimesheetByIdTest() {
		var jobId = 12
		Mockito.`when`(timesheetService.getSingleTimesheet(12)).thenReturn(incomingTimesheetResource)
		var response = timesheetController.getTimesheetById(jobId)
		assertEquals(200, response.statusCodeValue)
		assertEquals(incomingTimesheetResource.employeeId, response.body?.employeeId)
		assertEquals(incomingTimesheetResource.rate, response.body?.rate)
		assertEquals(incomingTimesheetResource.startTime, response.body?.startTime)
		assertEquals(incomingTimesheetResource.endTime, response.body?.endTime)
		assertEquals(incomingTimesheetResource.href.link, response.body?.href?.link)


	}
	@Test
	fun addTimesheetTest() {
		var timesheetResourceAny = TimesheetResource()
		var total = 0
		for(list in timesheetResources) {
			Mockito.`when`(timesheetService.addNewTimesheets(timesheetResourceAny)).thenReturn(++total)
		}
		timesheetController.addTimesheet(timesheetResources)
		assertEquals(2, total)

	}

	@Test
	fun generateTimesheetForLawyerTest() {
		Mockito.`when`(timesheetService.getWeeklyTimesheetForEmployee(employeeTimesheetRequest)).thenReturn(timesheetDataResource)
		var response =  timesheetController.generateTimesheetForLawyer(employeeTimesheetRequest)
		assertEquals(200, response.statusCodeValue)
		assertEquals(timesheetDataResource.message, response.body?.message)
		assertEquals(timesheetDataResource.totalTimesheet, response.body?.totalTimesheet)
		assertTrue(response.body?.data?.count()==2)
	}

	@Test
	fun generateCompanyInvoiceTest() {
		var companyName = "MTN"
		Mockito.`when`(timesheetService.getCompanyInvoice(companyName)).thenReturn(companyInvoiceDataResource)
		var response =  timesheetController.generateCompanyInvoice(companyName)
		assertEquals(200, response.statusCodeValue)
		assertEquals(companyName, response.body?.company)
		assertEquals(companyInvoiceDataResource.total, response.body?.total)
		assertEquals(companyInvoiceDataResource.message, response.body?.message)
		assertTrue(response.body?.data?.count() == 1)
	}

}

