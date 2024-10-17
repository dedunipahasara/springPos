let baseUrl = "http://localhost:8081/demo/";

$(document).ready(function () {
    loadAllCustomer();
});

// Load all customers from the server
function loadAllCustomer() {
    $("#customerTable").empty();
    $.ajax({
        url: baseUrl + "customer",
        method: "GET",
        dataType: "json",
        success: function (res) {
            for (let customer of res) {
                let row = `
                    <tr>
                        <td>${customer.customerId}</td>
                        <td>${customer.customerName}</td>
                        <td>${customer.customerAddress}</td>
                        <td>${customer.customerSalary}</td>
                    </tr>`;
                $("#customerTable").append(row);
            }
            bindClickEvents();
            generateCustomerID();
            setTextFieldValues("", "", "", "");
        },
        error: function (error) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Error loading customers!',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }
    });
}

// Generate customer ID
function generateCustomerID() {
    $.ajax({
        url: baseUrl + "customer/generateCustomerId",
        method: "GET",
        success: function (resp) {
            console.log("Generated Customer ID:", resp); // Log the response
            $("#txtCusId").val(resp.id); // Set the new customer ID in the form field
        },
        error: function (error) {
            console.log("Error generating customer ID:", error); // Log the error details
            Swal.fire({
                icon: 'error',
                title: 'Error generating customer ID',
                text: error.responseText || 'Unknown error occurred'
            });
        }
    });
}
// Save a new customer
$("#btnSaveCustomer").click(function () {
    let customerOb = {
        customerId: $("#txtCusId").val(),
        customerName: $("#txtCusName").val(),
        customerAddress: $("#txtCusAddress").val(),
        customerSalary: $("#txtCustomerSalary").val()
    };

    $.ajax({
        url: baseUrl + "customer",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(customerOb),
        success: function () {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Customer saved successfully!',
                showConfirmButton: false,
                timer: 1500
            });
            loadAllCustomer();
            generateCustomerID(); // Generate the next customer ID after saving
        },
        error: function (error) {
            Swal.fire({
                icon: 'error',
                title: 'Error saving customer',
                text: error.responseText
            });
        }
    });
});
// Update an existing customer
$("#btnUpdateCustomer").click(function () {
    let customerOb = {
        customerId: $("#txtCusId").val(),
        customerName: $("#txtCusName").val(),
        customerAddress: $("#txtCusAddress").val(),
        customerSalary: $("#txtCustomerSalary").val()
    };

    $.ajax({
        url: baseUrl + "customer/" + customerOb.customerId,
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(customerOb),
        success: function () {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Customer updated successfully!',
                showConfirmButton: false,
                timer: 1500
            });
            loadAllCustomer();
        },
        error: function (error) {
            Swal.fire({
                icon: 'error',
                title: 'Error updating customer',
                text: error.responseText
            });
        }
    });
});

// Attach click event to the table rows
function bindClickEvents() {
    $("#customerTable tr").click(function () {
        let id = $(this).children().eq(0).text();
        let name = $(this).children().eq(1).text();
        let address = $(this).children().eq(2).text();
        let salary = $(this).children().eq(3).text();

        setTextFieldValues(id, name, address, salary);
        $("#btnSaveCustomer").attr("disabled", true);  // Disable save while updating or deleting
        $("#btnUpdateCustomer").attr("disabled", false);
        $("#btnDeleteCustomer").attr("disabled", false);
    });
}

// Set input field values
function setTextFieldValues(id, name, address, salary) {
    $("#txtCusId").val(id);
    $("#txtCusName").val(name);
    $("#txtCusAddress").val(address);
    $("#txtCustomerSalary").val(salary);
}
// Validate form inputs before enabling the buttons
$("#txtCusName, #txtCusAddress, #txtCustomerSalary").on('keyup', function () {
    validateForm();
});

function validateForm() {
    let name = $("#txtCusName").val();
    let address = $("#txtCusAddress").val();
    let salary = $("#txtCustomerSalary").val();

    let isValid = name.length > 0 && address.length > 0 && !isNaN(salary);

    // Enable/disable buttons based on validation
    $("#btnSaveCustomer").attr("disabled", !isValid);
    $("#btnUpdateCustomer").attr("disabled", !isValid);
    $("#btnDeleteCustomer").attr("disabled", !isValid);
}

// Delete a customer with confirmation
$("#btnDeleteCustomer").click(function () {
    let customerId = $("#txtCusId").val();

    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: baseUrl + "customer/" + customerId,
                method: "DELETE",
                success: function () {
                    Swal.fire(
                        'Deleted!',
                        'Customer has been deleted.',
                        'success'
                    );
                    loadAllCustomer();
                },
                error: function (error) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error deleting customer',
                        text: error.responseText
                    });
                }
            });
        }
    });
});
