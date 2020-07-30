package com.techustle.billablehour.v1.backend.repository

import com.techustle.billablehour.v1.backend.model.Bill
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BillRepository : JpaRepository<Bill, Int> {

}