<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="service.ItemService" %>
<%
  
    ItemService itemService = new ItemService();
    String nextItemCode = itemService.generateNextItemCode();
    System.out.println("newItemCode "+nextItemCode);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Add Item</title>
    <link rel="stylesheet" href="css/itemStyle.css">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
<div class="top-card">
<h2 class="top-title">Add Item</h2>
</div>

<div class="register-card">
	 <h3 class="register-title">Register New Item</h3>
<form action="ItemServlet" method="post">
    <input type="hidden" name="action" value="insert" id="actionField" />
<div class="form-group">
    <label for="itemCode">Item Code:</label><br>
    <input type="text" id="itemCode" name="itemCode"
     value="<%= nextItemCode %>" readonly>
</div>
<div class="form-group">
    <label for="itemName">Item Name:</label><br>
    <input type="text" id="itemName" name="itemName" required>
</div>
<div class="form-group">
    <label for="description">Description:</label><br>
    <textarea id="description" name="description" rows="3" required></textarea>
</div>
<div class="form-group">
    <label for="price">Price:</label><br>
    <input type="number" step="0.01" id="price" name="price" required>
</div>
<div class="form-group">
    <label for="quantity">Quantity:</label><br>
    <input type="number" id="quantity" name="quantity" required><br>
</div>
    <button type="submit">Submit</button>
</form>
    
 <%
    String item_error = (String) request.getAttribute("error");
    String item_message = (String) request.getAttribute("message");
    
    if (item_error != null) request.removeAttribute("error");
    if (item_message != null) request.removeAttribute("message");
%> 
  
</div>



<script>
    <% if (item_error != null) { %>
    
    Swal.fire({
        toast: true,
        position: 'bottom-end', // bottom-right corner
        icon: 'error',
        title: "<%= item_error.replace("\"", "\\\"") %>",
        showConfirmButton: false,
        timer: 4000, // auto close in 4s
        timerProgressBar: true
    });
    <% } %>

    <% if (item_message != null) { %>
    Swal.fire({
        toast: true,
        position: 'bottom-end', // bottom-right corner
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