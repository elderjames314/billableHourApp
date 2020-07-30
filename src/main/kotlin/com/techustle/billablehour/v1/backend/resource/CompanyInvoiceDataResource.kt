package com.techustle.billablehour.v1.backend.resource

class CompanyInvoiceDataResource {
    var message:String = ""
    var total:String = ""
    var company: String = ""
    var data : MutableList<CompanyInvoiceResource> = mutableListOf<CompanyInvoiceResource>()
}