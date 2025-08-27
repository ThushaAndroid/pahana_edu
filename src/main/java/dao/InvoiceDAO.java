package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import connection.DBConnectionFactory;
import model.Invoice;

public class InvoiceDAO {

    public String generateNextInvoiceNo() {
        String prefix = "INV";
        String newInvoiceNo = prefix + "001";

        try (Connection conn = DBConnectionFactory.getConnection()) {
            String sql = "SELECT invoice_no FROM invoices ORDER BY invoice_no DESC LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    String lastInv = rs.getString("invoice_no");
                    int lastNum = Integer.parseInt(lastInv.replaceAll("\\D+", ""));
                    newInvoiceNo = String.format("%s%03d", prefix, lastNum + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newInvoiceNo;
    }

    public boolean insertInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoices (invoice_no, customer_name, customer_nic, invoice_date, due_date, discount, total_qty, total_amount, cash, balance, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, invoice.getInvoiceNo());
            ps.setString(2, invoice.getCustomerName());
            ps.setString(3, invoice.getNic());
            ps.setDate(4, new java.sql.Date(invoice.getInvoiceDate().getTime()));
            ps.setDate(5, new java.sql.Date(invoice.getDueDate().getTime()));
            ps.setDouble(6, invoice.getDiscount());
            ps.setDouble(7, invoice.getTotalQty());
            ps.setDouble(8, invoice.getTotalAmount());
            ps.setDouble(9, invoice.getCash());
            ps.setDouble(10, invoice.getBalance());
            ps.setString(11, invoice.getStatus());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateInvoice(Invoice invoice) {
        String sql = "UPDATE invoices SET due_date=?, cash=?, balance=?, status=? WHERE invoice_no=?";
        try (Connection con = DBConnectionFactory.getConnection();
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
        try (Connection con = DBConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, invoiceNo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Invoice getInvoiceByNo(String invoiceNo) {
        String sql = "SELECT * FROM invoices WHERE invoice_no=?";
        try (Connection con = DBConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, invoiceNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceNo(rs.getString("invoice_no"));
                    invoice.setCustomerName(rs.getString("customer_name"));
                    invoice.setInvoiceDate(rs.getDate("invoice_date"));
                    invoice.setDueDate(rs.getDate("due_date"));
                    invoice.setDiscount(rs.getDouble("discount"));
                    invoice.setTotalQty(rs.getInt("total_qty"));
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    invoice.setBalance(rs.getDouble("balance"));
                    invoice.setStatus(rs.getString("status"));
                    return invoice;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices";
        try (Connection con = DBConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceNo(rs.getString("invoice_no"));
                invoice.setCustomerName(rs.getString("customer_name"));
                invoice.setNic(rs.getString("customer_nic"));
                invoice.setInvoiceDate(rs.getDate("invoice_date"));
                invoice.setDueDate(rs.getDate("due_date"));
                invoice.setDiscount(rs.getDouble("discount"));
                invoice.setTotalQty(rs.getInt("total_qty"));
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
}
