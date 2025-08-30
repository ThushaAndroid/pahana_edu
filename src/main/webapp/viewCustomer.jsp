<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="model.Customer" %>
    
    <%
    Customer customer = (Customer) request.getAttribute("customer");
    %>
    
<!DOCTYPE html>
<html>
<head>
    <title>View Customer</title>
    <link rel="stylesheet" href="css/customerRegisterStyle.css">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
<div class="top-card">
    <h2 class="top-title">View Customer</h2>
    </div>
<div class="register-card">
	 <h3 class="register-title">View Customer Details</h3>
    <form action="CustomerServlet" method="post">
       <!--  <input type="hidden" name="action" value="update" /> -->

        <div class="form-group">
            <label for="accountNumber">Account Number:</label><br>
            <input type="text" id="accountNumber" name="accountNumber" 
                   value="<%= customer != null ? customer.getAccountNumber() : "" %>" readonly>
        </div>

        <div class="form-group">
            <label for="nic">NIC Number:</label><br>
            <input type="text" id="nic" name="nic" 
                   value="<%= customer != null ? customer.getNic() : "" %>" readonly>
        </div>

        <div class="form-group">
            <label for="name">Name:</label><br>
            <input type="text" id="name" name="name" 
                   value="<%= customer != null ? customer.getName() : "" %>" readonly>
        </div>

        <div class="form-group">
            <label for="address">Address:</label><br>
            <textarea id="address" name="address" rows="3" readonly><%= customer != null ? customer.getAddress() : "" %></textarea>
        </div>

        <div class="form-group">
            <label for="telephone">Telephone:</label><br>
            <input type="text" id="telephone" name="telephone" 
                   value="<%= customer != null ? customer.getTelephone() : "" %>" readonly>
        </div>

        <div class="form-group">
            <label for="email">Email:</label><br>
            <input type="email" id="email" name="email" 
                   value="<%= customer != null ? customer.getEmail() : "" %>" readonly>
        </div>
        
          <div class="form-group">
            <label for="unit">units Consumed:</label><br>
            <input type="number" id="unit" name="unit" 
                   value="<%= customer != null ? customer.getUnitsConsumed() : "" %>" readonly>
        </div>

      <!--   <button type="submit">Update Customer</button> -->
    </form>

   
    <!-- <a href="customer?action=list">Back to Customer List</a> -->
    

  
</div>





</body>
</html>