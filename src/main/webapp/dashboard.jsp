<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Item" %>
<%@ page import="service.ItemService" %>
<%@ page import="model.Invoice" %>
<%@ page import="service.InvoiceService" %>
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
   /*  ItemService itemService = ItemService.getInstance();;
    List<Item>items=itemService.getItemsNameNQty();
    if(items!=null){
    	for(Item item:items){
    		System.out.println("Item name "+item.getItemName() + " Item qty "+item.getQuantity());
    	}
    } */
    
    int notificationCount = 0;
    
    ItemService itemService = ItemService.getInstance();
    List<Item> items = itemService.getItemsNameNQty();
   /*  int notificationCount = 0; */
   
   if(roleName=="Admin"){
    if (items != null) {
        for (Item item : items) {
            if (item.getQuantity() == 0) {   // or use <= 5 for low stock warning
                notificationCount++;
            }
        }
    }
   }
    
    InvoiceService invoiceServices = InvoiceService.getInstance();
    List<Invoice> invoiceStatus = invoiceServices.getInvoicesStatusNDue();

    if (invoiceStatus != null) {
        java.util.Date today = new java.util.Date(); // current date

        for (Invoice invoice : invoiceStatus) {
            if ("Pending".equals(invoice.getStatus()) 
                    && invoice.getDueDate() != null 
                    && invoice.getDueDate().before(today)) {
                notificationCount++;
            }
        }
    }


%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= roleName %> Dashboard - Pahana Edu</title>
<link rel="stylesheet" href="css/dashboardStyle.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

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
    
    function notification() {
        const popup = document.getElementById("notificationPopup");
        popup.classList.toggle("open");
    }
    
    function openSettings() {
        const popup = document.getElementById("settingsModal");
        popup.classList.toggle("open");
    }
    
    function openHelp() {
        window.location.href = "help.jsp"; 
    }

    function setMenu() {
        const dropdown = document.getElementById("menuDropdown");
        dropdown.classList.toggle("open");
    }

    // Close popups/menus when clicking outside
    document.addEventListener("click", function(event) {
        const profilePopup = document.getElementById("profilePopup");
        const menuDropdown = document.getElementById("menuDropdown");
        const notificationPopup = document.getElementById("notificationPopup");
        const settingsModal = document.getElementById("settingsModal");

        if (!event.target.closest("#profilePopup") && !event.target.closest("[title='Profile']")) {
            profilePopup.classList.remove("open");
        }
        if (!event.target.closest("#menuDropdown") && !event.target.closest("[onclick='setMenu()']")) {
            menuDropdown.classList.remove("open");
        }
        if (!event.target.closest("#notificationPopup") && !event.target.closest("[title='You have <%= notificationCount %> notifications']")) {
            notificationPopup.classList.remove("open");
        }
        if (!event.target.closest("#settingsModal") && !event.target.closest("[title='Settings']")) {
        	settingsModal.classList.remove("open");
        }

    });
</script>

<style>
    /* Better toggle handling with CSS class */
    .open { display: block !important; }
    #profilePopup, #menuDropdown, #notificationPopup, #settingsModal, #transactionMenu, #operationMenu, #reportMenu {
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
           <!--  <button class="icon-btn" onclick="notification()" title="Notifications">🔔</button> -->
           <button class="icon-btn" onclick="notification()" title="You have <%= notificationCount %> notifications">
    🔔
    <% if (notificationCount > 0) { %>
        <span class="notification-badge"><%= notificationCount %></span>
    <% } %>
</button>
           

            <!-- <button class="icon-btn" title="Settings">⚙️</button> -->
            <button class="icon-btn" title="Settings" onclick="openSettings()">⚙️</button>
            <button class="icon-btn" onclick="userProfile()" title="Profile">👤</button>
            <button class="icon-btn" onclick="openHelp()" title="Help">❓</button>
        </div>
        <button class="icon-btn menu-btn" onclick="setMenu()" title="Menu">☰</button>
    </div>
    
    <!-- Dropdown Menu -->
    <div id="menuDropdown" class="menu-dropdown">
        <button class="icon-btn" onclick="refreshPage()">🔄</button>
        <!-- <button class="icon-btn" title="Notifications">🔔</button> -->
        <button class="icon-btn" onclick="notification()" title="You have <%= notificationCount %> notifications">
    🔔
    <% if (notificationCount > 0) { %>
        <span class="notification-badge"><%= notificationCount %></span>
    <% } %>
</button>
        <!-- <button class="icon-btn" title="Settings">⚙️</button> -->
        <button class="icon-btn" title="Settings" onclick="openSettings()">⚙️</button>
        <button class="icon-btn" onclick="userProfile()" title="Profile">👤</button>
        <button class="icon-btn" onclick="openHelp()" title="Help">❓</button>
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
    <button onclick="toggleMenu('transactionMenu')">Invoice</button>
    <!-- <div id="transactionMenu" style="padding-left:10px;">
    <form action="InvoiceServlet" method="get">
     <input type="hidden" name="action" value="invoice">
          <button type="submit" class="btn" >Generate Bill</button>


        </form>
    
    </div> -->
<div id="transactionMenu" style="padding-left:10px;">
    <button type="button" class="btn" onclick="openInvoicePopup('InvoiceServlet?action=invoice&username=<%= username %>')">Generate Bill</button>
</div>

<script>
function openInvoicePopup(url) {
    const w = screen.width;
    const h = screen.height;

    window.open(
    	 url,  
    	"popupWindow",         
        "width=" + w + ",height=" + h + ",top=0,left=0,scrollbars=yes,resizable=yes"
    );
}
</script>

    
    


    <% if ("Admin".equals(roleName)) { %>
    <!-- Operation Menu -->
    <!-- <button onclick="toggleMenu('operationMenu')">Operation</button>
    <div id="operationMenu" style="padding-left:10px;">
        <a href="userRegister.jsp" class="button-link">Register Users</a>
        <a href="customerRegister.jsp" class="button-link">Register Customers</a>
        <a href="addItem.jsp" class="button-link">Add Items</a>
    </div> -->
    <button onclick="toggleMenu('operationMenu')">Operation</button>
<div id="operationMenu" style="padding-left:10px;">
    <a href="javascript:void(0)" class="button-link" onclick="openPopup('userRegister.jsp')">Register Users</a>
    <a href="javascript:void(0)" class="button-link" onclick="openPopup('customerRegister.jsp?roleName=<%= roleName %>')">Register Customers</a>
    <a href="javascript:void(0)" class="button-link" onclick="openPopup('addItem.jsp')">Add Items</a>
</div>

<script>
function openPopup(url) {
	
	   const w = screen.width;
	   const h = screen.height;
	    
    window.open(
        url,
        "popupWindow",
        "width=" + w + ",height=" + h + ",top=0,left=0,scrollbars=yes,resizable=yes"
    );
}
</script>
    
    <% } %>

    <!-- Reports Menu -->
    <button onclick="toggleMenu('reportMenu')">Reports</button>
    <div id="reportMenu" style="padding-left:10px;">
    
      <% if ("Admin".equals(roleName)) { %>
      
       <button type="button" class="btn" onclick="openPopup('UserServlet?action=userReport')">User Report</button>

    <button type="button" class="btn" onclick="openPopup('CustomerServlet?roleName=<%= roleName %>')">Customer Report</button>

    <button type="button" class="btn" onclick="openPopup('ItemServlet?action=itemReport')">Item Report</button>

    <button type="button" class="btn" onclick="openPopup('InvoiceServlet?action=invoiceReport&username=<%= username %>')">Invoice Report</button>
<!-- </div> -->


<script>
function openPopup(url) {
	
	   const w = screen.width;
	   const h = screen.height;
	    
 window.open(
     url,
     "popupWindow",
     "width=" + w + ",height=" + h + ",top=0,left=0,scrollbars=yes,resizable=yes"
 );
}
</script>
<%--         <form action="UserServlet" method="get">
      

            <button type="submit" class="btn">User Report</button>
        </form>
        
      <form action="CustomerServlet" method="get">
         <input type="hidden" name="roleName" value="<%= roleName %>">

            <button type="submit" class="btn">Customer Report</button>
        </form> 
        
        
        <form action="ItemServlet" method="get">
       

            <button type="submit" class="btn">Item Report</button>
        </form>
        <form action="InvoiceServlet" method="get">
      

            <button type="submit" class="btn">Invoice Report</button>
        </form> --%>
        
          <% }else{
        	  %>

		<button type="button" class="btn"
			onclick="openPopup('CustomerServlet?roleName=<%=roleName%>')">Customer
			Report</button>

		<script>
			function openPopup(url) {

				const w = screen.width;
				const h = screen.height;

				window.open(url, "popupWindow", "width=" + w + ",height=" + h
						+ ",top=0,left=0,scrollbars=yes,resizable=yes");
			}
		</script>
		<%-- 	  
        	    <form action="CustomerServlet" method="get">
         <input type="hidden" name="roleName" value="<%= roleName %>">

            <button type="submit" class="btn">Customer Report</button>
        </form> --%>
           <% } %>
        
        
        <%--   <form action="CustomerServlet" method="get">
         <input type="hidden" name="roleName" value="<%= roleName %>">

            <button type="submit" class="btn">Customer Report</button>
        </form> --%>
    </div>
    
    <div class="space">
   
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

<!-- notification Popup -->
<%-- <div id="notificationPopup" class="notification-popup">
    <p><strong><%= item.getItemName() %></strong></p>
    <small>(<%= item.getQuantity() %>)</small>
    <hr>
   <!--  <a href="logout" class="logout-btn">Logout</a> -->
</div> --%>
<%-- <div id="notificationPopup" class="notification-popup">
    <% 
   
        if (items != null && !items.isEmpty()) {
            for (Item item : items) {
                if (item.getQuantity() == 0) {  // ✅ Properly inside Java block
                	/* notificationCount++; */
    %>
                    <p><strong><%= item.getItemName() %></strong></p>
                    <small>(Qty: <%= item.getQuantity() %>)</small>
                    <hr>
                    
    <% 
                }
            }
        } else {
    %>
        <p>No notifications</p>
    <% 
        } 
    %>
</div> --%>

<div id="notificationPopup" class="notification-popup">
    <% 
    if ("Admin".equals(roleName)) {
        // Show item notifications
        if (items != null && !items.isEmpty()) {
            for (Item item : items) {
                if (item.getQuantity() == 0) {  
    %>
                    <p><strong><%= item.getItemName() %></strong></p>
                    <small>(Qty: <%= item.getQuantity() %>)</small>
                    <hr>
    <% 
                }
            }
        }
        
    }

        // Show invoice notifications
        if (invoiceStatus != null) {
            java.util.Date today = new java.util.Date(); // current date
            for (Invoice invoice : invoiceStatus) {
                if ("Pending".equals(invoice.getStatus()) 
                        && invoice.getDueDate() != null 
                        && invoice.getDueDate().before(today)) {  
    %>
                    <p><strong>Invoice No: <%= invoice.getInvoiceNo() %></strong></p>
                    <small>(Overdue date: <%= invoice.getDueDate() %>)</small>
                    <hr>
    <% 
                }
            }
        }

        // If no notifications at all
        if ((items == null || items.isEmpty()) && (invoiceStatus == null || invoiceStatus.isEmpty())) {
    %>
            <p>No notifications</p>
    <% 
        }
    %>
</div>

<div id="settingsModal" class="settings-modal">
  <div class="settings-content">
    <span class="close-btn" onclick="openSettings()">&times;</span>
    <h2>⚙️ Settings</h2>

    <form action="UserServlet" method="post">
        <input type="hidden" name="action" value="updateSettings">
       <input type="hidden" name="userName" value="<%= username %>">

        <label>Change Password:</label>
        <input type="password" name="newPassword"><br><br>

        <label>Theme:</label>
        <select name="theme">
            <option value="light">Light</option>
            <option value="dark">Dark</option>
        </select><br><br>

        <button type="submit" class="save-btn">Save</button>
    </form>
  </div>
</div>



<script>
<%
String error = (String) request.getAttribute("error");
String message = (String) request.getAttribute("message");

%>

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
