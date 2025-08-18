<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.BillDetail" %>
<%@ page import="model.Invoice" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    Invoice invoice = (Invoice) request.getAttribute("invoice");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // For HTML date inputs
%>

<!DOCTYPE html>
<html>
<head>
    
    <title>Update Invoice</title>
    <link rel="stylesheet" href="css/invoiceStyle.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<!-- Title -->
<div class="top-card">
    <h2 class="top-title">Update Invoice</h2>
</div>

<!-- Form -->
<div class="register-card">
    <h3 class="register-title">Edit Invoice Details</h3>

    <form action="InvoiceServlet" method="post">
        <input type="hidden" name="action" value="update">

        <!-- Invoice No (read-only) -->
        <div class="form-group">
            <label for="invoiceNo">Invoice No:</label><br>
            <input type="text" id="invoiceNo" name="invoiceNo" 
                   value="<%= invoice != null?invoice.getInvoiceNo():"" %>" readonly>
        </div>

        <!-- Customer Name -->
        <div class="form-group">
            <label for="customerName">Customer Name:</label><br>
            <input type="text" id="customerName" name="customerName" 
                   value="<%= invoice != null?invoice.getCustomerName():"" %>" readonly>
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
            
            
        </tr>
    </thead>
        <tbody>
       <%
    List<BillDetail> bills = (List<BillDetail>) request.getAttribute("bill_details");
    if (bills != null && !bills.isEmpty()) {
        for (BillDetail b : bills) {
%>
<tr>
    <td><%= b.getItemCode() %></td>
    <td><%= b.getItemName() %></td>
    <td><%= b.getDescription() %></td>
    <td><%= b.getPrice() %></td>
    <td><%= b.getQuantity() %></td>
    <td><%= b.getTotal() %></td>
</tr>
<%
        }
    } else {
%>
<tr>
    <td colspan="6" style="text-align:center;">No bill details found</td>
</tr>
<%
    }
%>

                <tbody>
</table>
</div>

        <!-- Invoice Date -->
        <div class="form-group">
            <label for="invoiceDate">Invoice Date:</label><br>
            <input type="date" id="invoiceDate" name="invoiceDate" 
                   value="<%= invoice != null?(invoice.getInvoiceDate() != null ? sdf.format(invoice.getInvoiceDate()) : ""):""%>" readonly>
        </div>

        <!-- Due Date -->
        <div class="form-group">
            <label for="dueDate">Due Date:</label><br>
            <input type="date" id="dueDate" name="dueDate" 
                   value="<%= invoice != null?(invoice.getDueDate() != null ? sdf.format(invoice.getDueDate()) : ""):"" %>">
        </div>
        
         <div class="form-group">
            <label for="discount">Discount:</label><br>
            <input type="number" step="0.01" id="discount" name="discount"
             value="<%= invoice != null?invoice.getDiscount():"" %>" readonly>
        </div>

        <!-- Total Amount -->
        <div class="form-group">
            <label for="totalAmount">Total Amount:</label><br>
            <input type="number" step="0.01" id="totalAmount" name="totalAmount" 
                   value="<%= invoice != null?invoice.getTotalAmount():"" %>" readonly>
        </div>
        
         <div class="form-group">
            <label for="cash">Cash:</label><br>
            <input type="number" step="0.01" id="cash" name="cash">
        </div>

        <!-- Balance -->
        <div class="form-group">
            <label for="balance">Balance:</label><br>
            <input type="number" step="0.01" id="balance" name="balance" 
                   value="<%= invoice != null?invoice.getBalance():"" %>" readonly>
        </div>

        <!-- Status -->
       <%--  <div class="form-group">
            <label for="status">Status:</label>
            <select id="status" name="status" required>
                <option value="Pending" <%= "Pending".equalsIgnoreCase(invoice != null?invoice.getStatus():"") ? "selected" : "" %>>Pending</option>
                <option value="Paid" <%= "Paid".equalsIgnoreCase(invoice != null?invoice.getStatus():"") ? "selected" : "" %>>Paid</option>
                <option value="Cancelled" <%= "Cancelled".equalsIgnoreCase(invoice != null?invoice.getStatus():"") ? "selected" : "" %>>Cancelled</option>
            </select>
        </div> --%>

        <button type="submit">Update Invoice</button>
    </form>

    <%
        String error = (String) request.getAttribute("error");
        String message = (String) request.getAttribute("message");
    %>
</div>

<!-- SweetAlert Messages -->
<script>
    <% if (error != null) { %>
    Swal.fire({
        toast: true,
        position: 'bottom-end',
        icon: 'error',
        title: "<%= error.replace("\"", "\\\"") %>",
        showConfirmButton: false,
        timer: 4000,
        timerProgressBar: true
    });
    <% } %>

    <% if (message != null) { %>
    Swal.fire({
        toast: true,
        position: 'bottom-end',
        icon: 'success',
        title: "<%= message.replace("\"", "\\\"") %>",
        showConfirmButton: false,
        timer: 4000,
        timerProgressBar: true
    });
    <% } %>
</script>


<script>
const cashInput = document.getElementById('cash');
const balanceInput = document.getElementById('balance');

const balanceValue = parseFloat(balanceInput.value) || 0;

cashInput.addEventListener('input', () => {
    
    const cashValue = parseFloat(cashInput.value) || 0;

    const newBalance = balanceValue+cashValue;

    
    balanceInput.value = newBalance.toFixed(2);
}); 


</script>

</body>
</html>
