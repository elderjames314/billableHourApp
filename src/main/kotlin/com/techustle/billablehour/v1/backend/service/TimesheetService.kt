package com.techustle.billablehour.v1.backend.service

import com.techustle.billablehour.v1.backend.model.Bill
import com.techustle.billablehour.v1.backend.model.Employee
import com.techustle.billablehour.v1.backend.model.Job
import com.techustle.billablehour.v1.backend.model.Project
import com.techustle.billablehour.v1.backend.repository.BillRepository
import com.techustle.billablehour.v1.backend.repository.EmployeeRepository
import com.techustle.billablehour.v1.backend.repository.JobRepository
import com.techustle.billablehour.v1.backend.repository.ProjectRepository
import com.techustle.billablehour.v1.backend.resource.*
import org.aspectj.util.LangUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TimesheetService {

    @Autowired
    lateinit var  employeeRepository: EmployeeRepository
    @Autowired
    lateinit var billRepository: BillRepository
    @Autowired
    lateinit var projectRepository: ProjectRepository
    @Autowired
    lateinit var jobRepository: JobRepository

    /**
     * @param Timesheet
     * @return Timesheet object
     * This function below handle timesheet engine from the employee creation, bill management, project and job
     */
    fun addNewTimesheets(timesheetResource: TimesheetResource): Unit {

        //employee management
        var employee : Employee = saveEmployee(timesheetResource)

       //project management
        var project :Project = saveProject(timesheetResource.project)

        //job management
        saveJob(employee, project, timesheetResource)

        saveJob(employee, project, timesheetResource)

    }

    /**
     * @param Employee object
     * @param Project object
     * @param Timesheet
     * if all things is equal and all parameter are gotten as expected.
     * the system will create the job for this employee(lawyer) nicely
     */
    fun saveJob(employee: Employee, project: Project, timesheetResource: TimesheetResource) : Int {
        var job:Job = Job()
        job.employee = employee
        job.project = project
        job.startTime = timesheetResource.startTime
        job.endTime = timesheetResource.endTime
        job.hour = timesheetResource.hourWorked
        job.created = timesheetResource.date
        job = jobRepository.save(job)

        timesheetResource.href.link = "/billablehour/v1/timesheets/${job.Id}"

        return job.Id;
    }

    /**
     * @param Timesheet object
     * let's check if the this project is in our database to avoid redundancy
     * if not project not exist, create new one, else do nothing
     * @return Project
     */
    fun saveProject(projectName: String) : Project
    {
        var project :Project? = projectRepository.findProjectByName(projectName)
        //check if project is null, if yes, add it to database else do nothing
        if (project == null) {
            project = Project()
            project.name = projectName
            project.remarks = "${projectName} created successfully"
            projectRepository.save(project)
        }
        return project
    }

    /**create employee if employee is not found in the database
    * Please note employee ID must be given
    * since this system has no control on employee ID input. it is assumed that employeeID is unique
     * @param timesheetResource
     * @return Employee object
    */
    fun saveEmployee(timesheetResource: TimesheetResource) : Employee {
        var employee = employeeRepository.findEmployeeByEmployeeId(timesheetResource.employeeId)
        if(employee != null)
            return employee
        else {
            //save employee
            var employee1 : Employee = Employee()
            employee1.employeeId = timesheetResource.employeeId
            employee1.name = "name${timesheetResource.employeeId}"
            employee1 = employeeRepository.save(employee1)

            var bill :Bill = saveBill(timesheetResource, employee1)
            //update employee bill
            employee1.bill = bill
            employeeRepository.save(employee1)
            return employee1
        }
    }

    /**since employee is not found, chances are bill has not been created for him/her too
    * we will now create the bill
     * @param Timesheet
     * @param Employee
     * @return Bill object
    */
    fun saveBill(timesheetResource: TimesheetResource, employee: Employee) : Bill {
        var bill = Bill()
        bill.grade = "Lawyer"
        bill.amount = timesheetResource.amount
        bill.employee = employee
        bill.remark = "bill created"
        bill.hour = 1
        var billCreated : Bill =  billRepository.save(bill)
        return billCreated
    }

    fun getSingleTimesheet(Id:Int) : IncommingTimesheetResource
    {
       return getTimesheet(Id)
    }

    fun getWeeklyTimesheetForEmployee(employeeTimesheetRequest: EmployeeTimesheetRequest) : TimesheetDataResource {
        var employee: Employee? = employeeRepository.findEmployeeByEmployeeId(employeeTimesheetRequest.employeeId)
        val timesheetList = mutableListOf<IncommingTimesheetResource>()
        var timesheetDataResource: TimesheetDataResource = TimesheetDataResource()
        if(employee != null) {
            var jobs:List<Job> =  jobRepository.findJobBetweenCreatedDate(employee,
                    employeeTimesheetRequest.from, employeeTimesheetRequest.to)
            var total:Int = 0
            if(jobs.isNotEmpty()) {
                for(job:Job in jobs) {
                    total += 1
                    timesheetList.add(getTimesheet(job.Id))
                }
            }
            timesheetDataResource.message = "Operation was successful"
            timesheetDataResource.totalTimesheet = "$total timesheet(s) were found"
            timesheetDataResource.data = timesheetList
        }
        return timesheetDataResource
    }

    private fun getTimesheet(jobId: Int) : IncommingTimesheetResource {
        var job: Optional<Job> =  jobRepository.findById(jobId);
        var incommingTimesheet: IncommingTimesheetResource = IncommingTimesheetResource()
        if(job.isPresent) {
            incommingTimesheet.endTime = job.get().endTime.toString()
            incommingTimesheet.startTime = job.get().startTime.toString()
            incommingTimesheet.employeeId = job.get().employee.employeeId
            incommingTimesheet.project = job.get().project.name
            var amount = job.get().employee.bill?.amount
            var totalAmount = amount?.times(job.get().hour)
            incommingTimesheet.total = String.format("%.2f", totalAmount)
            incommingTimesheet.rate = amount.toString()
            incommingTimesheet.date = job.get().created
            incommingTimesheet.hourWorked = job.get().hour
            incommingTimesheet.href.link = "billablehour/v1/timesheets/${job.get().Id}"
        }
        return incommingTimesheet
    }

    fun getCompanyInvoice(company: String): CompanyInvoiceDataResource {
        var project:Project? = projectRepository.findProjectByName(company)
        var companyInvoiceDataResource : CompanyInvoiceDataResource = CompanyInvoiceDataResource()
        if(project != null) {
            var count:Int = 0
            var jobs: List<Job> = jobRepository.findJobByProject(project)
            for(job in jobs) {
                count += 1
                var companyInvoice: CompanyInvoiceResource = CompanyInvoiceResource()
                companyInvoice.employeeId = job.employee.employeeId
                companyInvoice.numberOfHours = job.hour
                var unitCost : Double? = job.employee.bill?.amount
                companyInvoice.unitPrice = unitCost.toString()
                var total = job.hour * unitCost!!
                companyInvoice.cost = total.toString()
                companyInvoiceDataResource.data.add(companyInvoice)
            }
            companyInvoiceDataResource.company = project.name
            companyInvoiceDataResource.message = "Operation was successful"
            companyInvoiceDataResource.total = "$count job(s) were found"
        }
        return companyInvoiceDataResource
    }

}