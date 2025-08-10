package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import service.LoginService;


/*@WebServlet("/LoginServlet")*/
public class LoginServlet extends HttpServlet {
	
	 private static final long serialVersionUID = 1L;
	    private LoginService loginService;

	    @Override
	    public void init() throws ServletException {
	        loginService = new LoginService();
	    }

	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	            throws ServletException, IOException {

	        String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        
	        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
	            request.setAttribute("error", "Username and Password are required.");
	            request.getRequestDispatcher("login.jsp").forward(request, response);
	            return;
	        }

	        try {
	        	
	        
	        User user = loginService.authenticate(username, password);

	        if (user != null) {
	            // Store user details in session
	            HttpSession session = request.getSession();
	            session.setAttribute("username", user.getUsername());
	            session.setAttribute("role", user.getRole());

	            // Redirect based on role
	            if ("admin".equalsIgnoreCase(user.getRole())) {
	                response.sendRedirect("dashboard.jsp"); // Goes to DashboardServlet
	                System.out.println("Admin is successfull");
	            } else {
	                response.sendRedirect("dashboard.jsp"); // Can be changed to user dashboard
	                System.out.println("User login is successfull");
	            } 

	        } else {
	            // Invalid login
	            request.setAttribute("error", "Invalid username or password!");
	            request.getRequestDispatcher("login.jsp").forward(request, response);
	            System.out.println("Invalid Username or Password");
	        }
	        
	    } catch (Exception e) {
            // 3️⃣ Database error handling
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
            System.out.println("Database error: " + e.getMessage());
        }
	    }

}
