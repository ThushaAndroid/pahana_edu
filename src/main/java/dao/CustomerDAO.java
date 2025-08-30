package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import connection.DBConnectionFactory;
import model.Customer;

public class CustomerDAO {

    public String generateNextAccountNumber() {
        String prefix = "CUST";
        String newAccountNumber = prefix + "001";

        try (Connection conn =  DBConnectionFactory.getConnection()) {
            String sql = "SELECT account_number FROM customers ORDER BY account_number DESC LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    String lastAcc = rs.getString("account_number");
                    int lastNum = Integer.parseInt(lastAcc.replaceAll("\\D+", ""));
                    newAccountNumber = String.format("%s%03d", prefix, lastNum + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newAccountNumber;
    }

    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (account_number, nic, name, address, telephone, email, units_consumed) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con =  DBConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customer.getAccountNumber());
            ps.setString(2, customer.getNic());
            ps.setString(3, customer.getName());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getTelephone());
            ps.setString(6, customer.getEmail());
            ps.setInt(7, customer.getUnitsConsumed());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Customer getCustomerByNIC(String nic) {
        String sql = "SELECT * FROM customers WHERE nic=?";
        try (Connection con =  DBConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nic);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Customer getCustomerByAccount(String accountNumber) {
        String sql = "SELECT * FROM customers WHERE account_number=?";
        try (Connection con =  DBConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET nic=?, name=?, address=?, telephone=?, email=?, units_consumed=? WHERE account_number=?";
        try (Connection con =  DBConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customer.getNic());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getTelephone());
            ps.setString(5, customer.getEmail());
            ps.setInt(6, customer.getUnitsConsumed());
            ps.setString(7, customer.getAccountNumber());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCustomer(String accountNumber) {
        String sql = "DELETE FROM customers WHERE account_number=?";
        try (Connection con =  DBConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection con =  DBConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    public List<Customer> searchCustomers(String query) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE name LIKE ? OR email LIKE ? OR nic LIKE ?";

        try (Connection con =  DBConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String searchTerm = "%" + query + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            ps.setString(3, searchTerm);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws Exception {
        return new Customer(
                rs.getString("account_number"),
                rs.getString("nic"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("telephone"),
                rs.getString("email"),
                rs.getInt("units_consumed")
        );
    }
    
    
 // In CustomerDAO.java
    public Integer getUnitsByAccountNumber(String accountNumber) {
        String sql = "SELECT units_consumed FROM customers WHERE account_number = ?";
        try (Connection conn =  DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("units_consumed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // return null if not found
    }
    
    public Integer getUnitsByNic(String nic) {
    	 String sql = "SELECT units_consumed FROM customers WHERE nic = ?";
         try (Connection conn =  DBConnectionFactory.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)) {
             
             ps.setString(1, nic);
             ResultSet rs = ps.executeQuery();
             
             if (rs.next()) {
                 return rs.getInt("units_consumed");
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
         return null; // return null if not found
    }
    
    
    
//    public boolean updateUnitsByAccountNumber(String accountNumber, int units) {
//        String sql = "UPDATE customers SET units_consumed = ? WHERE account_number = ?";
//        
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            
//            ps.setInt(1, units);
//            ps.setString(2, accountNumber);
//            
//            int rowsUpdated = ps.executeUpdate();
//            return rowsUpdated > 0; // true if update succeeded
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        
//        return false; // update failed
//    }
    
    public boolean updateUnitsByAccountNumberOrNic(String accountNumberOrNic, int units) {
        String sql = "UPDATE customers SET units_consumed = ? WHERE account_number = ? OR nic = ?";
        
        try (Connection conn =  DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, units);
            ps.setString(2, accountNumberOrNic); // account_number
            ps.setString(3, accountNumberOrNic); // nic

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // true if update succeeded
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false; // update failed
    }



}

