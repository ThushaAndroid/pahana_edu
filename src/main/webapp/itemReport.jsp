<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="java.util.List" %>
<%@ page import="model.Item" %>

<%
    // Debug code - check what's actually in the request
    Object usersObj = request.getAttribute("items");
    System.out.println("Type of items attribute: " + (usersObj != null ? usersObj.getClass() : "null"));
    
    if (usersObj instanceof List) {
        List<Item> itemList = (List<Item>) usersObj;
        System.out.println("Number of items: " + itemList.size());
        for (Item item : itemList) {
            System.out.println("User: " + item.getItemCode() + ", " + item.getItemName());
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Item Report</title>
    <link rel="stylesheet" href="css/reportStyle.css">
</head>
<body>
<div class="top-card">
    <h2 class="top-title">Item Report</h2>
</div>

<div class="report-card">
    <!-- <h3 class="register-title">View User Report</h3> -->
    
        <div class="chart-card">
            <h3 class="register-title">View Item Report</h3>
            <div class="responsive-table">
                <table class="invoice-table">
                    <thead>
                        <tr>
                            <th>Item Code</th>
                            <th>Item Name</th>
                            <th>Description</th>
                            <th>price</th>
                            <th>Quantity</th>
                            
                        </tr>
                    </thead>
                      <tbody>
            <c:choose>
                <c:when test="${not empty items}">
                    <c:forEach items="${items}" var="item">
                        <tr>
                            <td>${item.itemCode}</td>
                            <td>${item.itemName}</td>
                            <td>${item.description}</td>
                            <td>${item.price}</td>
                            <td>${item.quantity}</td>
        
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="5">No users found</td>
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