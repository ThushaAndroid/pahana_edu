//package service;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//
//import connection.DBConnection;
//import model.User;
//
//public class UserService {
//
//	public boolean addUser(User user) {
//	    String sql = "INSERT INTO users (user_id, username, password, role) VALUES (?, ?, ?, ?)";
//	    try (Connection conn = DBConnection.getConnection();
//	         PreparedStatement stmt = conn.prepareStatement(sql)) {
//	    	stmt.setString(1, user.getUser_id());
//	        stmt.setString(2, user.getUsername());
//	        stmt.setString(3, user.getPassword());
//	        stmt.setString(4, user.getRole());
//	        return stmt.executeUpdate() > 0;
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    return false;
//	}
//
//	public User getUserByUsername(String username) {
//	    String sql = "SELECT * FROM users WHERE username = ?";
//	    try (Connection conn = DBConnection.getConnection();
//	         PreparedStatement stmt = conn.prepareStatement(sql)) {
//	        stmt.setString(1, username);
//	        ResultSet rs = stmt.executeQuery();
//	        if (rs.next()) {
//	            return new User(rs.getString("user_id"),rs.getString("username"), rs.getString("password"), rs.getString("role"));
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    return null;
//	}
//	
//	public User getUserByPassword(String password) {
//	    String sql = "SELECT * FROM users WHERE password = ?";
//	    try (Connection conn = DBConnection.getConnection();
//	         PreparedStatement stmt = conn.prepareStatement(sql)) {
//	        stmt.setString(1, password);
//	        ResultSet rs = stmt.executeQuery();
//	        if (rs.next()) {
//	            return new User(rs.getString("user_id"),rs.getString("username"), rs.getString("password"), rs.getString("role"));
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    return null;
//	}
//	
//	public User getUserById(String userId) {
//	    String sql = "SELECT * FROM users WHERE user_id = ?";
//	    try (Connection conn = DBConnection.getConnection();
//	         PreparedStatement stmt = conn.prepareStatement(sql)) {
//	        stmt.setString(1, userId);
//	        ResultSet rs = stmt.executeQuery();
//	        if (rs.next()) {
//	            return new User(rs.getString("user_id"),rs.getString("username"), rs.getString("password"), rs.getString("role"),rs.getString("status"));
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    return null;
//	}
//	
//	
//	public List<User> getAllUsers() {
//	    List<User> users = new ArrayList<>();
//	    String sql = "SELECT * FROM users";
//
//	    try (Connection con = DBConnection.getConnection();
//	         PreparedStatement ps = con.prepareStatement(sql);
//	         ResultSet rs = ps.executeQuery()) {
//
//	        while (rs.next()) {
//	            User user = new User();
//	            user.setUser_id(rs.getString("user_id"));
//	            user.setUsername(rs.getString("username"));
//	            user.setRole(rs.getString("role"));
//	            user.setStatus(rs.getString("status"));;
//	            users.add(user);
//	        }
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    return users;
//	}
//	
//	
//	public boolean deleteUserById(String userId) {
//	    String sql = "DELETE FROM users WHERE user_id=?";
//	    try (Connection con = DBConnection.getConnection();
//	         PreparedStatement ps = con.prepareStatement(sql)) {
//	        ps.setString(1, userId);
//	        return ps.executeUpdate() > 0;
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return false;
//	    }
//	}
//
//	
//	
//	public boolean updateUserStatus(String userId, String status) {
//	    String sql = "UPDATE users SET status=? WHERE user_id=?";
//	    try (Connection con = DBConnection.getConnection();
//	         PreparedStatement ps = con.prepareStatement(sql)) {
//	        ps.setString(1, status);
//	        ps.setString(2, userId);
//	        return ps.executeUpdate() > 0;
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return false;
//	    }
//	}
//	
//	public String generateNextUserId() {
//	    String prefix = "USR";
//	    String newUserId = prefix + "0001"; // default if no users yet
//
//	    try (Connection conn = DBConnection.getConnection()) {
//	        String sql = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1";
//	        try (PreparedStatement ps = conn.prepareStatement(sql);
//	             ResultSet rs = ps.executeQuery()) {
//
//	            if (rs.next()) {
//	                String lastUsr = rs.getString("user_id"); // e.g., USR0005
//	                int lastNum = Integer.parseInt(lastUsr.replaceAll("\\D+", "")); // Extract number
//	                newUserId  = String.format("%s%04d", prefix, lastNum + 1); // USR0006
//	            }
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace(); // log properly in real apps
//	    }
//
//	    return newUserId;
//	}
//	
//	
//	public boolean updateUser(User user) {
//	    String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE user_id = ?";
//	    try (Connection conn = DBConnection.getConnection();
//	         PreparedStatement ps = conn.prepareStatement(sql)) {
//
//	        ps.setString(1, user.getUsername());
//	        ps.setString(2, user.getPassword());
//	        ps.setString(3, user.getRole());
//	        ps.setString(4, user.getUser_id());
//
//	        return ps.executeUpdate() > 0;
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    return false;
//	}
//
//
//
//
//}

package service;

import java.util.List;

import dao.UserDAO;
import model.User;

public class UserService {
	
	private static volatile UserService instance;

    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    // Add a new user
    public boolean addUser(User user) {
        // You can add business logic here (e.g., validation)
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            return false;
        }
        user.setUser_id(userDAO.generateNextUserId());
        return userDAO.addUser(user);
    }

    // Get a user by username
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }
    
    // Get a user by username
    public User getUserByPassword(String password) {
        return userDAO.getUserByPassword(password);
    }

    // Get a user by ID
    public User getUserById(String userId) {
        return userDAO.getUserById(userId);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // Delete user by ID
    public boolean deleteUserById(String userId) {
        return userDAO.deleteUserById(userId);
    }

    // Update user status (e.g., active/inactive)
    public boolean updateUserStatus(String userId, String status) {
        return userDAO.updateUserStatus(userId, status);
    }

    // Update user details
    public boolean updateUser(User user) {
        // Add business validation if needed
        return userDAO.updateUser(user);
    }
    
    //generate NextUserId
    public String generateNextUserId() {
    return userDAO.generateNextUserId();
    }
    
 // Update user password
    public boolean changePassword(String userName, String newPassword) {
        return userDAO.changePassword(userName, newPassword);
    }
}

