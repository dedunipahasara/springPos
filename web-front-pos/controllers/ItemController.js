let baseUrl = "http://localhost:8081/demo/";
loadAllItems();

// Load all items from the server
function loadAllItems() {
    $("#ItemTable").empty();
    $.ajax({
        url: baseUrl + "item",
        method: "GET",
        dataType: "json",
        success: function (res) {
            for (let item of res) {
                let row = `
                    <tr>
                        <td>${item.itemId}</td>
                        <td>${item.itemName}</td>
                        <td>${item.itemQty}</td>
                        <td>${item.itemPrice}</td>
                    </tr>`;
                $("#ItemTable").append(row);
            }
            bindClickEvents();
            generateItemID();
            setTextFieldValues("", "", "", "");
        },
        error: function (error) {
            Swal.fire({
                icon: 'error',
                title: 'Error loading items',
                text: 'Unable to load items from the server!'
            });
        }
    });
}

// Save a new item
$("#btnAddItem").click(function () {
    let itemOb = {
        itemId: $("#txtItemID").val(),
        itemName: $("#txtItemName").val(),
        itemQty: $("#txtItemQty").val(),
        itemPrice: $("#txtItemPrice").val()
    };

    $.ajax({
        url: baseUrl + "item",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(itemOb),
        success: function () {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Item saved successfully!',
                showConfirmButton: false,
                timer: 1500
            });
            loadAllItems();
        },
        error: function (error) {
            Swal.fire({
                icon: 'error',
                title: 'Error saving item',
                text: JSON.parse(error.responseText).message
            });
        }
    });
});

// Update an existing item
$("#btnUpdateItem").click(function () {
    let itemOb = {
        itemId: $("#txtItemID").val(),
        itemName: $("#txtItemName").val(),
        itemQty: $("#txtItemQty").val(),
        itemPrice: $("#txtItemPrice").val()
    };

    $.ajax({
        url: baseUrl + "item/" + itemOb.itemId,
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(itemOb),
        success: function () {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Item updated successfully!',
                showConfirmButton: false,
                timer: 1500
            });
            loadAllItems();
        },
        error: function (error) {
            Swal.fire({
                icon: 'error',
                title: 'Error updating item',
                text: JSON.parse(error.responseText).message
            });
        }
    });
});

// Delete an item
$("#btnDeleteItem").click(function () {
    let itemId = $("#txtItemID").val();

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
                url: baseUrl + "item/" + itemId,
                method: "DELETE",
                success: function () {
                    Swal.fire(
                        'Deleted!',
                        'Item has been deleted.',
                        'success'
                    );
                    loadAllItems();
                },
                error: function (error) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error deleting item',
                        text: JSON.parse(error.responseText).message
                    });
                }
            });
        }
    });
});

// Generate item ID
function generateItemID() {
    $.ajax({
        url: baseUrl + "item/generateItemId",
        method: "GET",
        success: function (resp) {
            $("#txtItemID").val(resp.itemId);
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Error generating item ID'
            });
        }
    });
}

// Set input field values
function setTextFieldValues(itemId, itemName, itemQty, itemPrice) {
    $("#txtItemID").val(itemId);
    $("#txtItemName").val(itemName);
    $("#txtItemQty").val(itemQty);
    $("#txtItemPrice").val(itemPrice);
}

// Attach click event to the table rows
function bindClickEvents() {
    $("#ItemTable>tr").on("click", function () {
        let itemId = $(this).children().eq(0).text();
        let itemName = $(this).children().eq(1).text();
        let itemQty = $(this).children().eq(2).text();
        let itemPrice = $(this).children().eq(3).text();

        setTextFieldValues(itemId, itemName,itemQty, itemPrice);
        $("#btnAddItem").attr("disabled", true);  // Disable add while updating or deleting
        $("#btnUpdateItem").attr("disabled", false);
        $("#btnDeleteItem").attr("disabled", false);
    });
}

// Validate form inputs before enabling the buttons
$("#txtItemName, #txtItemQty, #txtItemPrice").on('keyup', function () {
    validateForm();
});

function validateForm() {
    let name = $("#txtItemName").val();
    let qty = $("#txtItemQty").val();
    let price = $("#txtItemPrice").val();

    let isValid = name.length > 0 && qty.length > 0 && price.length > 0 && !isNaN(price);

    // Enable/disable buttons based on validation
    $("#btnAddItem").attr("disabled", !isValid);
    $("#btnUpdateItem").attr("disabled", !isValid);
    $("#btnDeleteItem").attr("disabled", !isValid);
}
