package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		            case "update":
						/* deleteItem(request, response); */
		                break;
		            default:
		            	listUsers(request, response);
		                break;
		           
		    			
		        }
	       
	    }
	       
	   


		@Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	            throws ServletException, IOException {

	        String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        String role = request.getParameter("role");
	        
	        // Validation rules
	        String usernameRegex = "^[a-zA-Z0-9]{4,}$"; // letters & numbers only, min 4 chars
	        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"; // min 6 chars, 1 letter, 1 number

	        // Check empty fields
			/*
			 * if (username.isEmpty() || password.isEmpty()) {
			 * request.setAttribute("errorMessage", "Username and password are required.");
			 * request.getRequestDispatcher("userRegister.jsp").forward(request, response);
			 * return; }
			 */

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

	        User newUser = new User(username, password, role);

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
}
