package com.techustle.billablehour.v1.frontend.controller

import com.techustle.billablehour.v1.backend.model.Employee
import com.techustle.billablehour.v1.backend.model.Project
import com.techustle.billablehour.v1.backend.repository.EmployeeRepository
import com.techustle.billablehour.v1.backend.repository.ProjectRepository
import com.techustle.billablehour.v1.backend.resource.CompanyInvoiceDataResource
import com.techustle.billablehour.v1.backend.resource.EmployeeTimesheetRequest
import com.techustle.billablehour.v1.backend.resource.TimesheetDataResource
import org.dom4j.rule.Mode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.servlet.ModelAndView
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.util.*


@Controller
class HomeController {

    @Autowired
    lateinit var  employeeRepository: EmployeeRepository

    @Autowired
    lateinit var projectRepository: ProjectRepository

    @GetMapping
    fun index (): ModelAndView {
        var employees: List<Employee> = employeeRepository.findAll()
        var projects : List<Project> = projectRepository.findAll()
        var mv:ModelAndView = ModelAndView("index")
        mv.addObject("employees", employees)
        mv.addObject("projects", projects)
        var today:LocalDate = LocalDate.now()
        var weekdate:LocalDate = LocalDate.now().plusDays(7);
        mv.addObject("today", today)
        mv.addObject("weekdate", weekdate)
        return mv
    }



}