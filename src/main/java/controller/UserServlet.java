package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Customer;
import model.Item;
import model.User;
import service.UserService;


/*@WebServlet("/UserServlet")*/
public class UserServlet extends HttpServlet {
	 private static final long serialVersionUID = 1L;
	    private UserService userService;

	    @Override
	    public void init() throws ServletException {
	        userService = new UserService();
	    }

	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	            throws ServletException, IOException {
	    	
	    	 String action = request.getParameter("action");
		        if (action == null) {
		            action = "list";
		        }

		        switch (action) {
		            case "edit":
						editUser(request, response);
		                break;
		            default:
		            	listUsers(request, response);
		                break;
		           
		    			
		        }
	       
	    }
	       
	   


		@Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	            throws ServletException, IOException {

			String action = request.getParameter("action");

			if (action == null) {
				action = "insert";
			}

			switch (action) {
			case "status":
				changeStatus(request, response);
				break;
			case "delete":
				deleteUser(request, response);
				break;
			case "update":
				updateUser(request, response);
				break;
			case "insert":
			default:
				insertUser(request, response);
				break;
			}
}
		
		
		private void insertUser(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			
			   String userId = request.getParameter("userId");
			   String username = request.getParameter("username");
		        String password = request.getParameter("password");
		        String role = request.getParameter("role");
		        
		        // Validation rules
		        String usernameRegex = "^[a-zA-Z0-9]{4,}$"; // letters & numbers only, min 4 chars
		        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"; // min 6 chars, 1 letter, 1 number

		      
		        // Validate username
		        if (!username.matches(usernameRegex)) {
		            request.setAttribute("error", "Username must be at least 4 characters and contain only letters and numbers.");
		            request.setAttribute("mainContent", "userRegister.jsp");
		            request.getRequestDispatcher("userRegister.jsp").forward(request, response);
		            return;
		        }

		        // Validate password
		        if (!password.matches(passwordRegex)) {
		            request.setAttribute("error", "Password must be at least 6 characters, with at least one letter and one number.");
		            request.setAttribute("mainContent", "userRegister.jsp");
		            request.getRequestDispatcher("userRegister.jsp").forward(request, response);
		            return;
		        }

		        // Check if already exists
		        if (userService.getUserByUsername(username) != null) {
		            request.setAttribute("error", "Username already exists!");
		            request.setAttribute("mainContent", "userRegister.jsp");
		            request.getRequestDispatcher("userRegister.jsp").forward(request, response);
		            return;
		        }
		        
		        if (userService.getUserByPassword(password) != null) {
		            request.setAttribute("error", "Password already exists!");
		            request.setAttribute("mainContent", "userRegister.jsp");
		            request.getRequestDispatcher("userRegister.jsp").forward(request, response);
		            return;
		        }
		        
		        try {
		        
		        User newUser = new User(userId,username, password, role);

		        if (userService.addUser(newUser)) {
		            request.setAttribute("message", "User Registration successful! You can now log in.");
		            System.out.println("User Registration successful! You can now log in");
		        } else {
		            request.setAttribute("error", "Error occurred during registration.");
		            System.out.println("Error occurred during registration.");
		        }
		        
		        request.setAttribute("mainContent", "userRegister.jsp");
		        request.getRequestDispatcher("userRegister.jsp").forward(request, response);
		       
		    
		        
		    }catch (Exception e) {
	            // 3️⃣ Database error handling
	            request.setAttribute("error", "Database error: " + e.getMessage());
	            request.getRequestDispatcher("userRegister.jsp").forward(request, response);
	            System.out.println("Database error: " + e.getMessage());
	        }

		}
		
		
		private void updateUser(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException {

		    String userId = request.getParameter("userId"); // the primary key
		    String username = request.getParameter("username");
		    String password = request.getParameter("password");
		    String role = request.getParameter("role");
//		    String status = request.getParameter("status");
		  
			
			  if (userId == null || userId.isEmpty()) { request.setAttribute("error",
			  "User ID is missing!");
					/* forwardToUpdatePage(request, response); */
			  request.getRequestDispatcher("updateUser.jsp").forward(request, response);
			  return; }
			 
		    
		    // Validation rules
	        String usernameRegex = "^[a-zA-Z0-9]{4,}$"; // letters & numbers only, min 4 chars
	        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"; // min 6 chars, 1 letter, 1 number

	      
	        // Validate username
	        if (!username.matches(usernameRegex)) {
				/* forwardToUpdatePage(request, response); */
	            request.setAttribute("error", "Username must be at least 4 characters and contain only letters and numbers.");
	            request.getRequestDispatcher("updateUser.jsp").forward(request, response);
				/* forwardToUpdatePage(request, response); */
	            return;
	        }

	        // Validate password
	        if (!password.matches(passwordRegex)) {
				/* forwardToUpdatePage(request, response); */
	            request.setAttribute("error", "Password must be at least 6 characters, with at least one letter and one number.");
	            request.getRequestDispatcher("updateUser.jsp").forward(request, response);
				/* forwardToUpdatePage(request, response); */
	            return;
	        }

	        

		    try {
				/* User user = userService.getUserById(userId); */
		        User existingUser = new User(userId, username, password, role);

		        if (userService.updateUser(existingUser)) {
					/* request.setAttribute("user", user); */
					/* forwardToUpdatePage(request, response); */
		        	List<User> users = userService.getAllUsers();
			        request.setAttribute("users", users);
			        
		            request.setAttribute("message", "User updated successfully!");
		            System.out.println("User updated successfully!");
					/* response.sendRedirect("UserServlet?action=list"); */
		            request.getRequestDispatcher("updateUser.jsp").forward(request, response);
		        } else {
					/* forwardToUpdatePage(request, response); */
		            request.setAttribute("error", "Failed to update user.");
		            System.out.println("Failed to update user.");
		            request.getRequestDispatcher("updateUser.jsp").forward(request, response);
					/* forwardToUpdatePage(request, response); */
		        }
		        
				/* request.getRequestDispatcher("updateUser.jsp").forward(request, response); */

		    } catch (Exception e) {
		        e.printStackTrace();
				/* forwardToUpdatePage(request, response); */
		        request.setAttribute("error", "Database error: " + e.getMessage());
				/* forwardToUpdatePage(request, response); */
	            System.out.println("Database error: " + e.getMessage());
	            request.getRequestDispatcher("updateUser.jsp").forward(request, response);
		    }

		   
		}
		


		
//		private void forwardToUpdatePage(HttpServletRequest request, HttpServletResponse response)
//		        throws ServletException, IOException {
//		    // Preserve the user data for the form
//		    if (request.getAttribute("user") == null) {
//		        String userId = request.getParameter("userId");
//		        if (userId != null) {
//		            try {
//		                User user = userService.getUserById(userId);
//		                request.setAttribute("user", user);
//		            } catch (Exception e) {
//		            	 e.printStackTrace();
//		 		        request.setAttribute("error", "Database error: " + e.getMessage());
//		            }
//		        }
//		    }
//			/* request.getRequestDispatcher("updateUser.jsp").forward(request, response); */
//		}



		
		private void editUser(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException {
		    try {
			String userId = request.getParameter("userId");
		    User user = userService.getUserById(userId);

		    if (user != null) {
		        request.setAttribute("user", user);
		        request.getRequestDispatcher("updateUser.jsp").forward(request, response);
		    } else {
		        request.setAttribute("error", "User not found");
		        listUsers(request, response);
		    }
		 } catch (Exception e) {
		        e.printStackTrace();
		        request.setAttribute("error", "Error loading user id: " + e.getMessage());
		        request.getRequestDispatcher("updateUser.jsp").forward(request, response);
		    }
		}
		
		
		private void listUsers(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			
			
			 try {
			        List<User> users = userService.getAllUsers();
			        
			        // Debug print
			        System.out.println("==== User List ====");
			        for (User user : users) {
			            System.out.println("Username: " + user.getUsername() + 
			                             ", Password: " + user.getPassword() + 
			                             ", Role: " + user.getRole() + 
			                             ", Status: " + user.getStatus());
			        }
			        
			        // Set attribute and forward
			        request.setAttribute("users", users);
			        request.getRequestDispatcher("userReport.jsp").forward(request, response);
			        
			    } catch (Exception e) {
			        e.printStackTrace();
			        request.setAttribute("error", "Error loading user data: " + e.getMessage());
			        request.getRequestDispatcher("userReport.jsp").forward(request, response);
			    }
			
		}
		
		
		private void deleteUser(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException {
		    String userId = request.getParameter("userId");
		    
		    if (userId == null || userId.isEmpty()) { request.setAttribute("error",
					  "User ID is missing!");
		    System.out.println("User ID is missing!");
				/* forwardToUpdatePage(request, response); */
					  request.getRequestDispatcher("userReport.jsp").forward(request, response);
					  return; }

		    try {
		        boolean deleted = userService.deleteUserById(userId);

		        if (deleted) {
		            request.setAttribute("message", "User deleted successfully!");
		            System.out.println("User deleted successfully!");
		        } else {
		            request.setAttribute("error", "Failed to delete user.");
		            System.out.println("Failed to delete user.");
		        }

		        List<User> users = userService.getAllUsers();
		        request.setAttribute("users", users);
		        request.getRequestDispatcher("userReport.jsp").forward(request, response);

		    } catch (Exception e) {
		        e.printStackTrace();
		        request.setAttribute("error", "Error deleting user: " + e.getMessage());
		        request.getRequestDispatcher("userReport.jsp").forward(request, response);
		        System.out.println("Error deleting user: " + e.getMessage());
		    }
		}

		
		
		
		private void changeStatus(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException {
		    String userId = request.getParameter("userId");
		    
		    if (userId == null || userId.isEmpty()) { request.setAttribute("error",
					  "User ID is missing!");
		    System.out.println("User ID is missing!");
				/* forwardToUpdatePage(request, response); */
					  request.getRequestDispatcher("userReport.jsp").forward(request, response);
					  return; }

		    try {
		        User user = userService.getUserById(userId);
		        if (user != null) {
		            // Toggle status
		            String newStatus = "Active".equalsIgnoreCase(user.getStatus()) ? "Disabled" : "Active";
		            System.out.println("User status "+ user.getStatus());
		            user.setStatus(newStatus);

		            boolean updated = userService.updateUserStatus(userId, newStatus);

		            if (updated) {
		                request.setAttribute("message", "User status updated successfully!");
		                System.out.println("User status updated successfully!");
		            } else {
		                request.setAttribute("error", "Failed to update user status.");
		                System.out.println("Failed to update user status.");
		            }
		        } else {
		            request.setAttribute("error", "User not found.");
		            System.out.println("User not found.");
		        }

		        // Redirect back to report
		        List<User> users = userService.getAllUsers();
		        request.setAttribute("users", users);
		        request.getRequestDispatcher("userReport.jsp").forward(request, response);

		    } catch (Exception e) {
		        e.printStackTrace();
		        request.setAttribute("error", "Error changing user status: " + e.getMessage());
		        request.getRequestDispatcher("userReport.jsp").forward(request, response);
		        System.out.println("Error changing user status: " + e.getMessage());
		    }
		}

}
