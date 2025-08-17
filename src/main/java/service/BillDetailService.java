package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import connection.DBConnection;
import model.BillDetail;

public class BillDetailService {
	
	    public boolean insertBillDetail(BillDetail bill) {
	        String sql = "INSERT INTO bill_details (invoice_no, item_code, item_name, description, price, quantity, total) " +
	                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setString(1, bill.getInvoiceNo());
	            ps.setString(2, bill.getItemCode());
	            ps.setString(3, bill.getItemName());
	            ps.setString(4, bill.getDescription());
	            ps.setDouble(5, bill.getPrice());
	            ps.setInt(6, bill.getQuantity());
	            ps.setDouble(7, bill.getTotal());

	            return ps.executeUpdate() > 0;

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;

	}


}
