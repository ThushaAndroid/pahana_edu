package model;

public class User {
	 private String user_id;
	 private String username;
	 private String password;
	 private String role;
	 private String status;
	 
	 public User(String user_id, String username, String password, String role, String status) {
			super();
			this.user_id = user_id;
			this.username = username;
			this.password = password;
			this.role = role;
			this.status = status;
		}
	 
		/*
		 * public User(String username, String password, String role, String status) {
		 * super(); this.username = username; this.password = password; this.role =
		 * role; this.status = status; }
		 */
	
	public User(String user_id,String username, String password, String role) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.role = role;
	
	}
	
	
	
	public User() {
	
	}


	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	} 

}
