package com.techustle.billablehour.v1.backend.model

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.jetbrains.annotations.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

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
        @javax.validation.constraints.NotNull(message = "Start time cannot be null")
        @NotNull("Start time cannot be null")
        lateinit var startTime : Date

        @javax.validation.constraints.NotNull(message = "End time cannot be null")
        @Temporal(TemporalType.TIME)
        @DateTimeFormat(style = "HH:mm")
        @JsonFormat(pattern = "HH:mm")
        @NotNull("End time cannot be null")
        lateinit var endTime: Date

        @Min(1)
        var hour:Int = 1

        @Temporal(TemporalType.DATE)
        @DateTimeFormat(style = "yyyy-mm-dd", pattern  = "yyyy-mm-dd")
        @NotNull("Date  cannot be null")
        lateinit var created: Date
}