package com.techustle.billablehour

import com.techustle.billablehour.v1.backend.resource.TimesheetResource
import com.techustle.billablehour.v1.backend.service.TimesheetService
import com.techustle.billablehour.v1.backend.utility.Utils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.text.SimpleDateFormat
import java.util.*
import javax.annotation.PostConstruct


@SpringBootApplication
class BillablehourApplication

fun main(args: Array<String>) {
	runApplication<BillablehourApplication>(*args)

}





//this method will run upon starting the server
//this is possible because of @PostContruct annotation
@Component
class MyPostConstructBean {
	@Autowired
	lateinit var  timesheetService: TimesheetService

	@PostConstruct
	fun postContruct() : Unit {
		println("Application runnning....")
		//set default time
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
		//timesheet 1

		var utils = Utils()

		var timesheetResource: TimesheetResource = TimesheetResource()
		timesheetResource.employeeId = 1001
		timesheetResource.amount = 300.0.toBigDecimal()
		timesheetResource.project = "MTN"
		timesheetResource.date = utils.formateDate("2019-01-02")
		timesheetResource.setStarttime("09:00")
		timesheetResource.setEndtime("18:00")
		timesheetService.addNewTimesheets(timesheetResource)


		//timesheet 2

		var timesheetResource2: TimesheetResource = TimesheetResource()
		timesheetResource2.employeeId = 1001
		timesheetResource2.amount = 250.0.toBigDecimal()
		timesheetResource2.project = "Fidelity"
		timesheetResource2.date = utils.formateDate("2019-06-12")
		timesheetResource2.setStarttime("09:00")
		timesheetResource2.setEndtime("14:00")
		timesheetService.addNewTimesheets(timesheetResource2)



		//timesheet 3

		var timesheetResource3: TimesheetResource = TimesheetResource()
		timesheetResource3.employeeId = 1002
		timesheetResource3.amount = 350.0.toBigDecimal()
		timesheetResource3.project = "CBN"
		timesheetResource3.date = utils.formateDate("2019-04-12")
		timesheetResource3.setStarttime("09:00")
		timesheetResource3.setEndtime("16:00")
		timesheetService.addNewTimesheets(timesheetResource3)


		//timesheet 4

		var timesheetResource4: TimesheetResource = TimesheetResource()
		timesheetResource4.employeeId = 1003
		timesheetResource4.amount = 450.0.toBigDecimal()
		timesheetResource4.project = "Guranty Trust Bank"
		timesheetResource4.date = utils.formateDate("2019-03-12")
		timesheetResource4.setStarttime("09:00")
		timesheetResource4.setEndtime("18:00")
		timesheetService.addNewTimesheets(timesheetResource4)
	}

}
