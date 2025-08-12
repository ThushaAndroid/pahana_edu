package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import model.Invoice;


public class InvoiceService {
	
	public List<Invoice> getAllInvoices() {
	    List<Invoice> invoices = new ArrayList<>();
	    String sql = "SELECT * FROM users";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

			/*
			 * while (rs.next()) { Invoice invoice = new Invoice();
			 * invoice.setUsername(rs.getString("username"));
			 * invoice.setRole(rs.getString("role"));
			 * invoice.setStatus(rs.getString("status"));; invoices.add(invoice); }
			 */
	    	 invoices.add(new Invoice("INV-1001", new java.util.Date(), 1200.50, 200.00));
	         invoices.add(new Invoice("INV-1002", new java.util.Date(), 850.75, 0.00));

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return invoices;
	}

}
