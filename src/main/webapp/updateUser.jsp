<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%
    User user = (User) request.getAttribute("user");
%>
    
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit User</title>
    <link rel="stylesheet" href="css/userRegisterStyle.css">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<!-- Title Card -->
<div class="top-card">
    <h2 class="top-title">Edit User</h2>
</div>

<!-- Form Card -->
<div class="register-card">
    <h3 class="register-title">Edit User Account Details</h3>
    <form action="UserServlet" method="post">
    <input type="hidden" name="action" value="update">
    
       <div class="form-group">
    <label for="itemCode">User id:</label>
    <input type="text" id="userId" name="userId"
     value="<%=user!= null? user.getUser_id(): "" %>" readonly>
</div>
        <div class="form-group">
            <label>Username:</label>
            <input type="text" name="username" value="<%=user!= null ? user.getUsername(): "" %>" required>
        </div>

        <div class="form-group">
            <label>Password:</label>
             <input type="password" name="password" value="<%=user!= null ? user.getPassword(): "" %>" required>
        </div>

        <div class="form-group">
            <label>Role:</label>
          <select name="role">
    <option value="staff" <%= "staff".equals(user!= null ?user.getRole(): "") ? "selected" : "" %>>Staff</option>
    <option value="admin" <%= "admin".equals(user!= null ?user.getRole(): "") ? "selected" : "" %>>Admin</option>
</select>

        </div>

        <button type="submit">Update</button>
    </form>

<%
    String error = (String) request.getAttribute("error");
    String message = (String) request.getAttribute("message");
%>
<%--     <div class="message success">${message}</div>
    <div class="message error">${errorMessage}</div> --%>
</div>


<script>
    <% if (error != null) { %>
    Swal.fire({
        toast: true,
        position: 'bottom-end', // bottom-right corner
        icon: 'error',
        title: "<%= error.replace("\"", "\\\"") %>",
        showConfirmButton: false,
        timer: 4000, // auto close in 4s
        timerProgressBar: true
    });
    <% } %>

    <% if (message != null) { %>
    Swal.fire({
        toast: true,
        position: 'bottom-end', // bottom-right corner
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
