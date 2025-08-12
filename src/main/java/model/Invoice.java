package model;
import java.util.Date;

public class Invoice {
	
	private String invoiceNo;
	private Date date;
	private double amount;
	private double balance;
	
	public Invoice(String invoiceNo, Date date, double amount, double balance) {
		super();
		this.invoiceNo = invoiceNo;
		this.date = date;
		this.amount = amount;
		this.balance = balance;
	}

	public Invoice() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
	
	

}
