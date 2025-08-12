<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<%@ page import="java.util.List" %>
<%@ page import="model.Customer" %>
    
    <%
    // Debug code - check what's actually in the request
    Object usersObj = request.getAttribute("customers");
    System.out.println("Type of customer attribute: " + (usersObj != null ? usersObj.getClass() : "null"));
    
    if (usersObj instanceof List) {
        List<Customer> customerList = (List<Customer>) usersObj;
        System.out.println("Number of customers: " + customerList.size());
        for (Customer customer : customerList) {
            System.out.println("Customer: " + customer.getAccountNumber() + ", " + customer.getName());
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Customer Report</title>
    <link rel="stylesheet" href="css/reportStyle.css">
</head>
<body>
<div class="top-card">
    <h2 class="top-title">Customer Report</h2>
</div>

<div class="report-card">
    <!-- <h3 class="register-title">View User Report</h3> -->
    
        <div class="chart-card">
            <h3 class="register-title">View Customer Report</h3>
            <div class="responsive-table">
                <table class="invoice-table">
                    <thead>
                        <tr>
                            <th>Account Number</th>
                            <th>Nic</th>
                            <th>Name</th>
                            <th>Address</th>
                            <th>Telephone</th>
                            <th>Email</th>
                            <th>Units Consumed</th>
                            
                        </tr>
                    </thead>
                      <tbody>
            <c:choose>
                <c:when test="${not empty customers}">
                    <c:forEach items="${customers}" var="customer">
                        <tr>
                            <td>${customer.accountNumber}</td>
                            <td>${customer.nic}</td>
                            <td>${customer.name}</td>
                            <td>${customer.address}</td>
                            <td>${customer.telephone}</td>
                            <td>${customer.email}</td>
                            <td>${customer.unitsConsumed}</td>
                  
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7">No users found</td>
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