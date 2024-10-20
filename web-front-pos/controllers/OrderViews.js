
l
let baseUrl = "http://localhost:8081/demo/";
loadAllOrders();
loadAllOrderDetails();

function loadAllOrders() {
    $("#tblOrder").empty();
    $.ajax({
        url: baseUrl + "orders?option=LoadOrders", method: "GET", dataType: "json", success: function (res) {
            console.log(res);

            for (let i of res.data) {
                let orderId = i.orderId;
                let date = i.date;
                let cusId = i.cusId;

                let row = "<tr><td>" + orderId + "</td><td>" + date + "</td><td>" + cusId + "</td></tr>";
                $("#tblOrder").append(row);
            }
            console.log(res.message);
        }, error: function (error) {
            let message = JSON.parse(error.responseText).message;
            console.log(message);
        }

    });
}

function loadAllOrderDetails() {
    $("#tblOrderDetails").empty();
    $.ajax({
        url: baseUrl + "orders?option=LoadOrderDetails", method: "GET", dataType: "json", success: function (res) {
            console.log(res);

            for (let i of res.data) {
                let OrderId = i.OrderId;
                let code = i.code;
                let qty = i.qty;
                let unitPrice = i.unitPrice;

                let row = "<tr><td>" + OrderId + "</td><td>" + code + "</td><td>" + qty + "</td><td>" + unitPrice + "</td></tr>";
                $("#tblOrderDetails").append(row);
            }
            console.log(res.message);
        }, error: function (error) {
            let message = JSON.parse(error.responseText).message;
            console.log(message);
        }

    });
}