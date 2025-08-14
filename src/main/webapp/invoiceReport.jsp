<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="java.util.List" %>
<%@ page import="model.Invoice" %>

<%
    // Debug code - check what's actually in the request
    Object usersObj = request.getAttribute("invoices");
    System.out.println("Type of invoices attribute: " + (usersObj != null ? usersObj.getClass() : "null"));
    
    if (usersObj instanceof List) {
        List<Invoice> invoiceList = (List<Invoice>) usersObj;
        System.out.println("Number of invoices: " + invoiceList.size());
        for (Invoice invoice : invoiceList) {
            System.out.println("Invoices: " + invoice.getInvoiceNo() + ", " + invoice.getTotalAmount());
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Invoice Report</title>
    <link rel="stylesheet" href="css/reportStyle.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
<div class="top-card">
    <h2 class="top-title">Invoice Report</h2>
</div>

<div class="report-card">
    <!-- <h3 class="register-title">View User Report</h3> -->
    
        <div class="chart-card">
            <h3 class="register-title">View Invoice Report</h3>
            <div class="responsive-table">
                <table class="invoice-table">
                    <thead>
                        <tr>
							<th>Invoice No</th>
							<th>Customer Name</th>
							<th>Invoice Date</th>
							<th>Due Date</th>
							<th>Total Amount</th>
							<th>Balance</th>
							<th>Status</th>
							<th>Action</th>
						</tr>
                    </thead>
                     <%--  <tbody>
            <c:choose>
                <c:when test="${not empty invoices}">
                    <c:forEach items="${invoices}" var="invoice">
                        <tr>
                            <td>${invoice.username}</td>
                            <td>${invoice.role}</td>
                            <td>${invoice.status}</td>
                            <td>${invoice.status}</td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="4">No users found</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody> --%>
        
        <tbody>
<%
    List<Invoice> invoices = (List<Invoice>) request.getAttribute("invoices");
    if (invoices != null && !invoices.isEmpty()) {
        for (Invoice i : invoices) {
%>
<tr>
    <td><%= i.getInvoiceNo() %></td>
    <td><%= i.getCustomerName() %></td>
    <td><%= i.getInvoiceDate() %></td>
    <td><%= i.getDueDate() %></td>
    <td><%= i.getTotalAmount() %></td>
    <td><%= i.getBalance() %></td>
    <td><%= i.getStatus() %></td>
    <td>
        <form action="InvoiceServlet" method="get" style="display:inline;">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="invoiceNo" value="<%= i.getInvoiceNo() %>">
            <button type="submit" class="action-btn edit-btn">Update</button>
        </form>
        <form action="InvoiceServlet" method="post" style="display:inline;" 
              onsubmit="return confirm('Are you sure you want to delete this invoice?');">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="invoiceNo" value="<%= i.getInvoiceNo() %>">
            <button type="submit" class="action-btn delete-btn">Delete</button>
        </form>
    </td>
</tr>
<%
        }
    } else {
%>
<tr>
    <td colspan="8">No invoices found.</td>
</tr>
<%
    }
%>
</tbody>

                </table>
            </div>
        </div>
    <%
    String item_error = (String) request.getAttribute("error");
    String item_message = (String) request.getAttribute("message");
%>
</div>

<script>
    <% if (item_error != null) { %>
    Swal.fire({
        toast: true,
        position: 'bottom-end',
        icon: 'error',
        title: "<%= item_error.replace("\"", "\\\"") %>",
        showConfirmButton: false,
        timer: 4000,
        timerProgressBar: true
    });
    <% } %>

    <% if (item_message != null) { %>
    Swal.fire({
        toast: true,
        position: 'bottom-end',
        icon: 'success',
        title: "<%= item_message.replace("\"", "\\\"") %>",
        showConfirmButton: false,
        timer: 4000,
        timerProgressBar: true
    });
    <% } %>
</script>

</body>
</html>