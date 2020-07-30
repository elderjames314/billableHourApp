package com.techustle.billablehour.v1.backend.model

import javax.persistence.*

@Entity
@Table(name = "projects")
class Project{
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var Id:Int = 0
        lateinit var name:String
        var remarks:String? = null
}