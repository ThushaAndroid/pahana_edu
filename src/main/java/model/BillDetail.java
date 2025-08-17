package model;

public class BillDetail {
	
	
	    private String invoiceNo;
	    private String itemCode;
	    private String itemName;
	    private String description;
	    private double price;
	    private int quantity;
	    private double total;

	    
	    
	    
	    public BillDetail(String invoiceNo, String itemCode, String itemName, String description, double price,
				int quantity, double total) {
			super();
			this.invoiceNo = invoiceNo;
			this.itemCode = itemCode;
			this.itemName = itemName;
			this.description = description;
			this.price = price;
			this.quantity = quantity;
			this.total = total;
		}
	    
	    
	    
		public BillDetail() {
			super();
			// TODO Auto-generated constructor stub
		}



		public String getInvoiceNo() {
	        return invoiceNo;
	    }
	    public void setInvoiceNo(String invoiceNo) {
	        this.invoiceNo = invoiceNo;
	    }
	    public String getItemCode() {
	        return itemCode;
	    }
	    public void setItemCode(String itemCode) {
	        this.itemCode = itemCode;
	    }
	    public String getItemName() {
	        return itemName;
	    }
	    public void setItemName(String itemName) {
	        this.itemName = itemName;
	    }
	    public String getDescription() {
	        return description;
	    }
	    public void setDescription(String description) {
	        this.description = description;
	    }
	    public double getPrice() {
	        return price;
	    }
	    public void setPrice(double price) {
	        this.price = price;
	    }
	    public int getQuantity() {
	        return quantity;
	    }
	    public void setQuantity(int quantity) {
	        this.quantity = quantity;
	    }
	    public double getTotal() {
	        return total;
	    }
	    public void setTotal(double total) {
	        this.total = total;
	    }

}
