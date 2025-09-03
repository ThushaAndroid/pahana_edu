# Pahana Edu Billing and Customer Management System

Pahana Edu is a leading bookshop in Colombo City. This project is a **web-based Billing and Customer Management System** developed in **Java (JSP, Servlet, JDBC, MySQL)** to manage customers, items, users, and invoices efficiently.
This Java maven web application have been developed using MVC architecture, singleton design pattern and DAO design pattern.

## Features
- **User Authentication** (Login/Logout with roles: Admin & Staff)
- **User Management**: Add, update, activate/deactivate, and delete users
- **Customer Management**: Register, edit, update, and delete customer accounts
- **Item Management**: Manage book/item records (add, update, delete)
- **Invoice Management**: Generate invoices, calculate totals, apply discounts, and update customer usage
- **Reports**: Export data in **Excel** and **PDF**
- **Help Section**: Guidelines for new users
- **Notification Section**: Show empty stock items and overdue payment for user
- **setting Section**: Change user' password for current users

## Tech Stack
- **Frontend**: JSP, HTML, CSS, JavaScript
- **Backend**: Java Servlets
- **Database**: MySQL
- **Design patterns**: MVC, Singleton, DAO
- **Build Tool**: Maven
- **Server**: Apache Tomcat
- **Version Control**: Git & GitHub

## Project Structure
pahana_edu/
│── src/main/java/controller/ # Servlets
│── src/main/java/service/ # Business logic
│── src/main/java/dao/ # Data access objects
│── src/main/java/model/ # Model classes
│── src/main/webapp/ # JSP files, CSS, JS
│── database/pahanaedu.sql # Database schema
│── README.md # Project documentation
