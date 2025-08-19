 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="java.util.List" %>
<%@ page import="model.User" %>

<%
    // Debug code - check what's actually in the request
    Object usersObj = request.getAttribute("users");
    System.out.println("Type of users attribute: " + (usersObj != null ? usersObj.getClass() : "null"));
    
    if (usersObj instanceof List) {
        List<User> userList = (List<User>) usersObj;
        System.out.println("Number of users: " + userList.size());
        for (User user : userList) {
            System.out.println("User: " + user.getUsername() + ", " + user.getStatus());
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>User Report</title>
    <link rel="stylesheet" href="css/reportStyle.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
<div class="top-card">
    <h2 class="top-title">User Report</h2>
</div>

<div class="report-card">
    <!-- <h3 class="register-title">View User Report</h3> -->
    
        <div class="chart-card">
            <h3 class="register-title">View User Report</h3>
            <div class="responsive-table">
                <table class="invoice-table">
                    <thead>
                        <tr>
                            <th>User Name</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <%--   <tbody>
            <c:choose>
                <c:when test="${not empty users}">
                    <c:forEach items="${users}" var="user">
                        <tr>
                            <td>${user.username}</td>
                            <td>${user.role}</td>
                            <td>${user.status}</td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="3">No users found</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody> --%>
         <tbody>
        <%
            List<User> users = (List<User>) request.getAttribute("users");
            if (users != null && !users.isEmpty()) {
                for (User u : users) {
        %>
        <tr>
            <td><%= u.getUsername() %></td>
            <td><%= u.getRole() %></td>
            <td>
    <form action="UserServlet" method="post" style="display:inline;" 
          onsubmit="return confirm('Are you sure you want to <%= "Active".equalsIgnoreCase(u.getStatus()) ? "disable" : "active" %> this user?');">
        <input type="hidden" name="action" value="status">
        <input type="hidden" name="userId" value="<%= u.getUser_id() %>">
        <button type="submit" 
            class="status-btn <%= "Active".equalsIgnoreCase(u.getStatus()) ? "status-active" : "status-inactive" %>">
            <%= u.getStatus() %>
        </button>
    </form>
</td>

            <td>
        <form action="UserServlet" method="get" style="display:inline;">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="userId" value="<%= u.getUser_id() %>">
            <%-- <input type="hidden" name="status" value="<%= u.getStatus() %>"> --%>
            <button type="submit" class="action-btn edit-btn">Update</button>
        </form>
        <form action="UserServlet" method="post" style="display:inline;" 
              onsubmit="return confirm('Are you sure you want to delete this user?');">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="userId" value="<%= u.getUser_id() %>">
            <button type="submit" class="action-btn delete-btn">Delete</button>
        </form>
    </td>
        </tr>
        <% 
                }
            } else { 
        %>
        <tr>
            <td colspan="3">No users found.</td>
        </tr>
        <% } %>
    </tbody>
                </table>
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

 <%-- 
 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Add this inside your reports content div -->
<div id="userReports" class="tab-content">
    <h2>User Reports</h2>
    
    <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
    </c:if>
    
    <c:choose>
        <c:when test="${empty users}">
            <p>No users found.</p>
        </c:when>
        <c:otherwise>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Role</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td>${user.username}</td>
                            <td>${user.role}</td>
                            <td>${user.status}</td>
                            <td>
                                <button class="action-btn edit-btn">Edit</button>
                                <button class="action-btn delete-btn">Delete</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div> --%>