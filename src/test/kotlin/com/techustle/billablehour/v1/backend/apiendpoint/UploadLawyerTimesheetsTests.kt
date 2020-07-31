package com.techustle.billablehour.v1.backend.apiendpoint

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class UploadLawyerTimesheetsTests {

	val CONTEXT_PATH: String = "/billablehour/v1"

	@BeforeEach
	fun setup() {
		RestAssured.baseURI = "http://localhost"
		RestAssured.port = 8080
	}

	@Test
	fun uploadTimesheetTest() {

		val timesheets: MutableList<Map<String, Any>> = ArrayList()

		val timesheet: MutableMap<String, Any> = HashMap()
		timesheet["employeeId"] = 2002
		timesheet["rate"] = 300
		timesheet["project"] = "First bank"
		timesheet["date"] = "2019-04-04"
		timesheet["startTime"] = "09:00"
		timesheet["endTime"] = "17:00"
		timesheets.add(timesheet)

		val timesheet2: MutableMap<String, Any> = HashMap()
		timesheet2["employeeId"] = 3002
		timesheet2["rate"] = 450
		timesheet2["project"] = "Zenith Bank"
		timesheet2["date"] = "2019-05-23"
		timesheet2["startTime"] = "09:00"
		timesheet2["endTime"] = "20:00"
		timesheets.add(timesheet2)

		val timesheet3: MutableMap<String, Any> = HashMap()
		timesheet3["employeeId"] = 3002
		timesheet3["rate"] = 450
		timesheet3["project"] = "Zenith Bank"
		timesheet3["date"] = "2019-05-24"
		timesheet3["startTime"] = "09:00"
		timesheet3["endTime"] = "18:00"
		timesheets.add(timesheet3)

		var response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(timesheets)
				.`when`()
				.post(CONTEXT_PATH+"/timesheets")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.extract()
				.response()

		var totalJobsProcessed = response.jsonPath().getString("totalJobs")
		assertNotNull(totalJobsProcessed)
		assertEquals(3, totalJobsProcessed.toInt())

	}


	@Test
	fun uploadTimesheetWithMissingParameterWillResultToExceptionTest() {

		val timesheets: MutableList<Map<String, Any>> = ArrayList()

		val timesheet: MutableMap<String, Any> = HashMap()
		timesheet["employeeId"] = 2002
		timesheet["rate"] = 300
		timesheet["project"] = ""
		timesheet["date"] = ""
		timesheet["startTime"] = "09:00"
		timesheet["endTime"] = ""
		timesheets.add(timesheet)

		var response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(timesheets)
				.`when`()
				.post(CONTEXT_PATH+"/timesheets")
				.then()
				.statusCode(500)
				.contentType(ContentType.JSON)
				.extract()
				.response()

		var totalJobsProcessed = response.jsonPath().getString("totalJobs")
		assertNull(totalJobsProcessed)


	}

}
