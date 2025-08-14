
package model;

import java.util.Date;

public class Invoice {

    private String invoiceNo;
    private String customerName;
    private Date invoiceDate;       // invoice date
    private Date dueDate;    // payment due date
    private double totalAmount;   // total invoice amount
    private double balance;  // remaining unpaid balance
    private String status;   // Pending, Paid, Cancelled

    public Invoice() {
        super();
    }

	public Invoice(String invoiceNo, String customerName, Date invoiceDate, Date dueDate, double totalAmount,
			double balance, String status) {
		super();
		this.invoiceNo = invoiceNo;
		this.customerName = customerName;
		this.invoiceDate = invoiceDate;
		this.dueDate = dueDate;
		this.totalAmount = totalAmount;
		this.balance = balance;
		this.status = status;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
}

   