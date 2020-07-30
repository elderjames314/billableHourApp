package com.techustle.billablehour.v1.backend.model

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
@Entity
@Table(name = "jobs")
class Job{
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var Id:Int = 0
        @ManyToOne
        lateinit var employee : Employee
        @ManyToOne
        lateinit var project: Project
        @Temporal(TemporalType.TIME)
        @DateTimeFormat(style = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        lateinit var startTime : Date
        @Temporal(TemporalType.TIME)
        @DateTimeFormat(style = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        lateinit var endTime: Date
        var hour:Int = 0
        @Temporal(TemporalType.DATE)
        @DateTimeFormat(style = "yyyy-mm-dd")
        lateinit var created: Date
}