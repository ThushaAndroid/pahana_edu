<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="service.CustomerService" %>
<%
  
    CustomerService customerService = new CustomerService();
    String nextAccountNumber = customerService.generateNextAccountNumber();
    System.out.println("newAccountNumber "+nextAccountNumber);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Registration</title>
    <link rel="stylesheet" href="css/customerRegisterStyle.css">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
<div class="top-card">
    <h2 class="top-title">Customer Registration</h2>
    </div>
<div class="register-card">
	 <h3 class="register-title">Register New Customer</h3>
    <form action="CustomerServlet" method="post">
        <input type="hidden" name="action" value="insert" />
        
		<div class="form-group">
        <label for="accountNumber">Account Number:</label><br>
       <!--  <input type="text" id="accountNumber" name="accountNumber" required> -->
         <input type="text" id="accountNumber" name="accountNumber" 
           value="<%= nextAccountNumber %>" readonly>
		</div>
		
		<div class="form-group">
        <label for="nic">NIC Number:</label><br>
        <input type="text" id="nic" name="nic" required>
		</div>
		
		<div class="form-group">
        <label for="name">Name:</label><br>
        <input type="text" id="name" name="name" required>
		</div>
		
		<div class="form-group">
        <label for="address">Address:</label><br>
        <textarea id="address" name="address" rows="3" required></textarea>
        </div>

		<div class="form-group">
        <label for="telephone">Telephone:</label><br>
        <input type="text" id="telephone" name="telephone" required>
		 </div>

			<div class="form-group">
				<label for="email">Email:</label><br> 
				<input type="email" id="email" name="email" required>
			</div>

			<!-- 	 <div class="form-group">
        <label for="unitsConsumed">Units Consumed:</label><br>
        <input type="number" id="unitsConsumed" name="unitsConsumed" min="0" required>
        </div> -->

        <button type="submit">Register Customer</button>
    </form>

   
    <!-- <a href="customer?action=list">Back to Customer List</a> -->
    
 <%
    String customer_error = (String) request.getAttribute("error");
    String customer_message = (String) request.getAttribute("message");
    
    if (customer_error != null) request.removeAttribute("error");
    if (customer_message != null) request.removeAttribute("message");
%> 
  
</div>



<script>
    <% if (customer_error != null) { %>
    
    Swal.fire({
        toast: true,
        position: 'bottom-end', // bottom-right corner
        icon: 'error',
        title: "<%= customer_error.replace("\"", "\\\"") %>",
        showConfirmButton: false,
        timer: 4000, // auto close in 4s
        timerProgressBar: true
    });
    <% } %>

    <% if (customer_message != null) { %>
    Swal.fire({
        toast: true,
        position: 'bottom-end', // bottom-right corner
        icon: 'success',
        title: "<%= customer_message.replace("\"", "\\\"") %>",
        showConfirmButton: false,
        timer: 4000,
        timerProgressBar: true
    });
    <% } %>
</script>

</body>
</html>
