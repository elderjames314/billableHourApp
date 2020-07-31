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
class GetCompanyInvoiceTests {

	val CONTEXT_PATH: String = "/billablehour/v1"

	@BeforeEach
	fun setup() {
		RestAssured.baseURI = "http://localhost"
		RestAssured.port = 8080
	}

	@Test
	fun getCompanyInvoiceByCompanyName() {
		var companyName = "MTN" //first ensure this company name is present in that database
		var response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.`when`()
				.get(CONTEXT_PATH+"/timesheets/invoices/$companyName")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.extract()
				.response()
		var company = response.jsonPath().getString("company")
		var invoices = response.jsonPath().getString("data")
		assertEquals(companyName, company)
		assertTrue(invoices.count() >= 1)
	}

	@Test
	fun getCompanyInvoiceByUnknowCompanyNameResultIn404() {
		var companyName = "NAME NOT IN DATABASE" //first ensure this company name is present in that database
		var response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.`when`()
				.get(CONTEXT_PATH+"/timesheets/invoices/$companyName")
				.then()
				.statusCode(404)
				.contentType(ContentType.JSON)
				.extract()
				.response()
		var company = response.jsonPath().getString("company")
		var invoices = response.jsonPath().getString("data")
		assertEquals("", company)
	}

}
