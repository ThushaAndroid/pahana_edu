<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Item" %>
<%@ page import="model.Customer" %>
<%-- <%@ page import="model.Invoice" %>
<%@ page import="service.InvoiceService" %> --%>
<%
   String nextInvoiceNo = (String) request.getAttribute("invoiceNo");
    List<Item> items = (List<Item>) request.getAttribute("items");
   /*  List<Customer> customers = (List<Customer>) request.getAttribute("customers"); */
   /* InvoiceService invoiceServices = InvoiceService.getInstance();
    String nextInvoiceNo = invoiceServices.generateNextInvoiceNo(); */
    
    String username = request.getParameter("username"); 
    System.out.println("username: " + username);
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
    
    
   <form action="InvoiceServlet" method="post" onsubmit="return validateInvoice()">

        <input type="hidden" name="action" value="insert">
          <input type="hidden" id="unitsField" name="units" value="">
           <input type="hidden" id="nic" name="nic">
            <input type="hidden" name="username" value=<%= username %> />

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
    <input type="hidden" id="accountNumber" name="accountNumber">
    <div id="customerSuggestions" class="autocomplete-suggestions"></div>
</div>



 <div class="form-group">
    <label for="item">Item:</label><br>
    <input type="text" id="item" name="item"
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
            <input type="date" id="dueDate" name="dueDate">
        </div>
        
          <div class="form-group">
            <label for="discount">Discount:</label><br>
            <input type="number" step="0.01" id="discount" name="discount" value="0.0">
        </div>
        
        <div class="form-group">
            <label for="totalQty">Total Quantity:</label><br>
            <input type="number" step="0" id="totalQty" name="totalQty" value="0" readonly>
        </div>

        <div class="form-group">
            <label for="totalAmount">Total Amount:</label><br>
            <input type="number" step="0.01" id="totalAmount" name="totalAmount" value="0.0" readonly>
        </div>
        
        <div class="form-group">
            <label for="cash">Cash:</label><br>
            <input type="number" step="0.01" id="cash" name="cash" value="0.0">
        </div>

        <div class="form-group">
            <label for="balance">Balance:</label><br>
            <input type="number" step="0.01" id="balance" name="balance" value="0.0" readonly>
        </div>

     <!--    <div class="form-group">
            <label for="status">Status:</label><br>
            <select id="status" name="status" required>
                <option value="Pending">Pending</option>
                <option value="Paid">Paid</option>
                <option value="Cancelled">Cancelled</option>
            </select>
        </div> -->

     <button type="submit" class="btn">Generate</button>
    <!--  <button type="button" class="btn" onclick="validateAndSubmit()">Generate</button>  -->
     <!--   <button type="submit" name="action" value="generate" class="btn" onclick="validateAndSubmit()">Generate</button> -->
    </form>
</div>

<% String pdf = (String) request.getAttribute("pdfPath"); %>

<% if (pdf != null) { %>
    <script type="text/javascript">
        alert("Invoice PDF created successfully at: <%= pdf %>");
    </script>
<% } %>


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
function validateInvoice() {
    const tableBody = document.querySelector("#invoiceTable tbody");
    const rows = tableBody.querySelectorAll("tr");

    if (rows.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Please add at least one item before generating the invoice',
            toast: true,
            position: 'bottom-end',
            showConfirmButton: false,
            timer: 3000
        });
        return false; 
    }

    return true; 
}
</script>

<!-- <script>
/* document.querySelector("form").addEventListener("submit", function(event) {
	   // Check if any items are added
	   
	   const rows = document.querySelectorAll('#invoiceTable tbody tr');
	   
    if (rows.length === 0) {
        alert('Please add at least one item before generating the invoice.');
        return;
    }
    
    // Check if total amount is greater than 0
    const totalAmount = parseFloat(document.getElementById('totalAmount').value) || 0;
    if (totalAmount <= 0) {
        alert('Invoice total must be greater than 0.');
        return;
    } 
}); */
function validateAndSubmit() {
    const rows = document.querySelectorAll('#invoiceTable tbody tr');
    
    // Check if any items are added
    if (rows.length === 0) {
        alert('Please add at least one item before generating the invoice.');
        return;
    }
    
    // Check if total amount is greater than 0
 /*    const totalAmount = parseFloat(document.getElementById('totalAmount').value) || 0;
    if (totalAmount <= 0) {
        alert('Invoice total must be greater than 0.');
        return;
    } */
    
    // Optional: Check for required fields
  /*   const cash = parseFloat(document.getElementById('cash').value) || 0;
    if (cash < totalAmount) {
        const confirmSubmit = confirm('Cash amount is less than total. Do you want to continue?');
        if (!confirmSubmit) return;
    } */
    
    // All validations passed, submit the form
   //document.querySelector('form').submit();
    }
</script> -->

<script>
document.addEventListener('DOMContentLoaded', function() {
    const customerInput = document.getElementById('customerName');
    const customerSuggestions = document.getElementById('customerSuggestions');
    const customerIdInput = document.getElementById('accountNumber');
    const customerNicInput = document.getElementById('nic');
    
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
                   /*  div.textContent = customer.name;  */
                        // If query looks numeric → show NIC, else show name
                if (/^\d+$/.test(query)) {
                    div.textContent = customer.nic;
                } else {
                    div.textContent = customer.name;
                }

                    div.dataset.id = customer.accountNumber; 
                    div.addEventListener('click', function() {
                        customerInput.value = customer.name;
                        customerIdInput.value = customer.accountNumber;
                        customerNicInput.value = customer.nic;
                        customerSuggestions.style.display = 'none';
                        
                        getCustomerUnits(customer.accountNumber);
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
                        /* div.textContent = item.itemName; */
                           if (/^\d+$/.test(query)) {
                        // Numeric → likely item code
                        div.textContent = item.itemCode;
                    } else {
                        // Text → likely item name
                        div.textContent = item.itemName;
                    }
                        
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
                                qty: this.dataset.qty  
                               
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
    	    
    	    if (parseInt(item.qty) === 0) {
    	        Swal.fire({
    	            icon: 'warning',
    	            title: 'Item quantity is empty',
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
    	        <td>${item.code} <input type="hidden" name="item_code[]" value="${item.code}"></td>
    	        <td>${item.name} <input type="hidden" name="item_name[]" value="${item.name}"></td>
    	        <td>${item.desc} <input type="hidden" name="description[]" value="${item.desc}"></td>
    	        <td>${parseFloat(item.price).toFixed(2)}  <input type="hidden" name="price[]" value="${item.price}"></td>
    	        <td><input type="number" min="1" max="${item.qty}" value="1" class="buyQty" name="quantity[]"></td>
    	        <td class="total">${parseFloat(item.price).toFixed(2)}</td>
    	        <td><button type="button" class="removeBtn">Remove</button></td>
    	    `;

    	   
    	    // Update total when qty changes
    	    row.querySelector('.buyQty').addEventListener('input', function () {
    	        const qty = parseInt(this.value) || 0;
    	        const price = parseFloat(item.price);
    	        row.querySelector('.total').textContent = (qty * price).toFixed(2);
    	    });
    	    
    	  /*   row.querySelector('.removeBtn').addEventListener('click', function () {
    	        row.remove();
    	    }); */
    	    
    	    row.querySelector('.removeBtn').addEventListener('click', function () {
    	        // Get the row total before removing (use 'total' not 'itemTotal')
    	        const rowTotal = parseFloat(row.querySelector('.total').textContent) || 0;
    	        const rowTotalQty = parseInt(row.querySelector('.buyQty').value) || 0;
    	        
    	        const discount = parseFloat(discountInput.value) || 0;

    	        // Update the invoice total
    	        const totalAmountInput = document.getElementById("totalAmount");
    	        let currentTotal = parseFloat(totalAmountInput.value) || 0;
    	        currentTotal -= rowTotal;
    	        
    	        //currentTotal -=discount;
    	        // Set updated total
    	        totalAmountInput.value = currentTotal.toFixed(2);
    	        
    	        const totalQty = document.getElementById("totalQty");
    	        let currentTotalQty = parseInt(totalQty.value) || 0;
    	        currentTotalQty -= rowTotalQty;
    	        
    	        totalQty.value = currentTotalQty;
    	        
    	        const cash = parseFloat(document.getElementById('cash').value) || 0;
    	        
    	        
        	    // Update balance
        	    const balance = cash-currentTotal;
        	    document.getElementById('balance').value = balance.toFixed(2);
        	    originalAmount = currentTotal;
        	    
        	   /*  document.getElementById('discount').value = 0.0 */

    	        // Remove row
    	        if(currentTotalQty===0){
    	        	document.getElementById('totalAmount').value = 0.0;
    	        	document.getElementById('balance').value = 0.0;
    	        }
    	        
    	        row.remove();
    	    });

    	    tableBody.appendChild(row);
    	    updateTotal();
    	    
    	    document.getElementById("item").value = "";
    	    document.getElementById("itemId").value = "";
    	    document.getElementById("itemSuggestions").innerHTML = "";
    	}
     
     function updateTotal() {
    	    let totalQty = 0;
    	    let total = 0;
    	    const rows = document.querySelectorAll('#invoiceTable tbody tr');
    	    rows.forEach(row => {
    	        const price = parseFloat(row.querySelector('td:nth-child(4)').textContent) || 0;
    	        const qty = parseInt(row.querySelector('.buyQty').value) || 0;
    	        total += price * qty;
    	        
    	        totalQty+=qty;
    	    });
    	    
    	    const discount = parseFloat(document.getElementById('discount').value) || 0;
    	    total-=discount;
    	    
    	    document.getElementById('totalAmount').value = total.toFixed(2);
    	    document.getElementById('totalQty').value = totalQty;
    	    
    	    const cash = parseFloat(document.getElementById('cash').value) || 0;
    	    
    	  
    	    
    	    // Update balance
    	    const balance = cash-total;
    	    document.getElementById('balance').value = balance.toFixed(2);
    	    originalAmount = total;
    	   /*  document.getElementById('discount').value = 0.0;
    	    document.getElementById('cash').value = 0.0; */
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


/* const discountInput = document.getElementById('discount');
const totalAmountInput = document.getElementById('totalAmount');

discountInput.addEventListener('input', () => {
    const totalAmount = parseFloat(totalAmountInput.value) || 0;  
    const discount = parseFloat(discountInput.value) || 0; 

    const newTotalAmount = totalAmount - discount;

    totalAmountInput.value = newTotalAmount.toFixed(2);
}); */
const discountInput = document.getElementById('discount');
const totalAmountInput = document.getElementById('totalAmount');


let originalAmount = 0; // Use 'let' instead of 'const' so it can be updated
//let originalBalance = 0;

// Calculate discounted amount when discount is entered
discountInput.addEventListener('input', function() {
    const discount = parseFloat(discountInput.value) || 0;
    
    const newTotalAmount = originalAmount - discount;

    const finalAmount = Math.max(0, newTotalAmount);
    
    totalAmountInput.value = finalAmount.toFixed(2);
    
    balance();
});

function balance() {
    const balanceDisInput = document.getElementById('balance');
    const cash = parseFloat(document.getElementById('cash').value) || 0;
    const total = parseFloat(document.getElementById('totalAmount').value) || 0;
    const discount = parseFloat(document.getElementById('discount').value) || 0;

    // final amount after discount
   /*  const finalTotal = Math.max(0, total - discount); */

    // balance = cash - final total
    const newBalance = cash - total;

    balanceDisInput.value = newBalance.toFixed(2);
}

const cashInput = document.getElementById('cash');
const balanceInput = document.getElementById('balance');

cashInput.addEventListener('input', () => {
    
    const totalAmount = parseFloat(document.getElementById('totalAmount').value) || 0;

    
    const cashValue = parseFloat(cashInput.value) || 0;

   
    const newBalance = cashValue-totalAmount;

    
    balanceInput.value = newBalance.toFixed(2);
});


/* function getCustomerUnits(var accountNo) {
	Integer units = service.getUnitsByAccountNumber(accountNumber);
} */

function getCustomerUnits(accountNumber) {
    if (!accountNumber) {
        console.error("Account number is missing!");
        return;
    }

    fetch('CustomerServlet?action=getUnits&accountNumber=' + encodeURIComponent(accountNumber))
        .then(response => response.json())
        .then(data => {
            if (data.units_consumed !== undefined) {
                console.log("Units Consumed: " + data.units_consumed);

                // Example: update a hidden input or display field
                document.getElementById("unitsField").value = data.units_consumed;
            } else {
                console.error("Error:", data.error);
                alert("Customer not found!");
            }
        })
        .catch(err => console.error("Fetch error:", err));
}



</script>




</body>
</html>
