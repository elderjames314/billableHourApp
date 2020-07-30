package com.techustle.billablehour.v1.backend.model

import javax.persistence.*

@Entity
@Table(name = "employees")
class Employee{
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var Id: Int = 0
        var employeeId: Int = 0
        @OneToOne
        var bill: Bill? = null
        var name: String = ""
        var phone: String? = null
}