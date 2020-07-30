package com.techustle.billablehour.v1.backend.model

import javax.persistence.*

@Entity
@Table(name = "bills")
class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var Id:Int = 0
    @OneToOne
    lateinit var employee : Employee
    var grade:String? = null
    @Column(name = "hour", nullable = false, columnDefinition = "int default 1")
    var hour:Int = 0
    var amount:Double = 0.0
    var remark:String? = null

}
