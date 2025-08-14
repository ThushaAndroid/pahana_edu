package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import model.Customer;

public class CustomerService {
	
	public String generateNextAccountNumber() {
	    String prefix = "CUST";
	    String newAccountNumber = prefix + "001"; // default if no customers yet

	    try (Connection conn = DBConnection.getConnection()) {
	        String sql = "SELECT account_number FROM customers ORDER BY account_number DESC LIMIT 1";
	        try (PreparedStatement ps = conn.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {

	            if (rs.next()) {
	                String lastAcc = rs.getString("account_number"); // e.g., CUST005
	                int lastNum = Integer.parseInt(lastAcc.replaceAll("\\D+", "")); // Extract number
	                newAccountNumber = String.format("%s%03d", prefix, lastNum + 1); // CUST006
	                
	                
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // log properly in real apps
	    }

	    return newAccountNumber;
	}

	
	 // Add new customer
    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (account_number,nic, name, address, telephone,email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customer.getAccountNumber());
            ps.setString(2, customer.getNic());
            ps.setString(3, customer.getName());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getTelephone());
            ps.setString(6, customer.getEmail());
			/*
			 * ps.setInt(5, customer.getUnitsConsumed());
			 */
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    // Get customer by nic
    public Customer getCustomerByNIC(String nic) {
        String sql = "SELECT * FROM customers WHERE nic = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nic);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Customer(
                    rs.getString("account_number"),
                    rs.getString("nic"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("telephone"),
                    rs.getString("email")                   
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get customer by account number
    public Customer getCustomerByAccount(String accountNumber) {
        String sql = "SELECT * FROM customers WHERE account_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
            	 return new Customer(
                         rs.getString("account_number"),
                         rs.getString("nic"),
                         rs.getString("name"),
                         rs.getString("address"),
                         rs.getString("telephone"),
                         rs.getString("email")                   
                     );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update customer
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET nic=?, name=?, address=?, telephone=?, email=? WHERE account_number=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
        	
        	ps.setString(1, customer.getNic());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getTelephone());
            ps.setString(5, customer.getEmail());
            ps.setString(6, customer.getAccountNumber());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete customer
    public boolean deleteCustomer(String accountNumber) {
        String sql = "DELETE FROM customers WHERE account_number=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getString("account_number"),
                        rs.getString("nic"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getInt("units_consumed") 
                    ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }
    
    
    // Search customers by name or email
    public List<Customer> searchCustomers(String query) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE name LIKE ? OR email LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchTerm = "%" + query + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                	customers.add(new Customer(
                            rs.getString("account_number"),
                            rs.getString("nic"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("telephone"),
                            rs.getString("email"),
                            rs.getInt("units_consumed") 
                			 ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }

}
