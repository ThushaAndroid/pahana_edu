
package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Customer;
import service.CustomerService;

/**
 * Servlet implementation class CustomerServlet
 */
/* @WebServlet("/CustomerServlet") */
public class CustomerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "edit":
                getCustomerForEdit(request, response);
                break;
            case "delete":
                deleteCustomer(request, response);
                break;
            case "list":
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
            case "insert":
            default:
                insertCustomer(request, response);
                break;
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Customer> customers = customerService.getAllCustomers();
        request.setAttribute("customers", customers);
		/*
		 * request.getRequestDispatcher("customerList.jsp").forward(request, response);
		 */
        response.sendRedirect("customerList.jsp");
    }

    private void insertCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	
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
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            return;
        }
        
        if (customerService.getCustomerByAccount(accountNumber) != null) {
            request.setAttribute("error", "AccountNumber already exists!");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            return;
        }
        
        try {

        Customer customer = new Customer(accountNumber,nic, name, address, telephone, email);
        if(customerService.addCustomer(customer)) {
        	request.setAttribute("message", "Customer Registration successful! You can now log in.");
            System.out.println("Customer Registration successful! You can now log in");
            
            
        }else {
        	request.setAttribute("error", "Error occurred during registration.");
            System.out.println("Error occurred during registration.");
        }

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
		/* response.sendRedirect("customer?action=list"); */
        
        }catch (Exception e) {
            // 3️⃣ Database error handling
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            System.out.println("Database error: " + e.getMessage());
        }
        
  
    }

    private void getCustomerForEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");
        Customer existingCustomer = customerService.getCustomerByAccount(accountNumber);
        request.setAttribute("customer", existingCustomer);
        request.getRequestDispatcher("customerForm.jsp").forward(request, response);
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String accountNumber = request.getParameter("accountNumber");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String telephone = request.getParameter("telephone");
        String nic = request.getParameter("nic");
        String email = request.getParameter("email");
        int unitsConsumed = Integer.parseInt(request.getParameter("unitsConsumed"));

        Customer customer = new Customer(accountNumber, name, address, telephone, nic, email);
        customerService.updateCustomer(customer);

        response.sendRedirect("customer?action=list");
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String accountNumber = request.getParameter("accountNumber");
        customerService.deleteCustomer(accountNumber);
        response.sendRedirect("customer?action=list");
    }

}
