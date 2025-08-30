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
import model.Item;
import service.CustomerService;
import service.ItemService;

/**
 * Servlet implementation class ItemServlet
 */
@WebServlet("/ItemServlet")
public class ItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ItemService itemService;
       
    
    public ItemServlet() {
        super();
        // TODO Auto-generated constructor stub
        itemService=ItemService.getInstance();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*
		 * response.getWriter().append("Served at: ").append(request.getContextPath());
		 */  String action = request.getParameter("action");
	        if (action == null) {
	            action = "list";
	        }

	        switch (action) {
	            case "edit":
					editItem(request, response); 
	                break;
	            case "search":
	    			searchItem(request, response);
	    			break;
	        	case "excel":
					exportExcel( response);
					break;
				case "pdf":
					exportPDF( response);
					break;
	            default:
	            	listItems(request, response);
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
                updateItem(request, response);
                break;
            case "delete":
                deleteItem(request, response);
                break;
            default:
                insertItem(request, response);
                break;
           
    			
        }
    }
	
	private void listItems(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		try {
			List<Item> items = itemService.getAllItems();
	        
	        // Debug print
	        System.out.println("==== Item List ====");
	        for (Item item : items) {
	            System.out.println("Item Code: " + item.getItemCode() + 
	                             ", Item Name: " + item.getItemName() + 
	                             ", Price: " + item.getPrice() + 
	                             ", Quantity: " + item.getQuantity());
	        }
	        
//	        String roleName = request.getParameter("roleName"); 
//	        System.out.println("roleName: " + roleName);
//	        // Pass roleName to JSP
//	        request.setAttribute("roleName", roleName);
	        // Set attribute and forward
	        request.setAttribute("items", items);
	        request.getRequestDispatcher("itemReport.jsp").forward(request, response);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("error", "Error loading item data: " + e.getMessage());
	        request.getRequestDispatcher("itemReport.jsp").forward(request, response);
	    }
	}

    private void insertItem(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String itemCode = request.getParameter("itemCode");
        String itemName = request.getParameter("itemName");
        String description = request.getParameter("description");
        double price = 0.0;
        int quantity = 0;
        try {
        	
    	if (itemService.getItemByName(itemName) != null) {
			request.setAttribute("error", "Item already exists!");
			request.getRequestDispatcher("addItem.jsp").forward(request, response);
			return;
		}

        
        
        try {
            price = Double.parseDouble(request.getParameter("price"));
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid price or quantity format.");
            request.getRequestDispatcher("addItem.jsp").forward(request, response);
            return;
        }

        Item item = new Item(itemCode, itemName, description, price, quantity);
        
         if (itemService.addItem(item)) {
        	 request.setAttribute("message", "Item added successful!");
        	// request.getRequestDispatcher("addItem.jsp").forward(request, response);
			System.out.println("Item added successful!");
			
			 // Reload updated item list
    	    List<Item>items = itemService.getAllItems();
    	    request.setAttribute("items", items);

    	    // Forward to JSP
    	    request.getRequestDispatcher("itemReport.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Failed to add item.");
            request.getRequestDispatcher("addItem.jsp").forward(request, response);
            System.out.println("Item added successful!");
        }
        } catch (Exception e) {
			// 3️⃣ Database error handling
			request.setAttribute("error", "Database error: " + e.getMessage());
			request.getRequestDispatcher("addItem.jsp").forward(request, response);
			System.out.println("Database error: " + e.getMessage());
		}
    }
    
	private void editItem(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {
		String itemCode = request.getParameter("itemCode");
		Item item = itemService.getItemByCode(itemCode);
		
		 if (item != null) {
			 request.setAttribute("item", item);
				request.getRequestDispatcher("updateItem.jsp").forward(request, response);
		    } else {
		        request.setAttribute("error", "Item not found");
		        listItems(request, response);
		    }
	 } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("error", "Error loading item code: " + e.getMessage());
	        request.getRequestDispatcher("updateItem.jsp").forward(request, response);
	    }
	}

    private void updateItem(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String itemCode = request.getParameter("itemCode");
        String itemName = request.getParameter("itemName");
        String description = request.getParameter("description");
        double price = 0.0;
        int quantity = 0;

        try {
            price = Double.parseDouble(request.getParameter("price"));
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid price or quantity format.");
            request.getRequestDispatcher("updateItem.jsp").forward(request, response);
            System.out.println("Invalid price or quantity format.");
            return;
        }

        Item item = new Item(itemCode, itemName, description, price, quantity);
        try {
        if (itemService.updateItem(item)) {
            request.setAttribute("message", "Item updated successfully!");
            System.out.println("Item updated successfully!");
            
            // Reload updated item list
    	    List<Item>items = itemService.getAllItems();
    	    request.setAttribute("items", items);

    	    // Forward to JSP
    	    request.getRequestDispatcher("itemReport.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Failed to update item.");
            System.out.println("Failed to update item.");
            request.getRequestDispatcher("updateItem.jsp").forward(request, response);
        }
        
        //request.getRequestDispatcher("updateItem.jsp").forward(request, response);
        
    } catch (Exception e) {
		// 3️⃣ Database error handling
		request.setAttribute("error", "Database error: " + e.getMessage());
		request.getRequestDispatcher("updateItem.jsp").forward(request, response);
		System.out.println("Database error: " + e.getMessage());
	}
        
     
    }
    
    private void deleteItem(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    
	    String itemCode = request.getParameter("itemCode");

	    if (itemCode == null || itemCode.trim().isEmpty()) {
	        request.setAttribute("error", "Invalid account number.");
	    } else {
	    	
	    	try {
	        boolean deleted = itemService.deleteItem(itemCode);
	        if (deleted) {
	            request.setAttribute("message", "Item deleted successfully!");
	        } else {
	            request.setAttribute("error", "Failed to delete item. Item may not exist.");
	        }
	        
	    	   } catch (Exception e) {
			        e.printStackTrace();
			        request.setAttribute("error", "Error deleting item: " + e.getMessage());
			        request.getRequestDispatcher("itemReport.jsp").forward(request, response);
			        System.out.println("Error deleting itemr: " + e.getMessage());
			    }
	    }

	    // Reload updated item list
	    List<Item>items = itemService.getAllItems();
	    request.setAttribute("items", items);

	    // Forward to JSP
	    request.getRequestDispatcher("itemReport.jsp").forward(request, response);
	}
    
	private void searchItem(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    try {
	        String query = request.getParameter("query");
	        List<Item> items = itemService.searchItems(query);

	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");

	        // Convert list to JSON
	        String json = new Gson().toJson(items);
	        response.getWriter().write(json);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write("[]"); // return empty array if error
	    }
	}
	
 private void exportExcel( HttpServletResponse response) throws IOException {
		 
		 List<Item> items = itemService.getAllItems();
		 
	        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	        response.setHeader("Content-Disposition", "attachment; filename=items.xlsx");

	        Workbook workbook = new XSSFWorkbook();
	        Sheet sheet = workbook.createSheet("Items");
	        
	        // === Title ===
	        Row titleRow = sheet.createRow(0);
	        CellStyle titleStyle = workbook.createCellStyle();
	        Font titleFont = workbook.createFont();
	        titleFont.setBold(true);
	        titleFont.setFontHeightInPoints((short)16);
	        titleStyle.setFont(titleFont);

	        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 9));
	        Cell titleCell = titleRow.createCell(0);
	        titleCell.setCellValue("Item Report");
	        titleCell.setCellStyle(titleStyle);

	        CellStyle headerStyle = workbook.createCellStyle();
	        Font headerFont = workbook.createFont();
	        headerFont.setBold(true);                 
	        headerFont.setFontHeightInPoints((short)12); 
	        headerStyle.setFont(headerFont);

	        Row header = sheet.createRow(1);
	        String[] columns = {"Item Code", "Item Name", "Description", "price", "Quantity"};
	        
	        for (int i = 0; i < columns.length; i++) {
	            Cell cell = header.createCell(i);  
	            cell.setCellValue(columns[i]);    
	            cell.setCellStyle(headerStyle);    
	        }
	        
//	        for (int i = 0; i < columns.length; i++) {
//	            header.createCell(i).setCellValue(columns[i]);
//	           
//	        }

	        int rowIdx = 2;
	        for (Item itm : items) {
	            Row row = sheet.createRow(rowIdx++);
	            row.createCell(0).setCellValue(itm.getItemCode());
	            row.createCell(1).setCellValue(itm.getItemName());
	            row.createCell(2).setCellValue(itm.getDescription());
	            row.createCell(3).setCellValue(itm.getPrice());
	            row.createCell(4).setCellValue(itm.getQuantity());
	       
	        }

	        workbook.write(response.getOutputStream());
	        workbook.close();
	    } 

	 private void exportPDF( HttpServletResponse response) throws IOException {
		 
		 List<Item> items = itemService.getAllItems();
		 
	        response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition", "attachment; filename=items.pdf");

	        try {
	            Document document = new Document();
	            PdfWriter.getInstance(document, response.getOutputStream());
	            document.open();

	            document.add(new Paragraph("Item Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
	            document.add(new Paragraph(" ")); // empty line

	            PdfPTable table = new PdfPTable(5);
	            table.setWidthPercentage(100);
	            String[] headers = {"Item Code", "Item Name", "Description", "price", "Quantity"};

	            for (String header : headers) {
	                table.addCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
	            }

	            for (Item itm : items) {
	                table.addCell(itm.getItemCode());
	                table.addCell(itm.getItemName());
	                table.addCell(itm.getDescription());
	                table.addCell(String.valueOf(itm.getPrice()));
	                table.addCell(String.valueOf(itm.getQuantity()));
	             
	            }

	            document.add(table);
	            document.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}
