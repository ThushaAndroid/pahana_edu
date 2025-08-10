package model;

public class Customer {
	
    private String accountNumber;
    private String nic;
    private String name;
    private String address;
    private String telephone;
    private String email;
    private int unitsConsumed;
    
    
	public Customer(String accountNumber, String nic, String name, String address, String telephone, String email,
			int unitsConsumed) {
		super();
		this.accountNumber = accountNumber;
		this.nic = nic;
		this.name = name;
		this.address = address;
		this.telephone = telephone;
		this.email = email;
		this.unitsConsumed = unitsConsumed;
	}

	public Customer(String accountNumber, String nic, String name, String address, String telephone, String email) {
		super();
		this.accountNumber = accountNumber;
		this.nic = nic;
		this.name = name;
		this.address = address;
		this.telephone = telephone;
		this.email = email;
	}


	public Customer() {
		
	}


	public String getAccountNumber() {
		return accountNumber;
	}


	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public int getUnitsConsumed() {
		return unitsConsumed;
	}


	public void setUnitsConsumed(int unitsConsumed) {
		this.unitsConsumed = unitsConsumed;
	}

	public String getNic() {
		return nic;
	}

	public void setNic(String nic) {
		this.nic = nic;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	


}
