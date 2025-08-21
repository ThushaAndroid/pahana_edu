<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Pahana Edu Bookshop - Help</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 800px;
            margin: 50px auto;
            background: #fff;
            padding: 30px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            border-radius: 10px;
        }
        h1 {
            color: #333;
            text-align: center;
        }
        h2 {
            color: #555;
        }
        ul {
            line-height: 1.8;
        }
        a.button {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        a.button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Pahana Edu Bookshop - Help</h1>
    <h2>System Usage Guidelines</h2>
    <ul>
        <li><strong>Login:</strong> Use your assigned username and password to access the system.</li>
        <li><strong>Add Customer:</strong> Register new customers by providing account number, name, address, phone number, and units consumed.</li>
        <li><strong>Edit Customer:</strong> Update existing customer information whenever needed.</li>
        <li><strong>Display Customer Details:</strong> Search for a customer by account number to view their information.</li>
        <li><strong>Manage Items:</strong> Add, update, or delete items available in the bookstore.</li>
        <li><strong>Calculate & Print Bill:</strong> Compute the bill for a customer based on units/items consumed.</li>
        <li><strong>Help:</strong> Access this page for guidance on using the system.</li>
        <li><strong>Exit:</strong> Log out or close the application safely.</li>
    </ul>
    <a class="button" href="dashboard.jsp">Back to Home</a>
</div>
</body>
</html>
