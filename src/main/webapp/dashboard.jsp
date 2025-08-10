
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
    String roleName = "admin".equalsIgnoreCase(role) ? "Admin" : "Staff";
    
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies

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
        var menu = document.getElementById(menuId);
        menu.style.display = (menu.style.display === "block") ? "none" : "block";
    }

    function openContent(contentId) {
        var contents = document.getElementsByClassName("tab-content");
        for (var i = 0; i < contents.length; i++) {
            contents[i].style.display = "none";
        }
        document.getElementById(contentId).style.display = "block";
        
        var i, tabcontent;
        tabcontent = document.getElementsByClassName("tab-content");
        for (i = 0; i < tabcontent.length; i++) {
            tabcontent[i].classList.remove("active");
        }
       
        document.getElementById(contentId).classList.add("active");
        evt.currentTarget.classList.add("active");
    }

    window.onload = function() {
        // Show first section by default
        openContent('mainMenu');
        
        history.pushState(null, null, location.href);
        window.onpopstate = function () {
            history.go(1);
            
        }
    };
</script>
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


       <%--  <span class="top-bar-user">
            <%= username %> (<%= roleName %>)
        </span> --%>
       <%--   <div class="user-role-logout">
        <small>(<%= roleName %>)</small>
        <a href="logout" class="logout-link" >Logout</a>
    </div> --%>
    </div>
    
    <!-- Dropdown Menu (hidden by default) -->
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
       <!--  <img src="images/sliderbar-image.png" alt="Pahana Edu" class="sliderbar-image"> -->
    </div>

    <%-- <h3>Welcome, <%= username %></h3>
    <small>(<%= roleName %>)</small> --%>
<div class="user-info">
    <h3>Welcome, <%= username %></h3>
   <%--  <div class="user-role-logout">
        <small>(<%= roleName %>)</small>
        <a href="logout" class="logout-link" >Logout</a>
    </div> --%>
</div>


    <!-- Transaction Menu -->
    <button onclick="toggleMenu('transactionMenu')">Transaction</button>
    <div id="transactionMenu" style="display:none; padding-left:10px;">
      <a href="javascript:void(0)" class="button-link" onclick="openContent('viewBill')">View Bills</a>
	  <a href="javascript:void(0)" class="button-link" onclick="openContent('generateBill')">Generate Bill</a>

    </div>

    <% if ("Admin".equals(roleName)) { %>
    <!-- Operation Menu -->
    <button onclick="toggleMenu('operationMenu')">Operation</button>
    <div id="operationMenu" style="display:none; padding-left:10px;">
        <span class="sidebar-section-title">Users</span><br>
       
        <a href="javascript:void(0)" class="button-link" onclick="openContent('registerUser')">Register Users</a>
        <a href="javascript:void(0)" class="button-link" onclick="openContent('editUser')">Edit Users</a>
        <a href="javascript:void(0)" class="button-link" onclick="openContent('deleteUser')">Delete Users</a>
        <a href="javascript:void(0)" class="button-link" onclick="openContent('accessLevel')">Access Level</a>
 		<!-- <div style="margin-bottom:10px;"></div> spacing -->
        <span class="sidebar-section-title">Customers</span><br>
        
        <a href="javascript:void(0)" class="button-link" onclick="openContent('registerCustomer')">Register Customers</a>
        <a href="javascript:void(0)" class="button-link" onclick="openContent('editCustomer')">Edit Customers</a>
        <a href="javascript:void(0)" class="button-link" onclick="openContent('deleteCustomer')">Delete Customers</a>
		<!--  <div style="margin-bottom:10px;"></div> spacing -->
        <span class="sidebar-section-title">Items</span><br>
        
        <a href="javascript:void(0)" class="button-link" onclick="openContent('addItem')">Add Items</a>
        <a href="javascript:void(0)" class="button-link" onclick="openContent('editItem')">Edit Items</a>
        <a href="javascript:void(0)" class="button-link" onclick="openContent('deleteItem')">Delete Items</a>
       <!--   <div style="margin-bottom:10px;"></div> spacing -->
    </div>
    <% }else { %>
    <!-- Operation Menu for Regular User -->
    <button onclick="toggleMenu('operationMenu')">Operation</button>
    <div id="operationMenu" style="display:none; padding-left:10px;">
        <a href="javascript:void(0)" class="button-link" onclick="openContent('viewItems')">View Items</a>
        <a href="javascript:void(0)" class="button-link" onclick="openContent('placeOrder')">Place Order</a>
        <a href="javascript:void(0)" class="button-link" onclick="openContent('viewMyOrders')">View My Orders</a>
    </div>
<% } %>

    <!-- Reports Menu -->
    <button onclick="toggleMenu('reportMenu')">Reports</button>
    <div id="reportMenu" style="display:none; padding-left:10px;">
        <a href="javascript:void(0)" class="button-link" onclick="openContent('userReports')">User Reports</a>
        <a href="javascript:void(0)" class="button-link" onclick="openContent('customerReports')">Customer Reports</a>
        <a href="javascript:void(0)" class="button-link" onclick="openContent('itemReports')">Item Reports</a>
        <a href="javascript:void(0)" class="button-link" onclick="openContent('invoiceReports')">Invoice Reports</a>
       
    </div>
    
    <div class="space">
    </div>

   <!--  <p class="logout-link"><a href="logout" style="color:white;">Logout</a></p> -->
</div>

<!-- Main Content -->
<div class="main-content">

<%--  <%


    String mainContent = (String) request.getAttribute("mainContent");
%>

<% if (mainContent != null && !mainContent.isEmpty()) { %>
    <jsp:include page="<%= mainContent %>" />
  
<% }
else { %>

    <div id="mainMenu" class="tab-content active">
        <%@ include file="mainMenu.jsp" %>
    </div>
<% } 
%> --%>
	 <div id="mainMenu" class="tab-content active">
    <%@ include file="mainMenu.jsp" %>
  <!--   <a href="mainMenu.jsp"></a> -->
</div>  

    <!-- Transaction Content -->
    <div id="viewBill" class="tab-content">
        <h2>View Bills</h2>
        <p>Content for viewing bills goes here.</p>
    </div>
    <div id="generateBill" class="tab-content">
        <h2>Generate Bill</h2>
        <p>Content for generating bills goes here.</p>
    </div>

    <% if ("Admin".equals(roleName)) { %>
    <!-- Operation Content -->
    <div id="registerUser" class="tab-content">
     <%@ include file="userRegister.jsp" %>
     </div>
     
    <div id="editUser" class="tab-content"><h2>Edit Users</h2></div>
    <div id="deleteUser" class="tab-content"><h2>Delete Users</h2></div>
    <div id="accessLevel" class="tab-content"><h2>Access Level</h2></div>

    <div id="registerCustomer" class="tab-content">
     <%@ include file="customerRegister.jsp" %>
    </div>
    <div id="editCustomer" class="tab-content"><h2>Edit Customers</h2></div>
    <div id="deleteCustomer" class="tab-content"><h2>Delete Customers</h2></div>

    <div id="registerItem" class="tab-content"><h2>Add Items</h2></div>
    <div id="editItem" class="tab-content"><h2>Edit Items</h2></div>
    <div id="deleteItem" class="tab-content"><h2>Delete Items</h2></div>
    <% } %>

    <!-- Reports Content -->
    <div id="userReports" class="tab-content"><h2>User Reports</h2></div>
    <div id="customerReports" class="tab-content"><h2>Customer Reports</h2></div>
    <div id="itemReports" class="tab-content"><h2>Item Reports</h2></div>
    <div id="invoiceReports" class="tab-content"><h2>Invoice Reports</h2></div>
    
    
</div>


<!-- Profile Popup -->
<div id="profilePopup" class="profile-popup">
    <p><strong><%= username %></strong></p>
    <small>(<%= roleName %>)</small>
    <hr>
    <a href="logout" class="logout-btn">Logout</a>
</div>

<script>
function userProfile() {
    var popup = document.getElementById("profilePopup");
    popup.style.display = (popup.style.display === "block") ? "none" : "block";
}

// Close popup when clicking outside
window.addEventListener("click", function(event) {
    var popup = document.getElementById("profilePopup");
    if (!popup.contains(event.target) && event.target.title !== "Profile") {
        popup.style.display = "none";
    }
});



function setMenu() {
    const dropdown = document.getElementById("menuDropdown");
    dropdown.style.display = (dropdown.style.display === "block") ? "none" : "block";
}

// Optional: Close menu if clicking outside
document.addEventListener("click", function(event) {
    const dropdown = document.getElementById("menuDropdown");
    const menuBtn = event.target.closest(".icon-btn[onclick='setMenu()']");
    if (!dropdown.contains(event.target) && !menuBtn) {
        dropdown.style.display = "none";
    }
}); 


        
</script>

</body>
</html>
 