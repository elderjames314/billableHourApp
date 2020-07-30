package com.techustle.billablehour.v1.backend.repository

import com.techustle.billablehour.v1.backend.model.Employee
import com.techustle.billablehour.v1.backend.model.Job
import com.techustle.billablehour.v1.backend.model.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JobRepository : JpaRepository<Job, Int> {
    //return employee jobs between date, it is expected to be weekly, that by inputing employee ID
    //start weekdate and end weekdate
    @Query("select j from Job j where j.employee = :employee and (j.created BETWEEN :from and :to)")
    fun findJobBetweenCreatedDate(employee: Employee, from: Date, to:Date) : List<Job>

    //this will get all jobs/timesheet as per company name passed in
    fun findJobByProject(project: Project) : List<Job>
}