package com.techustle.billablehour.v1.backend.utility

import org.aspectj.util.UtilClassLoader
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension

@Configuration
class UtilityTests {

	@Autowired
	lateinit var utils: Utils

	@BeforeEach
	fun setup() {
		utils = Utils()
	}

	@Test
	fun formatDateTest() {
		var date = utils.formateDate("2019-02-02")
		assertNotNull(date)
	}

}
