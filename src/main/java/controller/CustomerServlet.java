
package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.Customer;
import model.Invoice;
import model.User;
import service.CustomerService;
import service.LoginService;

/**
 * Servlet implementation class CustomerServlet
 */
@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private CustomerService customerService;
	
	public void init() throws ServletException {
		customerService = CustomerService.getInstance();
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if (action == null) {
			action = "list";
		}

		switch (action) {
		case "view":
			viewCustomer(request, response);
			break;
		case "edit":
			editCustomer(request, response);
			break;
		case "search":
			searchCustomer(request, response);
			break;
		case "getUnits":
			getCustomerUnits(request, response);
			break;
		case "excel":
			exportExcel( response);
			break;
		case "pdf":
			exportPDF( response);
			break;
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
	        
	        String roleName = request.getParameter("roleName"); 
	        System.out.println("roleName: " + roleName);
	        // Pass roleName to JSP
	        request.setAttribute("roleName", roleName);
	        
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
				
				// Reload updated customer list
			    List<Customer> customers = customerService.getAllCustomers();
			    request.setAttribute("customers", customers);

			    // Forward to JSP
			    request.getRequestDispatcher("customerReport.jsp").forward(request, response);


			} else {
				request.setAttribute("error", "Error occurred during registration.");
				System.out.println("Error occurred during registration.");
				request.getRequestDispatcher("customerRegister.jsp").forward(request, response);
			}

			
			// request.getRequestDispatcher("customerRegister.jsp").forward(request, response);
			 
			/* response.sendRedirect("customer?action=list"); */

		} catch (Exception e) {
			// 3️⃣ Database error handling
			request.setAttribute("error", "Database error: " + e.getMessage());
			request.getRequestDispatcher("customerRegister.jsp").forward(request, response);
			System.out.println("Database error: " + e.getMessage());
		}

	}
	
	private void viewCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
		String accountNumber = request.getParameter("accountNumber");
		Customer existingCustomer = customerService.getCustomerByAccount(accountNumber);
		
		 if (accountNumber != null) {
			 request.setAttribute("customer", existingCustomer);
				request.getRequestDispatcher("viewCustomer.jsp").forward(request, response);
		    } else {
		        request.setAttribute("error", "Customer not found");
		        listCustomers(request, response);
		    }
	 } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("error", "Error loading account number: " + e.getMessage());
	        request.getRequestDispatcher("viewCustomer.jsp").forward(request, response);
	    }
	}

	private void editCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
		String accountNumber = request.getParameter("accountNumber");
		Customer existingCustomer = customerService.getCustomerByAccount(accountNumber);
		
		 if (accountNumber != null) {
			 request.setAttribute("customer", existingCustomer);
				request.getRequestDispatcher("updateCustomer.jsp").forward(request, response);
		    } else {
		        request.setAttribute("error", "Customer not found");
		        listCustomers(request, response);
		    }
	 } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("error", "Error loading account number: " + e.getMessage());
	        request.getRequestDispatcher("updateCustomer.jsp").forward(request, response);
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
			
			// Reload updated customer list
		    List<Customer> customers = customerService.getAllCustomers();
		    request.setAttribute("customers", customers);

		    // Forward to JSP
		    request.getRequestDispatcher("customerReport.jsp").forward(request, response);

		} else {
			request.setAttribute("error", "Error occurred during registration.");
			System.out.println("Error occurred during updating.");
			request.getRequestDispatcher("updateCustomer.js").forward(request, response);
		}

		//request.getRequestDispatcher("updateCustomer.jsp").forward(request, response);
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
	
	
	private void searchCustomer(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    try {
	        String query = request.getParameter("query");
	        List<Customer> customers = customerService.searchCustomers(query);

	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");

	        // Convert list to JSON
	        String json = new Gson().toJson(customers);
	        response.getWriter().write(json);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write("[]"); // return empty array if error
	    }
	}
	
	
	private void getCustomerUnits(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	 String accountNumber = request.getParameter("accountNumber");
     CustomerService service = new CustomerService();
     Integer units = service.getUnitsByAccountNumber(accountNumber);

     response.setContentType("application/json");
     response.setCharacterEncoding("UTF-8");

     if (units != null) {
         response.getWriter().write("{\"units_consumed\":" + units + "}");
     } else {
         response.getWriter().write("{\"error\":\"Customer not found\"}");
     }
	}

	 private void exportExcel( HttpServletResponse response) throws IOException {
		 
		 List<Customer> customers = customerService.getAllCustomers();
		 
	        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	        response.setHeader("Content-Disposition", "attachment; filename=customers.xlsx");

	        Workbook workbook = new XSSFWorkbook();
	        Sheet sheet = workbook.createSheet("Customers");
	        
	        // === Title ===
	        Row titleRow = sheet.createRow(0);
	        CellStyle titleStyle = workbook.createCellStyle();
	        Font titleFont = workbook.createFont();
	        titleFont.setBold(true);
	        titleFont.setFontHeightInPoints((short)16);
	        titleStyle.setFont(titleFont);

	        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 9));
	        Cell titleCell = titleRow.createCell(0);
	        titleCell.setCellValue("Customer Report");
	        titleCell.setCellStyle(titleStyle);

	        CellStyle headerStyle = workbook.createCellStyle();
	        Font headerFont = workbook.createFont();
	        headerFont.setBold(true);                 
	        headerFont.setFontHeightInPoints((short)12); 
	        headerStyle.setFont(headerFont);

	        Row header = sheet.createRow(1);
	        String[] columns = {"Account Number","NIC", "Name", "Telephone", "Email", "Units Consumed"};
	        
	        for (int i = 0; i < columns.length; i++) {
	            Cell cell = header.createCell(i);  
	            cell.setCellValue(columns[i]);    
	            cell.setCellStyle(headerStyle);    
	        }
//	        for (int i = 0; i < columns.length; i++) {
//	            header.createCell(i).setCellValue(columns[i]);
//	        }

	        int rowIdx = 2;
	        for (Customer cus : customers) {
	            Row row = sheet.createRow(rowIdx++);
	            row.createCell(0).setCellValue(cus.getAccountNumber());
	            row.createCell(1).setCellValue(cus.getNic());
	            row.createCell(2).setCellValue(cus.getName());
	            row.createCell(3).setCellValue(cus.getTelephone());
	            row.createCell(4).setCellValue(cus.getEmail());
	            row.createCell(5).setCellValue(cus.getUnitsConsumed());
	         
	        }

	        workbook.write(response.getOutputStream());
	        workbook.close();
	    } 

	 private void exportPDF( HttpServletResponse response) throws IOException {
		 
		 List<Customer> customers = customerService.getAllCustomers();
		 
	        response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition", "attachment; filename=customers.pdf");

	        try {
	            Document document = new Document();
	            PdfWriter.getInstance(document, response.getOutputStream());
	            document.open();

	            document.add(new Paragraph("Customer Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
	            document.add(new Paragraph(" ")); // empty line

	            PdfPTable table = new PdfPTable(6);
	            table.setWidthPercentage(100);
	            String[] headers = {"Account Number","NIC", "Name", "Telephone", "Email", "Units Consumed"};

	            for (String header : headers) {
	                table.addCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
	            }

	            for (Customer cus : customers) {
	                table.addCell(cus.getAccountNumber());
	                table.addCell(cus.getNic());
	                table.addCell(cus.getName());
	                table.addCell(cus.getTelephone());
	                table.addCell(cus.getEmail());
	                table.addCell(String.valueOf(cus.getUnitsConsumed()));
	            }

	            document.add(table);
	            document.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
