package com.techustle.billablehour.v1.backend.service

import com.techustle.billablehour.v1.backend.model.Bill
import com.techustle.billablehour.v1.backend.model.Employee
import com.techustle.billablehour.v1.backend.model.Job
import com.techustle.billablehour.v1.backend.model.Project
import com.techustle.billablehour.v1.backend.repository.BillRepository
import com.techustle.billablehour.v1.backend.repository.EmployeeRepository
import com.techustle.billablehour.v1.backend.repository.JobRepository
import com.techustle.billablehour.v1.backend.repository.ProjectRepository
import com.techustle.billablehour.v1.backend.resource.EmployeeTimesheetRequest
import com.techustle.billablehour.v1.backend.resource.TimesheetResource
import com.techustle.billablehour.v1.backend.utility.Utils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class TimesheetServiceTests {
	@InjectMocks
	lateinit var timesheetService : TimesheetService
	@Mock
	lateinit var projectRepository: ProjectRepository

	@Mock
	lateinit var jobRepository: JobRepository

	@Mock
	lateinit var employeeRepository: EmployeeRepository

	@Mock
	lateinit var billRepository: BillRepository



	@BeforeEach
	fun setup() {
		MockitoAnnotations.initMocks(this)
	}

	@Test
	fun saveProjectTest() {
		var project: Project = Project()
		project.Id = 1
		project.name = "MTN"
		project.remarks = "Project created for MTN"
		Mockito.`when`(projectRepository.findProjectByName(anyString())).thenReturn(project)
		var proj: Project = timesheetService.saveProject("MTN")
		//check if the return project is not null
		Assertions.assertNotNull(proj)
		Assertions.assertEquals("MTN", proj.name)
	}
	@Test
	fun saveJobTest() {

		var job = Job()

		var employee = Employee();
		employee.name="name9001"
		employee.employeeId = 9001
		job.employee = employee


		var project = Project()
		project.name = "MTN"
		project.remarks="MTN created successfully"
		job.project = project


		var utils = Utils()
		var timesheetResource = TimesheetResource()
		timesheetResource.startTime = utils.formatTime("09:00")
		timesheetResource.endTime = utils.formatTime("17:00")
		timesheetResource.hourWorked = utils.getHourDiffBetweenTimes(utils.formatTime("09:00"), utils.formatTime("17:00"))
		timesheetResource.date = utils.formateDate("2020-01-02")
		job.startTime = timesheetResource.startTime
		job.endTime = timesheetResource.endTime
		job.hour = timesheetResource.hourWorked
		job.created = timesheetResource.date

		var response = 1


		Mockito.`when`(jobRepository.save(any(Job::class.java))).thenReturn(job)
		response = timesheetService.saveJob(employee, project, timesheetResource)
		assertEquals(1, response)

	}

	@Test
	fun saveEmployeeTest() {
		var employee = Employee();
		employee.name="name9001"
		employee.employeeId = 9001

		var utils = Utils()
		var timesheetResource = TimesheetResource()
		timesheetResource.startTime = utils.formatTime("09:00")
		timesheetResource.endTime = utils.formatTime("17:00")
		timesheetResource.hourWorked = utils.getHourDiffBetweenTimes(utils.formatTime("09:00"), utils.formatTime("17:00"))
		timesheetResource.date = utils.formateDate("2020-01-02")

		Mockito.`when`(employeeRepository.findEmployeeByEmployeeId(any(Int::class.java))).thenReturn(employee)

		var response =  timesheetService.saveEmployee(timesheetResource)
		assertEquals(employee.name, response.name)
		assertEquals(employee.employeeId, response.employeeId)
	}

	@Test
	fun saveBillTest() {

		var employee = Employee();
		employee.name="name9001"
		employee.employeeId = 9001

		var bill = Bill()
		bill.grade = "Lawyer"
		bill.amount =300.0.toBigDecimal()
		bill.employee = employee
		bill.remark = "bill created"
		bill.hour = 1

		Mockito.`when`(billRepository.save(any(Bill::class.java))).thenReturn(bill)

		var response =  timesheetService.saveBill(bill.amount, employee)

		assertEquals(bill.grade, response.grade)
		assertEquals(bill.amount, response.amount)
		assertEquals(bill.hour, response.hour)
	}

	@Test
	fun getWeeklyTimesheetForEmployeeTest() {
		var employee = Employee();
		employee.name="name9001"
		employee.employeeId = 9001

		var utils = Utils()
		var employeeTimesheetRequest = EmployeeTimesheetRequest()
		employeeTimesheetRequest.employeeId=1001
		employeeTimesheetRequest.from = utils.formateDate("2019-01-12")
		employeeTimesheetRequest.to = utils.formateDate("2019-01-12")


		Mockito.`when`(employeeRepository.findEmployeeByEmployeeId(any(Int::class.java))).thenReturn(employee)
		var response =  timesheetService.getWeeklyTimesheetForEmployee(employeeTimesheetRequest)
		assertNotNull(response)
	}

	@Test
	fun getTimesheetTest() {

		val job = Optional.of(Job())

		var employee = Employee();
		employee.name="name9001"
		employee.employeeId = 9001
		job.get().employee = employee


		var project = Project()
		project.name = "MTN"
		project.remarks="MTN created successfully"
		job.get().project = project


		var utils = Utils()
		var timesheetResource = TimesheetResource()
		timesheetResource.startTime = utils.formatTime("09:00")
		timesheetResource.endTime = utils.formatTime("17:00")
		timesheetResource.hourWorked = utils.getHourDiffBetweenTimes(utils.formatTime("09:00"), utils.formatTime("17:00"))
		timesheetResource.date = utils.formateDate("2020-01-02")
		job.get().startTime = timesheetResource.startTime
		job.get().endTime = timesheetResource.endTime
		job.get().hour = timesheetResource.hourWorked
		job.get().created = timesheetResource.date



		Mockito.`when`(jobRepository.findById(any(Int::class.java))).thenReturn(job)

		var response  = timesheetService.getTimesheet(job.get().Id)

		assertEquals(job.get().hour, response.hourWorked)
		assertEquals(job.get().created, response.date)
	}

	@Test
	fun getCompanyInvoiceTest() {
		val project: Project? = Project()
		project?.name = "MTN"
		project?.remarks="MTN created successfully"


		Mockito.`when`(projectRepository.findProjectByName(anyString())).thenReturn(project)

		var response =  timesheetService.getCompanyInvoice(anyString())

		assertEquals(project?.name, response.company)
	}


}
