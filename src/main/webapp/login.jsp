<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>   
    <%@ page session="true" %>
<%
    String username = (String) session.getAttribute("username");
    if (username != null) {
        // User already logged in, redirect to dashboard
        response.sendRedirect("dashboard.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome login- Pahana Edu</title>
<link rel="stylesheet" type="text/css" href="css/loginStyle.css">
</head>
<body>

<div class="login-container">
    <h2>Login</h2>
    
     <img src="images/login-image.png" alt="Login Illustration" class="login-image">
    
    <form action="LoginServlet" method="post">
        <input type="text" name="username" placeholder="Username">
        <input type="password" name="password" placeholder="Password">
        <button type="submit">Login</button>
    </form>

    <!-- Error message shown BELOW the form -->
    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>
</div>

</body>
</html>