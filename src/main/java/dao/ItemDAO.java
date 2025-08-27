package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import connection.DBConnectionFactory;
import model.Item;

public class ItemDAO {

    public String generateNextItemCode() {
        String prefix = "ITMC";
        String newItemCode = prefix + "001";

        try (Connection conn =  DBConnectionFactory.getConnection()) {
            String sql = "SELECT item_code FROM items ORDER BY item_code DESC LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    String lastItc = rs.getString("item_code");
                    int lastNum = Integer.parseInt(lastItc.replaceAll("\\D+", ""));
                    newItemCode = String.format("%s%03d", prefix, lastNum + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newItemCode;
    }

    public boolean addItem(Item item) {
        String sql = "INSERT INTO items (item_code, item_name, description, price, quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn =  DBConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getItemCode());
            stmt.setString(2, item.getItemName());
            stmt.setString(3, item.getDescription());
            stmt.setDouble(4, item.getPrice());
            stmt.setInt(5, item.getQuantity());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Item getItemByCode(String itemCode) {
        String sql = "SELECT * FROM items WHERE item_code = ?";
        try (Connection conn =  DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, itemCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Item(
                        rs.getString("item_code"),
                        rs.getString("item_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
	 public Item getItemByName(String itemName) {
    String sql = "SELECT * FROM items WHERE item_name = ?";
    try (Connection con =  DBConnectionFactory.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, itemName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Item(
                rs.getString("item_code"),
                rs.getString("item_name"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("quantity")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

    public boolean updateItem(Item item) {
        String sql = "UPDATE items SET item_name = ?, description = ?, price = ?, quantity = ? WHERE item_code = ?";
        try (Connection conn =  DBConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getItemName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getPrice());
            stmt.setInt(4, item.getQuantity());
            stmt.setString(5, item.getItemCode());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteItem(String itemCode) {
        String sql = "DELETE FROM items WHERE item_code=?";
        try (Connection conn =  DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, itemCode);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";

        try (Connection conn =  DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(new Item(
                        rs.getString("item_code"),
                        rs.getString("item_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    public List<Item> searchItems(String query) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE item_name LIKE ? OR item_code LIKE ?";

        try (Connection conn =  DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchTerm = "%" + query + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new Item(
                        rs.getString("item_code"),
                        rs.getString("item_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
    
    public int getQtyByCode(String itemCode) {
        int qty = 0;
        String sql = "SELECT quantity FROM items WHERE item_code = ?";

        try (Connection conn =  DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, itemCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    qty = rs.getInt("quantity");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return qty;
    }

    
    public boolean updateItemQty(String itemCode, int qty) {
        String sql = "UPDATE items SET quantity = ? WHERE item_code = ?";
        try (Connection conn =  DBConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

           
            stmt.setInt(1, qty);
            stmt.setString(2, itemCode);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

