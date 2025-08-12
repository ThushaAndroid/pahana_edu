package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Invoice;
import model.User;
import service.InvoiceService;
import service.UserService;

/**
 * Servlet implementation class InvoiceServlet
 */
@WebServlet("/InvoiceServlet")
public class InvoiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private InvoiceService invoiceService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InvoiceServlet() {
        super();
        invoiceService = new InvoiceService();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String action = request.getParameter("action");
	        if (action == null) {
	            action = "list";
	        }

	        switch (action) {
	            case "update":
					/* deleteItem(request, response); */
	                break;
	            default:
	            	listInvoices(request, response);
	                break;
	           
	    			
	        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	private void listInvoices(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		 try {
		        List<Invoice> invoices = invoiceService.getAllInvoices();
		        
		        // Debug print
		        System.out.println("==== Invoice List ====");
		        for (Invoice invoice : invoices) {
		            System.out.println("InvoiceNo: " + invoice.getInvoiceNo() + 
		                             ", Date: " + invoice.getDate() + 
		                             ", Amount: " + invoice.getAmount() + 
		                             ", Balance: " + invoice.getBalance());
		        }
		        
		        // Set attribute and forward
		        request.setAttribute("invoices", invoices);
		        request.getRequestDispatcher("invoiceReport.jsp").forward(request, response);
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		        request.setAttribute("error", "Error loading invoice data: " + e.getMessage());
		        request.getRequestDispatcher("userReport.jsp").forward(request, response);
		    }
		
	}

}
