package controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BillDetail;
import model.Customer;
import model.Invoice;
import model.Item;
import model.User;
import print.InvoicePDFGenerator;
import service.BillDetailService;
import service.CustomerService;
import service.InvoiceService;
import service.ItemService;
import service.UserService;

import java.io.File;

/**
 * Servlet implementation class InvoiceServlet
 */
@WebServlet("/InvoiceServlet")
public class InvoiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private InvoiceService invoiceService;
	 private ItemService itemService;
	 private CustomerService customerService;
	 private BillDetailService billService;
	 private InvoicePDFGenerator invoicePDFGenerator;
	

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InvoiceServlet() {
        super();
        invoiceService = new InvoiceService();
        itemService = new ItemService();
        billService = new BillDetailService();
        // TODO Auto-generated constructor stub
        invoicePDFGenerator = new InvoicePDFGenerator();
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
	
	
	private void insertInvoice(HttpServletRequest request, HttpServletResponse response) 
	        throws IOException, ServletException {
		
		  
	    try {
	        // Load all parameters from request
	        String invoiceNo = request.getParameter("invoiceNo");
	        String customerName = request.getParameter("customerName");
	        Date invoiceDate = java.sql.Date.valueOf(request.getParameter("invoiceDate"));
//	        Date dueDate = java.sql.Date.valueOf(request.getParameter("dueDate"));
	        String dueDateStr = request.getParameter("dueDate");
	        java.sql.Date dueDate;

	        if (dueDateStr != null && !dueDateStr.isEmpty()) {
	            dueDate = java.sql.Date.valueOf(dueDateStr); // format must be "yyyy-MM-dd"
	        } else {
	            dueDate = new java.sql.Date(System.currentTimeMillis()); // use current date if null/empty
	        }

	        double discount = Double.parseDouble(request.getParameter("discount"));
	        double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
	        double cash = Double.parseDouble(request.getParameter("cash"));
	        double balance = Double.parseDouble(request.getParameter("balance"));
	        String status = "Pending";
	        
	        if(totalAmount<=cash) {
	        	status = "Paid";
	        }
	       
	      
	        
	        // Create Invoice using full constructor
	        Invoice invoice = new Invoice(invoiceNo, customerName, invoiceDate, dueDate,
	                                      discount, totalAmount, cash, balance, status);

	        if (invoiceService.insertInvoice(invoice)) {
	        	
	        	  // 3. Collect item details arrays from request
	            String[] itemCodes = request.getParameterValues("item_code[]");
	            String[] itemNames = request.getParameterValues("item_name[]");
	            String[] descriptions = request.getParameterValues("description[]");
	            String[] prices = request.getParameterValues("price[]");
	            String[] quantities = request.getParameterValues("quantity[]");
	            
	            List<BillDetail> billList = new ArrayList<>();

	            if (itemCodes != null) {
	                for (int i = 0; i < itemCodes.length; i++) {
	                    String code = itemCodes[i];
	                    String name = itemNames[i];
	                    String desc = descriptions[i];
	                    double price = Double.parseDouble(prices[i]);
	                    int qty = Integer.parseInt(quantities[i]);
	                    double total = price * qty;

	                    // 4. Save into bill_details table
	                    BillDetail bill = new BillDetail(invoice.getInvoiceNo(),code,name,desc,price,qty,total);
	                   

	                    
	                    if (billService.insertBillDetail(bill)) {
	                        request.setAttribute("message", "Bill items saved successfully!");
	                        System.out.println("Bill items saved successfully!");
	                    } else {
	                        request.setAttribute("error", "Failed to save bill items.");
	                        System.out.println("Failed to save bill items.");
	                    }
	                    
	                    billList.add(bill);
	                }
	            }
	        	
	            

	            String nextInvoiceNo = invoiceService.generateNextInvoiceNo();
		        request.setAttribute("invoiceNo", nextInvoiceNo);
	            request.setAttribute("message", "Invoice generated successfully!");
	            request.getRequestDispatcher("generateInvoice.jsp").forward(request, response);
	            System.out.println("Invoice generated successfully!");
	            
//	            String pdfPath = "D:\\invoices" + invoice.getInvoiceNo() + ".pdf";
	            
	         // Define the directory where PDFs will be saved
	            String pdfDirPath = "D:\\invoices";
	            File pdfDir = new File(pdfDirPath);

	            // Create the directory if it doesn't exist
	            if (!pdfDir.exists()) {
	                if (pdfDir.mkdirs()) {
	                    System.out.println("Directory created: " + pdfDirPath);
	                } else {
	                    System.out.println("Failed to create directory: " + pdfDirPath);
	                }
	            }

	            // Define the full PDF file path
	            String pdfPath = new File(pdfDir, invoice.getInvoiceNo() + ".pdf").getAbsolutePath();

	            boolean pdfGenerated = invoicePDFGenerator.generateInvoicePDF(invoice, billList, pdfPath);

				File pdfFile = new File(pdfPath);

				if (pdfGenerated && pdfFile.exists()) {
					System.out.println("Invoice PDF created successfully: " + pdfPath);
					request.setAttribute("pdfPath", pdfPath);
				} else {
					System.out.println("Failed to create Invoice PDF or file missing!");
					request.setAttribute("error", "Invoice saved but PDF was not generated.");
				}
            
//	            insertBillInformation(request, response);
	            
	        } else {
	        	  String nextInvoiceNo = invoiceService.generateNextInvoiceNo();
	        	request.setAttribute("invoiceNo", nextInvoiceNo);
	            request.setAttribute("error", "Failed to save invoice.");
	            request.getRequestDispatcher("generateInvoice.jsp").forward(request, response);
	            System.out.println("Failed to insert invoice.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        String nextInvoiceNo = invoiceService.generateNextInvoiceNo();
	        request.setAttribute("invoiceNo", nextInvoiceNo);
	        request.setAttribute("error", "Error inserting invoice: " + e.getMessage());
	        request.getRequestDispatcher("generateInvoice.jsp").forward(request, response);
	        System.out.println("Error inserting invoice: " + e.getMessage());
	    }
	}

	private void updateInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		  try {
		        // Load all parameters from request
		        String invoiceNo = request.getParameter("invoiceNo");
		        String customerName = request.getParameter("customerName");
		        Date invoiceDate = java.sql.Date.valueOf(request.getParameter("invoiceDate"));
//		        Date dueDate = java.sql.Date.valueOf(request.getParameter("dueDate"));
		        String dueDateStr = request.getParameter("dueDate");
		        java.sql.Date dueDate;

		        if (dueDateStr != null && !dueDateStr.isEmpty()) {
		            dueDate = java.sql.Date.valueOf(dueDateStr); // format must be "yyyy-MM-dd"
		        } else {
		            dueDate = new java.sql.Date(System.currentTimeMillis()); // use current date if null/empty
		        }

		        double discount = Double.parseDouble(request.getParameter("discount"));
		        double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
		        double cash = Double.parseDouble(request.getParameter("cash"));
		        double balance = Double.parseDouble(request.getParameter("balance"));
		        String status = "Pending";
		        
		        if(balance>=0) {
		        	status = "Paid";
		        }
		       
		      
		        
		        // Create Invoice using full constructor
		        Invoice invoice = new Invoice(invoiceNo,  dueDate, cash, balance, status);

		        if (invoiceService.updateInvoice(invoice)) {
		        	 Invoice invoice2 = new Invoice(invoiceNo, customerName, invoiceDate, dueDate,
	                            discount, totalAmount, cash, balance, status);
		        	
		        	  // 3. Collect item details arrays from request
//		            String[] itemCodes = request.getParameterValues("item_code[]");
//		            String[] itemNames = request.getParameterValues("item_name[]");
//		            String[] descriptions = request.getParameterValues("description[]");
//		            String[] prices = request.getParameterValues("price[]");
//		            String[] quantities = request.getParameterValues("quantity[]");
		            
//		            List<BillDetail> billList = new ArrayList<>();
		            List<BillDetail> billList = billService.getBillDetailsByInvoice(invoiceNo);
		           


//		            if (itemCodes != null) {
//		                for (int i = 0; i < itemCodes.length; i++) {
//		                    String code = itemCodes[i];
//		                    String name = itemNames[i];
//		                    String desc = descriptions[i];
//		                    double price = Double.parseDouble(prices[i]);
//		                    int qty = Integer.parseInt(quantities[i]);
//		                    double total = price * qty;
//
//		                    // 4. Save into bill_details table
//		                    BillDetail bill = new BillDetail(invoice.getInvoiceNo(),code,name,desc,price,qty,total);
//		                   
//		                    
//		                    if (billService.insertBillDetail(bill)) {
//		                        request.setAttribute("message", "Bill items saved successfully!");
//		                        System.out.println("Bill items saved successfully!");
//		                    } else {
//		                        request.setAttribute("error", "Failed to save bill items.");
//		                        System.out.println("Failed to save bill items.");
//		                    }
//		                    
//		                    billList.add(bill);
//		                }
//		            }
		            
  
			        request.setAttribute("invoiceNo", invoiceNo);
		            request.setAttribute("message", "Invoice updated successfully!");
		            request.getRequestDispatcher("updateInvoice.jsp").forward(request, response);
		            System.out.println("Invoice updated successfully!");
		            
//		            String pdfPath = "D:\\invoices" + invoice.getInvoiceNo() + ".pdf";
		            
		         // Define the directory where PDFs will be saved
		            String pdfDirPath = "D:\\invoices";
		            File pdfDir = new File(pdfDirPath);

		            // Create the directory if it doesn't exist
		            if (!pdfDir.exists()) {
		                if (pdfDir.mkdirs()) {
		                    System.out.println("Directory created: " + pdfDirPath);
		                } else {
		                    System.out.println("Failed to create directory: " + pdfDirPath);
		                }
		            }

		            // Define the full PDF file path
		            String pdfPath = new File(pdfDir, invoice.getInvoiceNo() + ".pdf").getAbsolutePath();

		            boolean pdfGenerated = invoicePDFGenerator.generateInvoicePDF(invoice2, billList, pdfPath);

					File pdfFile = new File(pdfPath);

					if (pdfGenerated && pdfFile.exists()) {
						System.out.println("Invoice PDF created successfully: " + pdfPath);
						request.setAttribute("pdfPath", pdfPath);
					} else {
						System.out.println("Failed to create Invoice PDF or file missing!");
						request.setAttribute("error", "Invoice saved but PDF was not generated.");
					}
	            
//		            insertBillInformation(request, response);
		            
		        } else {
		        	  
		        	request.setAttribute("invoiceNo", invoiceNo);
		            request.setAttribute("error", "Failed to save invoice.");
		            request.getRequestDispatcher("updateInvoice.jsp").forward(request, response);
		            System.out.println("Failed to updating invoice.");
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        String invoiceNo = request.getParameter("invoiceNo");
		        request.setAttribute("invoiceNo", invoiceNo);
		        request.setAttribute("error", "Error updating invoice: " + e.getMessage());
		        request.getRequestDispatcher("updateInvoice.jsp").forward(request, response);
		        System.out.println("Error updating invoice: " + e.getMessage());
		    }
		  
		  
//	    try {
//	        
//	    	 String invoiceNo = request.getParameter("invoiceNo");
//		        String customerName = request.getParameter("customerName");
//		        Date invoiceDate = java.sql.Date.valueOf(request.getParameter("invoiceDate"));
//		        Date dueDate = java.sql.Date.valueOf(request.getParameter("dueDate"));
//		        double discount = Double.parseDouble(request.getParameter("discount"));
//		        double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
//		        double cash = Double.parseDouble(request.getParameter("cash"));
//		        double balance = Double.parseDouble(request.getParameter("balance"));
//		        String status = request.getParameter("status");
//	        
//		        Invoice invoice = new Invoice(invoiceNo, customerName, invoiceDate, dueDate,
//                        discount, totalAmount, cash, balance, status);
//	        
//	        if (invoiceService.updateInvoice(invoice)) {
////	            response.sendRedirect("InvoiceServlet?action=list");
////	        	request.getRequestDispatcher("updateInvoice.jsp").forward(request, response);
//	        	 request.setAttribute("message", "Invoice updated successfully!");
//	             System.out.println("Invoice updated successfully!");
//	        } else {
//	            request.setAttribute("error", "Failed to update invoice.");
//	            System.out.println("Failed to update invoice.");
//	        }
//	        
//	        request.getRequestDispatcher("updateInvoice.jsp").forward(request, response);
//	        
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        request.setAttribute("error", "Error updating invoice: " + e.getMessage());
//	        System.out.println("Error updating invoice: " + e.getMessage());
//	        request.getRequestDispatcher("updateInvoice.jsp").forward(request, response);
//	    }
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
	    
	    List<BillDetail> billList = billService.getBillDetailsByInvoice(invoiceNo);
        if (billList == null) {
            billList = new ArrayList<>(); // ensure it's not null
        }

	    
		if (invoice != null) {
			 request.setAttribute("invoice", invoice);
			 request.setAttribute("bill_details", billList);
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
	
	
	    
//	    private void insertBillInformation(HttpServletRequest request, HttpServletResponse response) 
//		        throws ServletException, IOException {
//		    try {
//		    	
//		    	  // 3. Collect item details arrays from request
//	            String[] itemCodes = request.getParameterValues("item_code[]");
//	            String[] itemNames = request.getParameterValues("item_name[]");
//	            String[] descriptions = request.getParameterValues("description[]");
//	            String[] prices = request.getParameterValues("price[]");
//	            String[] quantities = request.getParameterValues("quantity[]");
//
//	            if (itemCodes != null) {
//	                for (int i = 0; i < itemCodes.length; i++) {
//	                    String code = itemCodes[i];
//	                    String name = itemNames[i];
//	                    String desc = descriptions[i];
//	                    double price = Double.parseDouble(prices[i]);
//	                    int qty = Integer.parseInt(quantities[i]);
//	                    double total = price * qty;
//
//	                    // 4. Save into bill_details table
//	                    BillDetail bill = new BillDetail();
//	                    bill.setInvoiceNo(invoice.getInvoiceNo());
//	                    bill.setItemCode(code);
//	                    bill.setItemName(name);
//	                    bill.setDescription(desc);
//	                    bill.setPrice(price);
//	                    bill.setQuantity(qty);
//	                    bill.setTotal(total);
//
//	                    billService.insertBillDetail(bill);
//	                }
//	            }
//	
//		    } catch (Exception e) {
//		        e.printStackTrace();
//		        request.setAttribute("error", "Error loading invoice data: " + e.getMessage());
//		        request.getRequestDispatcher("generateInvoice.jsp").forward(request, response);
//		    }
//	    }

	    
	




}
