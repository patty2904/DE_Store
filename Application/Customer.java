package Application;

import java.io.Serializable;

//Customer class represents the customer, who orders are assigned to

public class Customer implements Serializable{
	private int CustomerID;
	private String CustomerName;
	public CustomerLoyaltyCard loyaltyCard;
	public enum CustomerLoyaltyCard {
		RED,
		BLUE,
		PURPLE,
		NONE
	}
	
	//Constructor
	public Customer() {
		
	}
	
	//Parametrised constructor
	public Customer(int customerID, String customerName,  CustomerLoyaltyCard loyaltyCard) {
	    this.CustomerID = customerID;
	    this.CustomerName = customerName;
	    this.loyaltyCard = loyaltyCard;
	}
	
	//Getters and setters
	public int getCustomerID() {
		return CustomerID;
	}
	public void setCustomerID(int customerID) {
		CustomerID = customerID;
	}
	
	public String getCustomerName() {
		return CustomerName;
	}
	
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	
	
	public CustomerLoyaltyCard getLoyaltyCard() {
        return loyaltyCard;
    }

    public void setLoyaltyCard(CustomerLoyaltyCard loyaltyCard) {
        this.loyaltyCard = loyaltyCard;
    }
}
