<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Item" %>
<%
    Item item = (Item) request.getAttribute("item");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Update Item</title>
    <link rel="stylesheet" href="css/itemStyle.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
<div class="top-card">
    <h2 class="top-title">Update Item</h2>
</div>

<div class="register-card">
    <h3 class="register-title">Edit Item Details</h3>


<form action="ItemServlet" method="post">
    <input type="hidden" name="action" value="update" />

    <div class="form-group">
        <label for="itemCode">Item Code:</label><br>
        <input type="text" id="itemCode" name="itemCode"
               value="<%=item != null? item.getItemCode():"" %>" readonly>
    </div>

    <div class="form-group">
        <label for="itemName">Item Name:</label><br>
        <input type="text" id="itemName" name="itemName" 
               value="<%=item != null? item.getItemName():"" %>" required>
    </div>

    <div class="form-group">
        <label for="description">Description:</label><br>
        <textarea id="description" name="description" rows="3" required><%=item != null? item.getDescription():"" %></textarea>
    </div>

    <div class="form-group">
        <label for="price">Price:</label><br>
        <input type="number" step="0.01" id="price" name="price" 
               value="<%=item != null? item.getPrice():"" %>" required>
    </div>

    <div class="form-group">
        <label for="quantity">Quantity:</label><br>
        <input type="number" id="quantity" name="quantity" 
               value="<%=item != null? item.getQuantity():"" %>" required>
    </div>

    <button type="submit">Update</button>
</form>


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
