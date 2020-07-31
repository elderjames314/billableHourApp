$(document).ready(function(){
     $('#tableCompany').slideUp()
     $('#employeeTimesheet').slideUp()
    $(".projectForm .invoiceBtn").on('click', function(event){

          event.preventDefault();


          var projectName =  $(".projectName").val()
          if(projectName=="")
          {
            alert("Please select project")
          }

          //hide company table

          $('#message').text("Fetching company invoice...please wait.")

          $.get("/billablehour/v1/timesheets/invoices/"+projectName, function(data, status){
            $("#tableCompany #company").text(data.company)
            $("#tableCompany #totalInvoice").text(data.total)
            var body ="";
            $.each(data.data, function(index, element) {
                body += "<tr>";
                    body += "<td>"+element.employeeId+"</td>";
                    body += "<td>"+element.numberOfHours+"</td>";
                    body += "<td>"+element.unitPrice+"</td>";
                    body += "<td>"+element.cost+"</td>";
                body += "</tr>";
            });
            $('#tableCompany').fadeIn();
            $('#employeeTimesheet').slideUp()
            $('#message').text("");
            $("#companyDataTable tbody").html(body)
          });

    })


    // generate employee timesheet
    $('.timesheetForm .genEmpTimesheet').click(function(event) {
        event.preventDefault();

        var request = {
            employeeId: $('.timesheetForm .empname').val(),
            from: $('.timesheetForm .fromdate').val(),
            to: $('.timesheetForm .todate').val()
        }

        if(request.employeeId==null || request.employeeId=="") {
            alert("Please select employee name")
        }else if(request.from==null || request.from=="") {
            alert("Please select from date")
        }else if(request.to == null || request.to=="") {
            alert("please select to date field")
        }else {
            //process the operations
            console.log(request)
            $.ajax({
                url: '/billablehour/v1/timesheets/employee',
                dataType: 'json',
                type: 'post',
                contentType: 'application/json',
                data: JSON.stringify(request),
                processData: false,
                success: function( data, textStatus, jQxhr ){
                    console.log(data)
                    var body ="";
                    $.each(data.data, function(index, element) {
                        body += "<tr>";
                            body += "<td>"+element.employeeId+"</td>";
                            body += "<td>"+element.rate+"</td>";
                            body += "<td>"+element.project+"</td>";
                            body += "<td>"+element.date+"</td>";
                            body += "<td>"+element.startTime+"</td>";
                            body += "<td>"+element.endTime+"</td>";
                            body += "<td>"+element.hourWorked+"</td>";
                            body += "<td>"+element.total+"</td>";
                        body += "</tr>";
                    });
                    $('#employeeTimesheet #employee').text(request.employeeId + " between "+ request.from + " and "+ request.to)
                    $('#employeeTimesheet #totalTimesheet').text(data.totalTimesheet)
                    $('#tableCompany').slideUp();
                    $('#employeeTimesheet').fadeIn()
                    $('#message').text("");
                    $("#employeeTimesheetTable tbody").html(body)
                },
                error: function( jqXhr, textStatus, errorThrown ){
                    $('#message').text("Something went wrong, please try again");
                }
            });
        }
    })


    //update todate base on from date selected. add 7 days
    $('.timesheetForm .fromdate').change(function() {
        var fromdate = $(this).val()
        var days = 7
        var date = new Date(fromdate)
        var todate = new Date(date.getTime() + (days * 24 * 60 * 60 * 1000));
        var day =("0" + todate.getDate()).slice(-2);
        var month=("0" + (todate.getMonth() + 1)).slice(-2);
        var year=todate.getFullYear();
        var weekdate=year+"-"+month+"-"+day;
        $('.timesheetForm .todate').val(weekdate)

    })

})
