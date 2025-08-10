package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connection.DBConnection;
import model.User;

public class UserService {

	public boolean addUser(User user) {
	    String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, user.getUsername());
	        stmt.setString(2, user.getPassword());
	        stmt.setString(3, user.getRole());
	        return stmt.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public User getUserByUsername(String username) {
	    String sql = "SELECT * FROM users WHERE username = ?";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, username);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return new User(rs.getString("username"), rs.getString("password"), rs.getString("role"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public User getUserByPassword(String password) {
	    String sql = "SELECT * FROM users WHERE password = ?";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, password);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return new User(rs.getString("username"), rs.getString("password"), rs.getString("role"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

}
