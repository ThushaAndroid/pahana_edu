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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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
                            <th>Action</th>
                            
                        </tr>
                    </thead>
                     <%--  <tbody>
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
        </tbody> --%>
          <tbody>
        <%
        List<Item> items = (List<Item>) request.getAttribute("items");;
            if (items != null && !items.isEmpty()) {
                for (Item i : items) {
        %>
        <tr>
            <td><%= i.getItemCode()%></td>
            <td><%= i.getItemName() %></td>
            <td><%= i.getDescription() %></td>
            <td><%= i.getPrice() %></td>
            <td><%= i.getQuantity() %></td>
               <td>
        <form action="ItemServlet" method="get" style="display:inline;">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="itemCode" value="<%= i.getItemCode() %>">
            <button type="submit" class="action-btn edit-btn">Update</button>
        </form>
        <form action="ItemServlet" method="post" style="display:inline;" 
              onsubmit="return confirm('Are you sure you want to delete this item?');">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="itemCode" value="<%= i.getItemCode() %>">
            <button type="submit" class="action-btn delete-btn">Delete</button>
        </form>
    </td>
           
        </tr>
        <% 
                }
            } else { 
        %>
        <tr>
            <td colspan="3">No items found.</td>
        </tr>
        <% } %>
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