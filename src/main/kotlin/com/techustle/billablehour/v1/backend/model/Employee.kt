package com.techustle.billablehour.v1.backend.model

import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import javax.persistence.*
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "employees")
class Employee{
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var Id: Int = 0
        var employeeId: Int = 0
        @OneToOne
        var bill: Bill? = null
        @NotEmpty(message = "Employee name can not be emptied")
        var name: String = ""
        var phone: String? = null
}