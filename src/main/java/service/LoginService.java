package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connection.DBConnection;
import model.User;

public class LoginService {
	
	  public User authenticate(String username, String password) {
	        User user = null;

	        String sql = "SELECT username, password, role FROM users WHERE username = ? AND password = ?";

	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setString(1, username);
	            stmt.setString(2, password);

	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                user = new User();
	                user.setUsername(rs.getString("username"));
	                user.setPassword(rs.getString("password"));
	                user.setRole(rs.getString("role"));
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return user;
	    }

}
