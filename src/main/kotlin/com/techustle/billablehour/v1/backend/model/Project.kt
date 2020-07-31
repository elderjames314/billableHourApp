package com.techustle.billablehour.v1.backend.model

import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import javax.persistence.*
import javax.validation.constraints.NotEmpty


@Entity
@Table(name = "projects")
class Project{
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var Id:Int = 0
        @NotEmpty(message = "Project name can not be emptied")
        lateinit var name:String
        var remarks:String? = null
}