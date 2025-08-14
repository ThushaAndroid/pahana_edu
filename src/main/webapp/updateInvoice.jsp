<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Invoice" %>
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
            <label for="invoiceNo">Invoice No:</label>
            <input type="text" id="invoiceNo" name="invoiceNo" 
                   value="<%= invoice != null?invoice.getInvoiceNo():"" %>" readonly>
        </div>

        <!-- Customer Name -->
        <div class="form-group">
            <label for="customerName">Customer Name:</label>
            <input type="text" id="customerName" name="customerName" 
                   value="<%= invoice != null?invoice.getCustomerName():"" %>" required>
        </div>

        <!-- Invoice Date -->
        <div class="form-group">
            <label for="invoiceDate">Invoice Date:</label>
            <input type="date" id="invoiceDate" name="invoiceDate" 
                   value="<%= invoice != null?(invoice.getInvoiceDate() != null ? sdf.format(invoice.getInvoiceDate()) : ""):""%>" required>
        </div>

        <!-- Due Date -->
        <div class="form-group">
            <label for="dueDate">Due Date:</label>
            <input type="date" id="dueDate" name="dueDate" 
                   value="<%= invoice != null?(invoice.getDueDate() != null ? sdf.format(invoice.getDueDate()) : ""):"" %>">
        </div>

        <!-- Total Amount -->
        <div class="form-group">
            <label for="totalAmount">Total Amount:</label>
            <input type="number" step="0.01" id="totalAmount" name="totalAmount" 
                   value="<%= invoice != null?invoice.getTotalAmount():"" %>" required>
        </div>

        <!-- Balance -->
        <div class="form-group">
            <label for="balance">Balance:</label>
            <input type="number" step="0.01" id="balance" name="balance" 
                   value="<%= invoice != null?invoice.getBalance():"" %>">
        </div>

        <!-- Status -->
        <div class="form-group">
            <label for="status">Status:</label>
            <select id="status" name="status" required>
                <option value="Pending" <%= "Pending".equalsIgnoreCase(invoice != null?invoice.getStatus():"") ? "selected" : "" %>>Pending</option>
                <option value="Paid" <%= "Paid".equalsIgnoreCase(invoice != null?invoice.getStatus():"") ? "selected" : "" %>>Paid</option>
                <option value="Cancelled" <%= "Cancelled".equalsIgnoreCase(invoice != null?invoice.getStatus():"") ? "selected" : "" %>>Cancelled</option>
            </select>
        </div>

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

</body>
</html>
