<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="java.util.List" %>
<%@ page import="model.Invoice" %>
<%
    List<Invoice> invoices = (List<Invoice>) request.getAttribute("invoices");
String invoiceJson = new com.google.gson.Gson().toJson(invoices);
%>

<html>
<head>
    <meta charset="UTF-8">
    <title>Main Menu</title>
    <link rel="stylesheet" href="css/mainMenuStyle.css">
    <!-- Chart.js CDN -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

<!-- Dashboard Card -->
<div class="dashboard-card">
    <h2 class="dashboard-title">Dashboard</h2>
</div>

<div class="charts-container">
    <div class="chart-card">
        <h3>Monthly Transactions</h3>
        <canvas id="monthlyTransactionsChart"></canvas>
    </div>
    <div class="chart-card">
        <h3>Item Category Distribution</h3>
        <canvas id="itemCategoryChart"></canvas>
    </div>
</div>


<!-- Daily Invoices Table Section -->
<div class="charts-container">
    <div class="chart-card full-width">
        <h3>Daily Invoices</h3>
        <div class="responsive-table">
            <table class="invoice-table">
                <thead>
                    <tr>
                        <th>Invoice No</th>
                        <th>Date</th>
                        <th>Customer</th>
                        <th>Total Amount</th>
                        <th>Status</th>
                    </tr>
                </thead>
                 <tbody id="invoiceTableBody">
                <!-- Rows will be inserted by JavaScript -->
            </tbody>
            </table>
        </div>
    </div>
</div>


<!-- Chart.js CDN -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
    // Monthly Transactions Bar Chart
    const ctx1 = document.getElementById('monthlyTransactionsChart').getContext('2d');
    new Chart(ctx1, {
        type: 'bar',
        data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'],
            datasets: [{
                label: 'Transactions',
                data: [12, 19, 3, 5, 2, 3, 9],
                backgroundColor: '#4e73df'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });

    // Item Category Pie Chart
    const ctx2 = document.getElementById('itemCategoryChart').getContext('2d');
    new Chart(ctx2, {
        type: 'pie',
        data: {
            labels: ['Electronics', 'Clothing', 'Groceries', 'Books'],
            datasets: [{
                label: 'Categories',
                data: [10, 15, 25, 5],
                backgroundColor: ['#1cc88a', '#36b9cc', '#f6c23e', '#e74a3b']
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
    
    // Example data (in real case, fetch from server)
    const invoiceData = [
        { no: "INV001", date: "2025-08-06", customer: "John Doe", amount: "$250.00", status: "Paid" },
        { no: "INV002", date: "2025-08-06", customer: "Jane Smith", amount: "$180.00", status: "Pending" },
        { no: "INV003", date: "2025-08-06", customer: "Michael Lee", amount: "$320.00", status: "Paid" },
        { no: "INV004", date: "2025-08-06", customer: "Saman", amount: "$520.00", status: "Overdue" }
    ]; 
    
    function getStatusClass(status) {
        switch (status.toLowerCase()) {
            case "paid": return "status-paid";
            case "pending": return "status-pending";
            case "overdue": return "status-overdue";
            default: return "";
        }
    }

    function populateInvoiceTable(data) {
        const tableBody = document.getElementById("invoiceTableBody");
        tableBody.innerHTML = ""; // Clear old data

        data.forEach(inv => {
            const row = `<tr>
                <td>${inv.no}</td>
                <td>${inv.date}</td>
                <td>${inv.customer}</td>
                <td>${inv.amount}</td>
                <td class="${getStatusClass(inv.status)}">${inv.status}</td>
            </tr>`;
            tableBody.innerHTML += row;
        });
    }

    // Populate table on page load
    populateInvoiceTable(invoiceData);
</script>

</body>
</html>
