package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import model.Invoice;


public class InvoiceService {
	
	public String generateNextInvoiceNo() {
	    String prefix = "INV";
	    String newInvoiceNo = prefix + "001"; // default if no customers yet

	    try (Connection conn = DBConnection.getConnection()) {
	        String sql = "SELECT invoice_no FROM invoices ORDER BY invoice_no DESC LIMIT 1";
	        try (PreparedStatement ps = conn.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {

	            if (rs.next()) {
	                String lastInv = rs.getString("invoice_no"); // e.g., CUST005
	                int lastNum = Integer.parseInt(lastInv.replaceAll("\\D+", "")); // Extract number
	                newInvoiceNo = String.format("%s%03d", prefix, lastNum + 1); // CUST006
	                
	                
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // log properly in real apps
	    }

	    return newInvoiceNo;
	}
	
	public List<Invoice> getAllInvoices() {
	    List<Invoice> invoices = new ArrayList<>();
	    String sql = "SELECT invoice_no, customer_name, invoice_date, due_date, total_amount, balance, status FROM invoices";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            Invoice invoice = new Invoice();
	            invoice.setInvoiceNo(rs.getString("invoice_no"));
	            invoice.setCustomerName(rs.getString("customer_name"));
	            invoice.setInvoiceDate(rs.getDate("invoice_date"));
	            invoice.setDueDate(rs.getDate("due_date"));
	            invoice.setTotalAmount(rs.getDouble("total_amount"));
	            invoice.setBalance(rs.getDouble("balance"));
	            invoice.setStatus(rs.getString("status"));
	            invoices.add(invoice);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return invoices;
	}
	
	
	public boolean insertInvoice(Invoice invoice) {
	    String sql = "INSERT INTO invoices (invoice_no, customer_name, invoice_date, due_date, total_amount, cash, balance, status) " +
	                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, invoice.getInvoiceNo());
	        ps.setString(2, invoice.getCustomerName());
	        ps.setDate(3, new java.sql.Date(invoice.getInvoiceDate().getTime()));
	        ps.setDate(4, new java.sql.Date(invoice.getDueDate().getTime()));
	        ps.setDouble(5, invoice.getTotalAmount());
	        ps.setDouble(6, invoice.getCash());
	        ps.setDouble(7, invoice.getBalance());
	        ps.setString(8, invoice.getStatus());

	        return ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public boolean updateInvoice(Invoice invoice) {
	    String sql = "UPDATE invoices SET due_date=?, cash=?, balance=?, status=? " +
	                 "WHERE invoice_no=?";
	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setDate(1, new java.sql.Date(invoice.getDueDate().getTime()));
	        ps.setDouble(2, invoice.getCash());
	        ps.setDouble(3, invoice.getBalance());
	        ps.setString(4, invoice.getStatus());
	        ps.setString(5, invoice.getInvoiceNo());

	        return ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public boolean deleteInvoice(String invoiceNo) {
	    String sql = "DELETE FROM invoices WHERE invoice_no=?";
	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, invoiceNo);
	        return ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	
	public Invoice getInvoiceByNo(String invoiceNo) {
	    Invoice invoice = null;
	    String sql = "SELECT * FROM invoices WHERE invoice_no = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, invoiceNo);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                invoice = new Invoice();
	                invoice.setInvoiceNo(rs.getString("invoice_no"));
	                invoice.setCustomerName(rs.getString("customer_name"));
	                invoice.setInvoiceDate(rs.getDate("invoice_date"));
	                invoice.setDueDate(rs.getDate("due_date"));
	                invoice.setTotalAmount(rs.getDouble("total_amount"));
	                invoice.setBalance(rs.getDouble("balance"));
	                invoice.setStatus(rs.getString("status"));
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return invoice;
	}



}
