package com.techustle.billablehour.v1.backend.model

import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.hibernate.validator.constraints.Range
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.*

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

    @NotNull(message = "Rate can not be null")
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=3, fraction=2)
    var amount: BigDecimal? = null

    var remark:String? = null

}
