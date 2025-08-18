package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


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
	    
	    public List<BillDetail> getBillDetailsByInvoice(String invoiceNo) {
	        List<BillDetail> billList = new ArrayList<>();
	        String sql = "SELECT * FROM bill_details WHERE invoice_no = ?";

	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setString(1, invoiceNo);
	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    BillDetail bill = new BillDetail();
	                    bill.setItemCode(rs.getString("item_code"));
	                    bill.setItemName(rs.getString("item_name"));
	                    bill.setDescription(rs.getString("description"));
	                    bill.setPrice(rs.getDouble("price"));
	                    bill.setQuantity(rs.getInt("quantity"));
	                    bill.setTotal(rs.getDouble("total"));
	                    billList.add(bill);
	                }
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return billList;
	    }


}
