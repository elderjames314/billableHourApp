package com.techustle.billablehour.v1.backend.controller

import com.techustle.billablehour.v1.backend.resource.IncommingTimesheetResource
import com.techustle.billablehour.v1.backend.resource.TimesheetLinkResource
import com.techustle.billablehour.v1.backend.resource.TimesheetResource
import com.techustle.billablehour.v1.backend.service.TimesheetService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.mockito.ArgumentMatchers.anyString
import org.mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat
import java.util.*

@Configuration
class TimesheetControllerTests {

	@InjectMocks
	lateinit var timesheetController: TimesheetController

	@Mock
	lateinit var timesheetService: TimesheetService

	lateinit var incomingTimesheetResource: IncommingTimesheetResource

	@BeforeEach
	fun setup() {
		MockitoAnnotations.initMocks(this)
		incomingTimesheetResource = IncommingTimesheetResource()
		incomingTimesheetResource.employeeId = 1001
		incomingTimesheetResource.rate = "300.0"
		incomingTimesheetResource.project = "Fidelity"
		val date = SimpleDateFormat("yyyy-mm-dd")
		val datein = date.parse("2019-03-12")
		incomingTimesheetResource.date = datein
		incomingTimesheetResource.startTime = "09:00"
		incomingTimesheetResource.endTime = "17:00"
		incomingTimesheetResource.hourWorked = 8
		incomingTimesheetResource.total = "2400.0"
		incomingTimesheetResource.href.link="/billablehour/v1/timesheets/1"
	}

	@Test
	fun getTimesheetByIdTest() {
		var jobId: Int = 12
		Mockito.`when`(timesheetService.getSingleTimesheet(12)).thenReturn(incomingTimesheetResource)
		var response = timesheetController.getTimesheetById(jobId)
		assertEquals(200, response.statusCodeValue)
		assertEquals(incomingTimesheetResource.employeeId, response.body?.employeeId)
		assertEquals(incomingTimesheetResource.rate, response.body?.rate)
		assertEquals(incomingTimesheetResource.startTime, response.body?.startTime)
		assertEquals(incomingTimesheetResource.endTime, response.body?.endTime)
		assertEquals(incomingTimesheetResource.href.link, response.body?.href?.link)


	}

}

