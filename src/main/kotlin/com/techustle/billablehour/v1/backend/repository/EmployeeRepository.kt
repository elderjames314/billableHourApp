package com.techustle.billablehour.v1.backend.repository

import com.techustle.billablehour.v1.backend.model.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : JpaRepository<Employee, Int> {

    //This below function will return employee of employeeID given
   // @Query("select e from Employee e where e.employeeId = :employeeId")
    fun findEmployeeByEmployeeId(employeeId:Int) : Employee?
}