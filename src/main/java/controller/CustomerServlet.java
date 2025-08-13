
package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Customer;
import model.User;
import service.CustomerService;
import service.LoginService;

/**
 * Servlet implementation class CustomerServlet
 */
/* @WebServlet("/CustomerServlet") */
public class CustomerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private CustomerService customerService = new CustomerService();

	/*
	 * @Override public void init() throws ServletException { // Initialization code
	 * here System.out.println("Servlet is being initialized"); String
	 * nextAccountNumber = customerService.generateNextAccountNumber();
	 * System.out.println("newAccountNumber "+nextAccountNumber);
	 * 
	 * request.setAttribute("accountNumber", nextAccountNumber);
	 * request.getRequestDispatcher("customerRegister.jsp").forward(request,
	 * response);
	 * 
	 * }
	 */

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if (action == null) {
			action = "list";
		}

		switch (action) {
		case "edit":
			editCustomer(request, response);
			break;
		
		/* case "list": */
		default:
			listCustomers(request, response);
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
		case "update":
			updateCustomer(request, response);
			break;
		case "delete":
			deleteCustomer(request, response);
			break;
		case "insert":
		default:
			insertCustomer(request, response);
			break;
		}
	}

	private void listCustomers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		try {
			List<Customer> customers = customerService.getAllCustomers();
	        
	        // Debug print
	        System.out.println("==== Customer List ====");
	        for (Customer customer : customers) {
	            System.out.println("AccountNumber: " + customer.getAccountNumber() + 
	                             ", Name: " + customer.getName() + 
	                             ", Nic: " + customer.getNic() + 
	                             ", Address: " + customer.getAddress());
	        }
	        
	        // Set attribute and forward
	        request.setAttribute("customers", customers);
	        request.getRequestDispatcher("customerReport.jsp").forward(request, response);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("error", "Error loading customer data: " + e.getMessage());
	        request.getRequestDispatcher("customerReport.jsp").forward(request, response);
	    }
	}

	private void insertCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * String nextAccountNumber = customerService.generateNextAccountNumber();
		 * System.out.println("newAccountNumber "+nextAccountNumber);
		 */
		String accountNumber = request.getParameter("accountNumber");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String telephone = request.getParameter("telephone");
		String nic = request.getParameter("nic");
		String email = request.getParameter("email");
		/*
		 * int unitsConsumed = Integer.parseInt(request.getParameter("unitsConsumed"));
		 *
		 */
		if (customerService.getCustomerByNIC(nic) != null) {
			request.setAttribute("error", "NIC already exists!");
			request.getRequestDispatcher("customerRegister.jsp").forward(request, response);
			return;
		}

		if (customerService.getCustomerByAccount(accountNumber) != null) {
			request.setAttribute("error", "AccountNumber already exists!");
			request.getRequestDispatcher("customerRegister.jsp").forward(request, response);
			return;
		}

		try {

			Customer customer = new Customer(accountNumber, nic, name, address, telephone, email);
			if (customerService.addCustomer(customer)) {
				request.setAttribute("message", "Customer Registration successful! You can now log in.");
				System.out.println("Customer Registration successful! You can now log in");

			} else {
				request.setAttribute("error", "Error occurred during registration.");
				System.out.println("Error occurred during registration.");
			}

			request.getRequestDispatcher("customerRegister.jsp").forward(request, response);
			/* response.sendRedirect("customer?action=list"); */

		} catch (Exception e) {
			// 3️⃣ Database error handling
			request.setAttribute("error", "Database error: " + e.getMessage());
			request.getRequestDispatcher("customerRegister.jsp").forward(request, response);
			System.out.println("Database error: " + e.getMessage());
		}

	}

	private void editCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String accountNumber = request.getParameter("accountNumber");
		Customer existingCustomer = customerService.getCustomerByAccount(accountNumber);
		
		 if (accountNumber != null) {
			 request.setAttribute("customer", existingCustomer);
				request.getRequestDispatcher("updateCustomer.jsp").forward(request, response);
		    } else {
		        request.setAttribute("error", "Customer not found");
		        listCustomers(request, response);
		    }
	}

	private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		String accountNumber = request.getParameter("accountNumber");
		String nic = request.getParameter("nic");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String telephone = request.getParameter("telephone");
		String email = request.getParameter("email");
//		int unitsConsumed = Integer.parseInt(request.getParameter("unitsConsumed"));

		try {

		Customer customer = new Customer(accountNumber, nic, name, address, telephone, email);
		if(customerService.updateCustomer(customer)) {
			request.setAttribute("message", "Customer update successful!");
			System.out.println("Customer update successful!");

		} else {
			request.setAttribute("error", "Error occurred during registration.");
			System.out.println("Error occurred during updating.");
		}

		request.getRequestDispatcher("updateCustomer.jsp").forward(request, response);
		} catch (Exception e) {
			// 3️⃣ Database error handling
			request.setAttribute("error", "Database error: " + e.getMessage());
			request.getRequestDispatcher("updateCustomer.js").forward(request, response);
			System.out.println("Database error: " + e.getMessage());
		}
		/* response.sendRedirect("customer?action=list"); */
	}

	private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    
	    String accountNumber = request.getParameter("accountNumber");

	    if (accountNumber == null || accountNumber.trim().isEmpty()) {
	        request.setAttribute("error", "Invalid account number.");
	    } else {
	    	
	    	try {
	        boolean deleted = customerService.deleteCustomer(accountNumber);
	        if (deleted) {
	            request.setAttribute("message", "Customer deleted successfully!");
	        } else {
	            request.setAttribute("error", "Failed to delete customer. Customer may not exist.");
	        }
	        
	    	   } catch (Exception e) {
			        e.printStackTrace();
			        request.setAttribute("error", "Error deleting user: " + e.getMessage());
			        request.getRequestDispatcher("userReport.jsp").forward(request, response);
			        System.out.println("Error deleting customer: " + e.getMessage());
			    }
	    }

	    // Reload updated customer list
	    List<Customer> customers = customerService.getAllCustomers();
	    request.setAttribute("customers", customers);

	    // Forward to JSP
	    request.getRequestDispatcher("customerReport.jsp").forward(request, response);
	}


}
