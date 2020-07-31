package com.techustle.billablehour.v1.backend.apiendpoint

import com.techustle.billablehour.v1.backend.resource.IncommingTimesheetResource
import com.techustle.billablehour.v1.backend.resource.TimesheetResource
import com.techustle.billablehour.v1.backend.utility.Utils
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
class GetEmployeeTimesheetTests {

	val CONTEXT_PATH: String = "/billablehour/v1"

	@BeforeEach
	fun setup() {
		RestAssured.baseURI = "http://localhost"
		RestAssured.port = 8080
	}

	@Test
	fun getEmployeeWeeklyTimesheet() {
		val employeeRequest: MutableMap<String, Any> = HashMap()
		employeeRequest["employeeId"] = 1001
		employeeRequest["from"] = "2019-01-12"
		employeeRequest["to"] = "2019-01-12"

		var response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(employeeRequest)
				.`when`()
				.post(CONTEXT_PATH+"/timesheets/employee")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.extract()
				.response()

		var timesheets = response.jsonPath().getString("data")
		assertTrue(timesheets.count() >= 1)
	}

	@Test
	fun getEmployeeWeeklyTimesheetWithUnknowEmployeeID() {
		val employeeRequest: MutableMap<String, Any> = HashMap()
		employeeRequest["employeeId"] = 0  //UNKNOWN EMPLOYEE ID
		employeeRequest["from"] = "2019-01-12"
		employeeRequest["to"] = "2019-01-12"

		var response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(employeeRequest)
				.`when`()
				.post(CONTEXT_PATH+"/timesheets/employee")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.extract()
				.response()

		var message = response.jsonPath().getString("message")
		var totalTimesheet = response.jsonPath().getString("totalTimesheet")
		var timesheets = response.jsonPath().getString("data")
		assertNull(message)
		assertNull(totalTimesheet)

	}


	@Test
	fun getEmployeeWeeklyTimesheetWithIncorrectDates() {
		val employeeRequest: MutableMap<String, Any> = HashMap()
		employeeRequest["employeeId"] = 1001
		employeeRequest["from"] = "2019-01" // INCORRECT DATE
		employeeRequest["to"] = "" // DATE NOT SPECIFIED

		var response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(employeeRequest)
				.`when`()
				.post(CONTEXT_PATH+"/timesheets/employee")
				.then()
				.statusCode(500)
				.contentType(ContentType.JSON)
				.extract()
				.response()

		var message = response.jsonPath().getString("message")
		assertEquals("Oops! Something went wrong!", message)
	}

}
