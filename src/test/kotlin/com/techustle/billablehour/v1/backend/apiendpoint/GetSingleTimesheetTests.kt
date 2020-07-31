package com.techustle.billablehour.v1.backend.apiendpoint

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
class GetSingleTimesheetTests {

	val CONTEXT_PATH: String = "/billablehour/v1"

	@BeforeEach
	fun setup() {
		RestAssured.baseURI = "http://localhost"
		RestAssured.port = 8080
	}

	@Test
	fun getSingleTimesheetByJobId() {
		var jobId = 4 //first ensure this jobId is present in that base
		var response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.`when`()
				.get(CONTEXT_PATH+"/timesheets/$jobId")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.extract()
				.response()
		var employeeId = response.jsonPath().getString("employeeId")
		var project = response.jsonPath().getString("project")
		assertNotNull(employeeId, "employeeID can not be null")
		assertNotNull(project, "Project can not be null")
	}

	@Test
	fun getSingleTimesheetByJobIdNotPresentDatabaseResultIn404() {
		var jobId = 0 //We are certain that this Id is not present in database
		var response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.`when`()
				.get(CONTEXT_PATH+"/timesheets/$jobId")
				.then()
				.statusCode(404)
				.contentType(ContentType.JSON)
				.extract()
				.response()
		var employeeId = response.jsonPath().getString("employeeId")
		assertNull(employeeId)
	}

}
