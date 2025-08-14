<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Item" %>
<%@ page import="model.Customer" %>
<%
    String nextInvoiceNo = (String) request.getAttribute("invoiceNo");
    List<Item> items = (List<Item>) request.getAttribute("items");
   /*  List<Customer> customers = (List<Customer>) request.getAttribute("customers"); */
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Generate Invoice</title>
    <link rel="stylesheet" href="css/invoiceStyle.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    
    
</head>
<body>

<div class="top-card">
    <h2 class="top-title">Generate Invoice</h2>
</div>

<!-- Form -->
<div class="register-card">
    <h3 class="register-title">Generate New Invoice</h3>
    
    
    <form action="InvoiceServlet" method="post">
        <input type="hidden" name="action" value="insert">

        <div class="form-group">
            <label for="invoiceNo">Invoice No:</label><br>
            <input type="text" id="invoiceNo" name="invoiceNo" value="<%= nextInvoiceNo %>" readonly>
        </div>

      <!--   <div class="form-group">
            <label for="customerName">Customer Name:</label><br>
            <input type="text" id="customerName" name="customerName" required>
        </div>
        
         <div class="form-group">
            <label for="item">Item:</label><br>
            <input type="text" id="item" name="item" required>
        </div> -->
        
        <div class="form-group">
    <label for="customerName">Customer Name:</label><br>
    <input type="text" id="customerName" name="customerName" required 
           placeholder="Start typing customer name..." autocomplete="off">
    <input type="hidden" id="customerId" name="customerId">
    <div id="customerSuggestions" class="autocomplete-suggestions"></div>
</div>



 <div class="form-group">
    <label for="item">Item:</label><br>
    <input type="text" id="item" name="item" required 
           placeholder="Start typing item name..." autocomplete="off">
    <input type="hidden" id="itemId" name="itemId">
    <div id="itemSuggestions" class="autocomplete-suggestions"></div>
</div>

<div class="form-group">
<table id="invoiceTable" border="1">
    <thead>
        <tr>
            <th>Item Code</th>
            <th>Item Name</th>
            <th>Description</th>
            <th>Price</th>
            <th>Qty</th>
            <th>Total</th>
            <th>Action</th>
            
        </tr>
    </thead>
    <tbody></tbody>
</table>
</div>



        <div class="form-group">
            <label for="invoiceDate">Invoice Date:</label><br>
            <input type="date" id="invoiceDate" name="invoiceDate" required>
        </div>

        <div class="form-group">
            <label for="dueDate">Due Date:</label><br>
            <input type="date" id="dueDate" name="dueDate" required>
        </div>
        
          <div class="form-group">
            <label for="discount">Discount:</label><br>
            <input type="number" step="0.01" id="discount" name="discount">
        </div>

        <div class="form-group">
            <label for="totalAmount">Total Amount:</label><br>
            <input type="number" step="0.01" id="totalAmount" name="totalAmount" readonly>
        </div>
        
        <div class="form-group">
            <label for="cash">Cash:</label><br>
            <input type="number" step="0.01" id="cash" name="cash">
        </div>

        <div class="form-group">
            <label for="balance">Balance:</label><br>
            <input type="number" step="0.01" id="balance" name="balance" readonly>
        </div>

        <div class="form-group">
            <label for="status">Status:</label><br>
            <select id="status" name="status" required>
                <option value="Pending">Pending</option>
                <option value="Paid">Paid</option>
                <!-- <option value="Cancelled">Cancelled</option> -->
            </select>
        </div>

        <button type="submit" class="btn">Generate</button>
    </form>
</div>

<%
    String error = (String) request.getAttribute("error");
    String message = (String) request.getAttribute("message");
    if (error != null) request.removeAttribute("error");
    if (message != null) request.removeAttribute("message");
%>

<script>
    <% if (error != null) { %>
    Swal.fire({
        icon: 'error',
        title: '<%= error.replace("\"", "\\\"") %>',
        toast: true,
        position: 'bottom-end',
        showConfirmButton: false,
        timer: 4000
    });
    <% } %>

    <% if (message != null) { %>
    Swal.fire({
        icon: 'success',
        title: '<%= message.replace("\"", "\\\"") %>',
        toast: true,
        position: 'bottom-end',
        showConfirmButton: false,
        timer: 4000
    });
    <% } %>
</script>


<script>
document.addEventListener('DOMContentLoaded', function() {
    const customerInput = document.getElementById('customerName');
    const customerSuggestions = document.getElementById('customerSuggestions');
    const customerIdInput = document.getElementById('customerId');
    
    const itemInput = document.getElementById('item');
    const itemSuggestions = document.getElementById('itemSuggestions');
    const itemIdInput = document.getElementById('itemId');

    // Customer autocomplete
    customerInput.addEventListener('input', function() {
        const query = this.value.trim();
        if (query.length < 2) {
            customerSuggestions.style.display = 'none';
            return;
        }
        
        fetch('CustomerServlet?action=search&query=' + encodeURIComponent(query))
        .then(response => response.json())
        .then(data => {
            customerSuggestions.innerHTML = '';
            
            if (data.length > 0) {
                data.forEach(customer => {
                    const div = document.createElement('div');
                    div.textContent = customer.name; 
                    div.dataset.id = customer.accountNumber; 
                    div.addEventListener('click', function() {
                        customerInput.value = customer.name;
                        customerIdInput.value = customer.accountNumber;
                        customerSuggestions.style.display = 'none';
                    });
                    customerSuggestions.appendChild(div);
                });
                customerSuggestions.style.display = 'block';
            } else {
                customerSuggestions.style.display = 'none';
            }
        });

    });

    // Item autocomplete
     itemInput.addEventListener('input', function() {
        const query = this.value.trim();
        if (query.length < 2) {
            itemSuggestions.style.display = 'none';
            return;
        }
        
        fetch('ItemServlet?action=search&query=' + encodeURIComponent(query))
            .then(response => response.json())
            .then(data => {
                itemSuggestions.innerHTML = '';
                if (data.length > 0) {
                    data.forEach(item => {
                        const div = document.createElement('div');
                        div.textContent = item.itemName;
                        
                        div.dataset.code = item.itemCode;
                        div.dataset.name = item.itemName;
                        div.dataset.desc = item.description;
                        div.dataset.price = item.price;
                        div.dataset.qty = item.quantity;
                        
                        div.addEventListener('click', function() {
                          
                            itemInput.value = item.itemName;
                            itemIdInput.value = item.itemCode;

                            
                            addItemToTable({
                                code: this.dataset.code,
                                name: this.dataset.name,
                                desc: this.dataset.desc,
                                price: this.dataset.price,
                               
                            });
                            itemSuggestions.style.display = 'none';
                        });
                        itemSuggestions.appendChild(div);
                    });
                    itemSuggestions.style.display = 'block';
                } else {
                    itemSuggestions.style.display = 'none';
                }
            });
    }); 
    
    
     function addItemToTable(item) {
    	    const tableBody = document.querySelector('#invoiceTable tbody');

    	    // Avoid duplicate items
    	    if ([...tableBody.querySelectorAll('tr')].some(row => row.dataset.code === item.code)) {
    	        Swal.fire({
    	            icon: 'warning',
    	            title: 'Item already added',
    	            toast: true,
    	            position: 'bottom-end',
    	            showConfirmButton: false,
    	            timer: 2000
    	        });
    	        return;
    	    }

    	    const row = document.createElement('tr');
    	    row.dataset.code = item.code;

    	    row.innerHTML = `
    	        <td>${item.code}</td>
    	        <td>${item.name}</td>
    	        <td>${item.desc}</td>
    	        <td>${parseFloat(item.price).toFixed(2)}</td>
    	        <td><input type="number" min="1" max="${item.qty}" value="1" class="buyQty"></td>
    	        <td class="total">${parseFloat(item.price).toFixed(2)}</td>
    	        <td><button type="button" class="removeBtn">Remove</button></td>
    	    `;

    	   
    	    // Update total when qty changes
    	    row.querySelector('.buyQty').addEventListener('input', function () {
    	        const qty = parseInt(this.value) || 0;
    	        const price = parseFloat(item.price);
    	        row.querySelector('.total').textContent = (qty * price).toFixed(2);
    	    });
    	    
    	    row.querySelector('.removeBtn').addEventListener('click', function () {
    	        row.remove();
    	    });

    	    tableBody.appendChild(row);
    	    updateTotal();
    	}
     
     function updateTotal() {
    	    let total = 0;
    	    const rows = document.querySelectorAll('#invoiceTable tbody tr');
    	    rows.forEach(row => {
    	        const price = parseFloat(row.querySelector('td:nth-child(4)').textContent) || 0;
    	        const qty = parseInt(row.querySelector('.buyQty').value) || 0;
    	        total += price * qty;
    	    });
    	    document.getElementById('totalAmount').value = total.toFixed(2);
    	}

    	// Call updateTotal whenever quantity changes
    	document.querySelector('#invoiceTable tbody').addEventListener('input', function(e) {
    	    if (e.target.matches('.buyQty')) {
    	        updateTotal();
    	    }
    	});



    // Hide suggestions when clicking elsewhere
    document.addEventListener('click', function(e) {
        if (e.target !== customerInput) {
            customerSuggestions.style.display = 'none';
        }
        if (e.target !== itemInput) {
            itemSuggestions.style.display = 'none';
        }
    });
});

const discountInput = document.getElementById('discount');
const totalAmountInput = document.getElementById('totalAmount');

discountInput.addEventListener('input', () => {
    const totalAmount = parseFloat(totalAmountInputt.value) || 0;  
    const discount = parseFloat(discountInput.value) || 0; 

    const newTotalAmount = totalAmount - discount;

    totalAmountInput.value = newTotalAmount.toFixed(2);
});



const cashInput = document.getElementById('cash');
const balanceInput = document.getElementById('balance');

cashInput.addEventListener('input', () => {
    
    const totalAmount = parseFloat(document.getElementById('totalAmount').value) || 0;

    
    const cashValue = parseFloat(cashInput.value) || 0;

   
    const newBalance = cashValue-totalAmount;

    
    balanceInput.value = newBalance.toFixed(2);
});


</script>

</body>
</html>
