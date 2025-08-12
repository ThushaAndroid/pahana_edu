package model;

public class Item {
	
	private String itemCode;
    private String itemName;
    private String description;
    private double price;
    private int quantity;
    
    
	public Item(String itemCode, String itemName, String description, double price, int quantity) {
		super();
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
	}


	public Item() {
		
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
	
	
    
    

}
