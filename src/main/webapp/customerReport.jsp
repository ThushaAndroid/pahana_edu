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
    
    String roleName = (String)request.getAttribute("roleName");
    System.out.println("roleName: " + roleName);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Customer Report</title>
    <link rel="stylesheet" href="css/reportStyle.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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
                            <th>Action</th>
                            
                        </tr>
                    </thead>
                     <%--  <tbody>
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
        </tbody> --%>
          <tbody>
        <%
        List<Customer> customers = (List<Customer>) request.getAttribute("customers");;
            if (customers != null && !customers.isEmpty()) {
                for (Customer c : customers) {
        %>
        <tr>
            <td><%= c.getAccountNumber()%></td>
            <td><%= c.getNic() %></td>
            <td><%= c.getName() %></td>
            <td><%= c.getAddress() %></td>
            <td><%= c.getTelephone() %></td>
            <td><%= c.getEmail() %></td>
            <td><%= c.getUnitsConsumed() %></td>
            
            
            
             <td>
             <form action="CustomerServlet" method="get" style="display:inline;">
            <input type="hidden" name="action" value="view">
            <input type="hidden" name="accountNumber" value="<%= c.getAccountNumber() %>">
            <%-- <input type="hidden" name="status" value="<%= u.getStatus() %>"> --%>
            <button type="submit" class="action-btn view-btn">View</button>
        </form>
        
             <% if ("Admin".equals(roleName)) { %>
            
        <form action="CustomerServlet" method="get" style="display:inline;">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="accountNumber" value="<%= c.getAccountNumber() %>">
            <button type="submit" class="action-btn edit-btn">Update</button>
        </form>
        <form action="CustomerServlet" method="post" style="display:inline;" 
              onsubmit="return confirm('Are you sure you want to delete this customer?');">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="accountNumber" value="<%= c.getAccountNumber() %>">
            <button type="submit" class="action-btn delete-btn">Delete</button>
        </form>
         <% } %>
    </td>
    
        </tr>
        <% 
                }
            } else { 
        %>
        <tr>
            <td colspan="3">No customers found.</td>
        </tr>
        <% } %>
    </tbody>
                </table>
            </div>
            
<div class="export-buttons">
    <form action="CustomerServlet" method="get">
        <input type="hidden" name="action" value="excel">
        <button type="submit" class="excel-btn">Export to Excel</button>
    </form>

    <form action="CustomerServlet" method="get">
        <input type="hidden" name="action" value="pdf">
        <button type="submit" class="pdf-btn">Export to PDF</button>
    </form>
</div>
        </div>
      <%
    String error = (String) request.getAttribute("error");
    String message = (String) request.getAttribute("message");
%>
</div>

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