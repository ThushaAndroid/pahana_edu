package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Customer;
import model.Invoice;
import model.Item;
import model.User;
import service.CustomerService;
import service.InvoiceService;
import service.ItemService;
import service.UserService;

/**
 * Servlet implementation class InvoiceServlet
 */
@WebServlet("/InvoiceServlet")
public class InvoiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private InvoiceService invoiceService;
	 private ItemService itemService;
	 private CustomerService customerService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InvoiceServlet() {
        super();
        invoiceService = new InvoiceService();
        itemService = new ItemService();
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
			case "invoice":
				generateInvoice(request, response);
				break;
			case "edit":
				editInvoice(request, response);
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
//		doGet(request, response);
		   String action = request.getParameter("action");
	        if (action == null) {
	            action = "insert";
	        }

	        switch (action) {
	            case "update":
	                updateInvoice(request, response);
	                break;
	            case "delete":
	                deleteInvoice(request, response);
	                break;
	            default:
	                insertInvoice(request, response);
	                break;
	           
	    			
	        }
	}
	

	private void listInvoices(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    try {
	        List<Invoice> invoices = invoiceService.getAllInvoices();

	        // Debug print
	        System.out.println("==== Invoice List ====");
	        for (Invoice invoice : invoices) {
	            System.out.println(
	                "InvoiceNo: " + invoice.getInvoiceNo() +
	                ", Customer: " + invoice.getCustomerName() +
	                ", Invoice Date: " + invoice.getInvoiceDate() +
	                ", Due Date: " + invoice.getDueDate() +
	                ", Total Amount: " + invoice.getTotalAmount() +
	                ", Balance: " + invoice.getBalance() +
	                ", Status: " + invoice.getStatus()
	            );
	        }

	        // Set attribute and forward to invoice report
	        request.setAttribute("invoices", invoices);
	        request.getRequestDispatcher("invoiceReport.jsp").forward(request, response);

	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("error", "Error loading invoice data: " + e.getMessage());
	        request.getRequestDispatcher("invoiceReport.jsp").forward(request, response);
	    }
	}
	
	
	private void insertInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	    try {
	        Invoice invoice = new Invoice();
	        invoice.setInvoiceNo(request.getParameter("invoiceNo"));
	        invoice.setCustomerName(request.getParameter("customerName"));
	        invoice.setInvoiceDate(java.sql.Date.valueOf(request.getParameter("invoiceDate")));
	        invoice.setDueDate(java.sql.Date.valueOf(request.getParameter("dueDate")));
	        invoice.setTotalAmount(Double.parseDouble(request.getParameter("totalAmount")));
	        invoice.setBalance(Double.parseDouble(request.getParameter("balance")));
	        invoice.setStatus(request.getParameter("status"));

	        if (invoiceService.insertInvoice(invoice)) {
	            response.sendRedirect("InvoiceServlet?action=list");
	        } else {
	            request.setAttribute("error", "Failed to insert invoice.");
	            request.getRequestDispatcher("invoiceForm.jsp").forward(request, response);
	            
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("error", "Error inserting invoice: " + e.getMessage());
	        request.getRequestDispatcher("invoiceForm.jsp").forward(request, response);
	    }
	}

	private void updateInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	    try {
	        
	        String invoiceNo = request.getParameter("invoiceNo");
	        String customerName = request.getParameter("customerName");
	        java.sql.Date invoiceDate = java.sql.Date.valueOf(request.getParameter("invoiceDate"));
	        java.sql.Date dueDate = java.sql.Date.valueOf(request.getParameter("dueDate"));
	        double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
	        double balance = Double.parseDouble(request.getParameter("balance"));
	        String status = request.getParameter("status");
	        
	        Invoice invoice = new Invoice(invoiceNo,customerName,invoiceDate,dueDate,totalAmount,balance,status);
	        
	        if (invoiceService.updateInvoice(invoice)) {
//	            response.sendRedirect("InvoiceServlet?action=list");
//	        	request.getRequestDispatcher("updateInvoice.jsp").forward(request, response);
	        	 request.setAttribute("message", "Invoice updated successfully!");
	             System.out.println("Invoice updated successfully!");
	        } else {
	            request.setAttribute("error", "Failed to update invoice.");
	            System.out.println("Failed to update invoice.");
	        }
	        
	        request.getRequestDispatcher("updateInvoice.jsp").forward(request, response);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("error", "Error updating invoice: " + e.getMessage());
	        System.out.println("Error updating invoice: " + e.getMessage());
	        request.getRequestDispatcher("updateInvoice.jsp").forward(request, response);
	    }
	}

	private void deleteInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {
	    String invoiceNo = request.getParameter("invoiceNo");
//	    if (invoiceService.deleteInvoice(invoiceNo)) {
//	        response.sendRedirect("InvoiceServlet?action=list");
//	    } else {
//	        response.sendRedirect("InvoiceServlet?action=list&error=Failed to delete invoice");
//	    }
//	    
//	    
	    
	    

	    if (invoiceNo == null || invoiceNo.trim().isEmpty()) {
	        request.setAttribute("error", "Invalid invoive number.");
	    } else {
	    	
	    	try {
	        boolean deleted = invoiceService.deleteInvoice(invoiceNo);
	        if (deleted) {
	            request.setAttribute("message", "Invoive deleted successfully!");
	        } else {
	            request.setAttribute("error", "Failed to delete invoice. Invoice may not exist.");
	        }
	        
	    	   } catch (Exception e) {
			        e.printStackTrace();
			        request.setAttribute("error", "Error deleting invoive: " + e.getMessage());
			        request.getRequestDispatcher("invoiceReport.jsp").forward(request, response);
			        System.out.println("Error deleting invoive: " + e.getMessage());
			    }
	    }

	    // Reload updated item list
	    List<Invoice>invoices = invoiceService.getAllInvoices();
	    request.setAttribute("invoices", invoices);

	    // Forward to JSP
	    request.getRequestDispatcher("invoiceReport.jsp").forward(request, response);
	}

	private void editInvoice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
		String invoiceNo = request.getParameter("invoiceNo");
	    Invoice invoice = invoiceService.getInvoiceByNo(invoiceNo);
	    
		if (invoice != null) {
			 request.setAttribute("invoice", invoice);
				request.getRequestDispatcher("updateInvoice.jsp").forward(request, response);
		    } else {
		        request.setAttribute("error", "Invoice not found");
		        listInvoices(request, response);
		    }
	  } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("error", "Error loading invoice no: " + e.getMessage());
	        request.getRequestDispatcher("updateInvoice.jsp").forward(request, response);
	    }
		
	}	
	private void generateInvoice(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
	    try {
	        // Generate the next invoice number
	        String nextInvoiceNo = invoiceService.generateNextInvoiceNo();

//	        // Get all items and customers
//	        List<Item> items = itemService.getAllItems();
//	        List<Customer> customers = customerService.getAllCustomers();
//
//	        // Validate item list
//	        if (items == null || items.isEmpty()) {
//	            request.setAttribute("error", "No items available to generate an invoice.");
//	            request.getRequestDispatcher("generateInvoice.jsp").forward(request, response);
//	            return;
//	        }
//
//	         //Validate customer list
//	        if (customers == null || customers.isEmpty()) {
//	            request.setAttribute("error", "No customers available to generate an invoice.");
//	            request.getRequestDispatcher("generateInvoice.jsp").forward(request, response);
//	            return;
//	        }
	        if (nextInvoiceNo != null && !nextInvoiceNo.isEmpty()) {
	        // Pass data to JSP
	        request.setAttribute("invoiceNo", nextInvoiceNo);
////	        request.setAttribute("customers", customers);
//	        request.setAttribute("items", items);
	        request.getRequestDispatcher("generateInvoice.jsp").forward(request, response);
	        }else {
		        request.setAttribute("error", "Invoice no not found");
		        request.getRequestDispatcher("generateInvoice.jsp").forward(request, response);
		    }
	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("error", "Error loading invoice data: " + e.getMessage());
	        request.getRequestDispatcher("generateInvoice.jsp").forward(request, response);
	    }
	


	    
	}




}
