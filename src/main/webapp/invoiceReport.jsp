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
            System.out.println("Invoices: " + invoice.getInvoiceNo() + ", " + invoice.getAmount());
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Invoice Report</title>
    <link rel="stylesheet" href="css/reportStyle.css">
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
                            <th>Invoive no</th>
                            <th>Date</th>
                            <th>Amount</th>
                            <th>Balance</th>
                        </tr>
                    </thead>
                      <tbody>
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
        </tbody>
                </table>
            </div>
        </div>
    </div>

</body>
</html>