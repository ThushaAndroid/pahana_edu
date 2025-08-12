<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
    String roleName = "admin".equalsIgnoreCase(role) ? "Admin" : "Staff";
    
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= roleName %> Dashboard - Pahana Edu</title>
<link rel="stylesheet" href="css/dashboardStyle.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<script>
    function toggleMenu(menuId) {
        const menu = document.getElementById(menuId);
        menu.classList.toggle("open");
    }

    function refreshPage() {
        location.reload();
    }

    function userProfile() {
        const popup = document.getElementById("profilePopup");
        popup.classList.toggle("open");
    }

    function setMenu() {
        const dropdown = document.getElementById("menuDropdown");
        dropdown.classList.toggle("open");
    }

    // Close popups/menus when clicking outside
    document.addEventListener("click", function(event) {
        const profilePopup = document.getElementById("profilePopup");
        const menuDropdown = document.getElementById("menuDropdown");

        if (!event.target.closest("#profilePopup") && !event.target.closest("[title='Profile']")) {
            profilePopup.classList.remove("open");
        }
        if (!event.target.closest("#menuDropdown") && !event.target.closest("[onclick='setMenu()']")) {
            menuDropdown.classList.remove("open");
        }
    });
</script>

<style>
    /* Better toggle handling with CSS class */
    .open { display: block !important; }
    #profilePopup, #menuDropdown, #transactionMenu, #operationMenu, #reportMenu {
        display: none;
    }
</style>
</head>
<body>

<!-- Top Bar -->
<div class="top-bar">
    <div class="top-bar-left">
        <h3>Pahana Edu</h3>
    </div>
    <div class="top-bar-right">
        <div id="desktopIcon" class="desktop_icon">
            <button class="icon-btn" onclick="refreshPage()" title="Refresh">🔄</button>
            <button class="icon-btn" title="Notifications">🔔</button>
            <button class="icon-btn" title="Settings">⚙️</button>
            <button class="icon-btn" onclick="userProfile()" title="Profile">👤</button>
        </div>
        <button class="icon-btn menu-btn" onclick="setMenu()" title="Menu">☰</button>
    </div>
    
    <!-- Dropdown Menu -->
    <div id="menuDropdown" class="menu-dropdown">
        <button class="icon-btn" onclick="refreshPage()">🔄</button>
        <button class="icon-btn" title="Notifications">🔔</button>
        <button class="icon-btn" title="Settings">⚙️</button>
        <button class="icon-btn" onclick="userProfile()" title="Profile">👤</button>
    </div>
</div>

<!-- Sidebar -->
<div class="sidebar">
    <div class="sidebar-logo">
        <img src="images/sidebar-image.jpg" alt="Pahana Edu" class="sidebar-image">
    </div>

    <div class="user-info">
        <h3>Welcome, <%= username %></h3>
    </div>

    <!-- Transaction Menu -->
    <button onclick="toggleMenu('transactionMenu')">Transaction</button>
    <div id="transactionMenu" style="padding-left:10px;">
        <a href="#" class="button-link">View Bills</a>
        <a href="#" class="button-link">Generate Bill</a>
    </div>

    <% if ("Admin".equals(roleName)) { %>
    <!-- Operation Menu -->
    <button onclick="toggleMenu('operationMenu')">Operation</button>
    <div id="operationMenu" style="padding-left:10px;">
        <a href="userRegister.jsp" class="button-link">Register Users</a>
        <a href="customerRegister.jsp" class="button-link">Register Customers</a>
        <a href="addItem.jsp" class="button-link">Add Items</a>
    </div>
    <% } %>

    <!-- Reports Menu -->
    <button onclick="toggleMenu('reportMenu')">Reports</button>
    <div id="reportMenu" style="padding-left:10px;">
        <form action="UserServlet" method="get">
            <button type="submit" class="btn">User Report</button>
        </form>
        <form action="CustomerServlet" method="get">
            <button type="submit" class="btn">Customer Report</button>
        </form>
        <form action="ItemServlet" method="get">
            <button type="submit" class="btn">Item Report</button>
        </form>
        <form action="InvoiceServlet" method="get">
            <button type="submit" class="btn">Invoice Report</button>
        </form>
    </div>
</div>

<!-- Main Content -->
<div class="main-content">
    <div id="mainMenu" class="tab-content active">
        <%@ include file="mainMenu.jsp" %>
    </div>
</div>

<!-- Profile Popup -->
<div id="profilePopup" class="profile-popup">
    <p><strong><%= username %></strong></p>
    <small>(<%= roleName %>)</small>
    <hr>
    <a href="logout" class="logout-btn">Logout</a>
</div>

</body>
</html>
