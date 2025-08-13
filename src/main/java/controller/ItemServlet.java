package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Customer;
import model.Item;
import service.CustomerService;
import service.ItemService;

/**
 * Servlet implementation class ItemServlet
 */
/* @WebServlet("/ItemServlet") */
public class ItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ItemService itemService = new ItemService();
       
    
    public ItemServlet() {
        super();
        // TODO Auto-generated constructor stub
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
//                deleteItem(request, response);
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
        	 request.getRequestDispatcher("addItem.jsp").forward(request, response);
			System.out.println("Item added successful!");
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
		String itemCode = request.getParameter("itemCode");
		Item item = itemService.getItemByCode(itemCode);
		
		 if (item != null) {
			 request.setAttribute("item", item);
				request.getRequestDispatcher("updateItem.jsp").forward(request, response);
		    } else {
		        request.setAttribute("error", "Item not found");
		        listItems(request, response);
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
            request.getRequestDispatcher("addItem.jsp").forward(request, response);
            return;
        }

        Item item = new Item(itemCode, itemName, description, price, quantity);
        try {
        if (itemService.updateItem(item)) {
            request.setAttribute("message", "Item updated successfully!");
        } else {
            request.setAttribute("error", "Failed to update item.");
        }
        
        request.getRequestDispatcher("updateItem.jsp").forward(request, response);
        
    } catch (Exception e) {
		// 3️⃣ Database error handling
		request.setAttribute("error", "Database error: " + e.getMessage());
		request.getRequestDispatcher("addItem.jsp").forward(request, response);
		System.out.println("Database error: " + e.getMessage());
	}
    }
    
//    private void deleteItem(HttpServletRequest request, HttpServletResponse response)
//	        throws ServletException, IOException {
//	    
//	    String accountNumber = request.getParameter("accountNumber");
//
//	    if (accountNumber == null || accountNumber.trim().isEmpty()) {
//	        request.setAttribute("error", "Invalid account number.");
//	    } else {
//	    	
//	    	try {
//	        boolean deleted = itemService.deleteCustomer(accountNumber);
//	        if (deleted) {
//	            request.setAttribute("message", "Customer deleted successfully!");
//	        } else {
//	            request.setAttribute("error", "Failed to delete customer. Customer may not exist.");
//	        }
//	        
//	    	   } catch (Exception e) {
//			        e.printStackTrace();
//			        request.setAttribute("error", "Error deleting user: " + e.getMessage());
//			        request.getRequestDispatcher("userReport.jsp").forward(request, response);
//			        System.out.println("Error deleting customer: " + e.getMessage());
//			    }
//	    }
//
//	    // Reload updated customer list
//	    List<Item> customers = itemService.getAllCustomers();
//	    request.setAttribute("customers", customers);
//
//	    // Forward to JSP
//	    request.getRequestDispatcher("customerReport.jsp").forward(request, response);
//	}

}
