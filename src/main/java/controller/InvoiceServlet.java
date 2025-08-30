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

//Apache POI for Excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//iText for PDF
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

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
        invoiceService = InvoiceService.getInstance();
        itemService = ItemService.getInstance();
        billService = BillDetailService.getInstance();
        // TODO Auto-generated constructor stub
        invoicePDFGenerator = new InvoicePDFGenerator();
        customerService = CustomerService.getInstance();
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
			case "excel":
				exportExcel( response);
				break;
			case "pdf":
				exportPDF( response);
				break;
			case "insert":
				insertInvoice(request, response);
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
	                ", Customer NIC: " + invoice.getNic() +
	                ", Invoice Date: " + invoice.getInvoiceDate() +
	                ", Due Date: " + invoice.getDueDate() +
	                ", Total Amount: " + invoice.getTotalAmount() +
	                ", Balance: " + invoice.getBalance() +
	                ", Status: " + invoice.getStatus()
	            );
	        }
	        
//	        String roleName = request.getParameter("roleName"); 
//	        System.out.println("roleName: " + roleName);
//	        // Pass roleName to JSP
//	        request.setAttribute("roleName", roleName);

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
	        String customerNic = request.getParameter("nic");
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
	        int totalQty = Integer.parseInt(request.getParameter("totalQty"));
	        double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
	        double cash = Double.parseDouble(request.getParameter("cash"));
	        double balance = Double.parseDouble(request.getParameter("balance"));
	        String status = "Pending";
	        
	        if(totalAmount<=cash) {
	        	status = "Paid";
	        }
	        
	        // Create Invoice using full constructor
	        Invoice invoice = new Invoice(invoiceNo, customerName,customerNic, invoiceDate, dueDate,
	                                      discount, totalQty, totalAmount, cash, balance, status);

	        if (invoiceService.insertInvoice(invoice)) {
	        	int newUnit = 0;
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
	                    newUnit+=qty;
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
	                    
	                    
	                    System.out.println("current item qty "+qty);	
	                    int itemQty=itemService.getQtyByCode(code);
	                    itemQty-=qty;
	                    
	                    System.out.println("new item qty "+itemQty);	
	                   
	                    
	                    if (itemService.updateItemQty(code, itemQty)) {
	                    	
	                    	System.out.println("itemQty updated successfully!");
	                    }else {
	                    	  request.setAttribute("error", "Failed to update itemQty.");
	                    	 System.out.println("Failed to update itemQty.");
	                    }
	                }
	            }
	            
	            String unitsStr = request.getParameter("units"); // match the "name" of your hidden input
	            String accountNumber = request.getParameter("accountNumber"); // match the "name" of your hidden input
	          

	            if (accountNumber == null || accountNumber.isEmpty()) {
	                response.getWriter().write("{\"error\":\"Account number is required\"}");
	                return;
	            }

	            if (unitsStr != null && !unitsStr.isEmpty()) {
	                try {
	                    int units = Integer.parseInt(unitsStr);
	                    units+=newUnit;
	                    System.out.println("units "+units);
	                    boolean updated = customerService.updateUnitsByAccountNumberOrNic(accountNumber, units);

//	                    response.setContentType("application/json");
//	                    response.setCharacterEncoding("UTF-8");

	                    if (updated) {
//	                        response.getWriter().write("{\"success\":\"Units updated successfully\"}");
	                    	 System.out.println("Units updated successfully");
	                    } else {
//	                        response.getWriter().write("{\"error\":\"Update failed or customer not found\"}");
	                    	System.out.println("Update failed or customer not found");
	                    }

	                } catch (NumberFormatException e) {
//	                    response.getWriter().write("{\"error\":\"Invalid units value\"}");
	                	System.out.println("Invalid units value");
	                }
	            } else {
//	                response.getWriter().write("{\"error\":\"Units value is missing\"}");
	            	System.out.println("Units value is missing");
	            }

	            

	            

	            String nextInvoiceNo = invoiceService.generateNextInvoiceNo();
		        request.setAttribute("invoiceNo", nextInvoiceNo);
	            request.setAttribute("message", "Invoice generated successfully!");
	            request.getRequestDispatcher("generateInvoice.jsp").forward(request, response);
//	            response.sendRedirect("InvoiceServlet?action=insert");
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

	            //boolean pdfGenerated = invoicePDFGenerator.generateInvoicePDF(invoice, billList, pdfPath);
	            
	           // String filePath = request.getServletContext().getRealPath("/invoices/invoice.pdf");

	            boolean pdfGenerated = invoicePDFGenerator.generateInvoicePDF(request, invoice, billList, pdfPath);

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
		        int totalQty = Integer.parseInt(request.getParameter("totalQty"));
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
	                            discount,totalQty, totalAmount, cash, balance, status);
		        	

//		            List<BillDetail> billList = new ArrayList<>();
		            List<BillDetail> billList = billService.getBillDetailsByInvoice(invoiceNo);
		           


		            
  
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

		            //boolean pdfGenerated = invoicePDFGenerator.generateInvoicePDF(invoice2, billList, pdfPath);
		            boolean pdfGenerated = invoicePDFGenerator.generateInvoicePDF(request,invoice2, billList, pdfPath);


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
		  
		  

	}

//	private void deleteInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {
//	    String invoiceNo = request.getParameter("invoiceNo");
//
//
//	    if (invoiceNo == null || invoiceNo.trim().isEmpty()) {
//	        request.setAttribute("error", "Invalid invoive number.");
//	    } else {
//	    	
//	    	try {
//	        boolean deleted = invoiceService.deleteInvoice(invoiceNo);
//	        if (deleted) {
//	            request.setAttribute("message", "Invoive deleted successfully!");
//	            
//	            String totalQty = request.getParameter("totalQty");
//	            String nic = request.getParameter("nic");
//	            
//	            int units = Integer.parseInt(totalQty);
//	            
//	            int currentUnits=customerService.getUnitsByNic(nic);
//	            currentUnits-=units;
//	            
//	            boolean updated = customerService.updateUnitsByAccountNumberOrNic(nic, units);
//
////                response.setContentType("application/json");
////                response.setCharacterEncoding("UTF-8");
//
//                if (updated) {
////                    response.getWriter().write("{\"success\":\"Units updated successfully\"}");
//                	 System.out.println("Units updated successfully");
//                } else {
////                    response.getWriter().write("{\"error\":\"Update failed or customer not found\"}");
//                	System.out.println("Update failed or customer not found");
//                } 
//                
//	        } else {
//	            request.setAttribute("error", "Failed to delete invoice. Invoice may not exist.");
//	        }
//	        
//	    	   } catch (Exception e) {
//			        e.printStackTrace();
//			        request.setAttribute("error", "Error deleting invoive: " + e.getMessage());
//			        request.getRequestDispatcher("invoiceReport.jsp").forward(request, response);
//			        System.out.println("Error deleting invoive: " + e.getMessage());
//			    }
//	    }
//
//	    // Reload updated item list
//	    List<Invoice>invoices = invoiceService.getAllInvoices();
//	    request.setAttribute("invoices", invoices);
//
//	    // Forward to JSP
//	    request.getRequestDispatcher("invoiceReport.jsp").forward(request, response);
//	}
	
	private void deleteInvoice(HttpServletRequest request, HttpServletResponse response) 
	        throws IOException, ServletException {

	    String invoiceNo = request.getParameter("invoiceNo");

	    if (invoiceNo == null || invoiceNo.trim().isEmpty()) {
	        request.setAttribute("error", "Invalid invoice number.");
	    } else {
	        try {
	            boolean deleted = invoiceService.deleteInvoice(invoiceNo);
	            if (deleted) {
	                request.setAttribute("message", "Invoice deleted successfully!");

	                String totalQtyStr = request.getParameter("totalQty");
	                String nic = request.getParameter("nic");

	                int units = 0;
	                try {
	                    units = Integer.parseInt(totalQtyStr);
	                } catch (NumberFormatException e) {
	                    units = 0;
	                }

	                int currentUnits = customerService.getUnitsByNic(nic);
	                currentUnits -= units;

	                boolean updated = customerService.updateUnitsByAccountNumberOrNic(nic, currentUnits);

	                if (updated) {
	                    System.out.println("Units updated successfully");
	                } else {
	                    System.out.println("Update failed or customer not found");
	                }

	            } else {
	                request.setAttribute("error", "Failed to delete invoice. Invoice may not exist.");
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            request.setAttribute("error", "Error deleting invoice: " + e.getMessage());
	        }
	    }

	    // Reload updated invoice list
	    List<Invoice> invoices = invoiceService.getAllInvoices();
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
	
	
	 private void exportExcel( HttpServletResponse response) throws IOException {
		 
		 List<Invoice> invoices = invoiceService.getAllInvoices();
		 
	        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	        response.setHeader("Content-Disposition", "attachment; filename=invoices.xlsx");

	        Workbook workbook = new XSSFWorkbook();
	        Sheet sheet = workbook.createSheet("Invoices");
	        
	        // === Title ===
	        Row titleRow = sheet.createRow(1);
	        CellStyle titleStyle = workbook.createCellStyle();
	        Font titleFont = workbook.createFont();
	        titleFont.setBold(true);
	        titleFont.setFontHeightInPoints((short)16);
	        titleStyle.setFont(titleFont);

	        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 9));
	        Cell titleCell = titleRow.createCell(0);
	        titleCell.setCellValue("Invoice Report");
	        titleCell.setCellStyle(titleStyle);

	        CellStyle headerStyle = workbook.createCellStyle();
	        Font headerFont = workbook.createFont();
	        headerFont.setBold(true);                 
	        headerFont.setFontHeightInPoints((short)12); 
	        headerStyle.setFont(headerFont);


	        Row header = sheet.createRow(1);
	        String[] columns = {"Invoice No", "Customer Name", "NIC", "Invoice Date", "Due Date", "Discount", "Qty", "Amount", "Balance", "Status"};
	        
	        for (int i = 0; i < columns.length; i++) {
	            Cell cell = header.createCell(i);  
	            cell.setCellValue(columns[i]);    
	            cell.setCellStyle(headerStyle);    
	        }
//	        for (int i = 0; i < columns.length; i++) {
//	            header.createCell(i).setCellValue(columns[i]);
//	        }

	        int rowIdx = 1;
	        for (Invoice inv : invoices) {
	            Row row = sheet.createRow(rowIdx++);
	            row.createCell(0).setCellValue(inv.getInvoiceNo());
	            row.createCell(1).setCellValue(inv.getCustomerName());
	            row.createCell(2).setCellValue(inv.getNic());
	            row.createCell(3).setCellValue(inv.getInvoiceDate().toString());
	            row.createCell(4).setCellValue(inv.getDueDate().toString());
	            row.createCell(5).setCellValue(inv.getDiscount());
	            row.createCell(6).setCellValue(inv.getTotalQty());
	            row.createCell(7).setCellValue(inv.getTotalAmount());
	            row.createCell(8).setCellValue(inv.getBalance());
	            row.createCell(9).setCellValue(inv.getStatus());
	        }

	        workbook.write(response.getOutputStream());
	        workbook.close();
	    } 

	 private void exportPDF( HttpServletResponse response) throws IOException {
		 
		 List<Invoice> invoices = invoiceService.getAllInvoices();
		 
	        response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition", "attachment; filename=invoices.pdf");

	        try {
	            Document document = new Document();
	            PdfWriter.getInstance(document, response.getOutputStream());
	            document.open();

	            document.add(new Paragraph("Invoice Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
	            document.add(new Paragraph(" ")); // empty line

	            PdfPTable table = new PdfPTable(6);
	            table.setWidthPercentage(100);
	            String[] headers = {"Invoice No", "Customer", "NIC", "Invoice Date", "Due Date", "Discount", "Qty", "Amount", "Balance", "Status"};

	            for (String header : headers) {
	                table.addCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
	            }

	            for (Invoice inv : invoices) {
	                table.addCell(inv.getInvoiceNo());
	                table.addCell(inv.getCustomerName());
	                table.addCell(inv.getNic());
	                table.addCell(inv.getInvoiceDate().toString());
	                table.addCell(inv.getDueDate().toString());
	                table.addCell(String.valueOf(inv.getDiscount()));
	                table.addCell(String.valueOf(inv.getTotalQty()));
	                table.addCell(String.valueOf(inv.getTotalAmount()));
	                table.addCell(String.valueOf(inv.getBalance()));
	                table.addCell(inv.getStatus());
	            }

	            document.add(table);
	            document.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
