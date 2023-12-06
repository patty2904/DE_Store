package Application;

import java.io.Serializable;

//Purchase clas represents the orders made
public class Purchase implements Serializable{
	private int PurchaseID;
	private int PurchaseCustomerID;
	private int PurchaseProductID;
	private int PurchaseStoreID;
	private int PurchaseQuantity;
	private long PurchaseTotal;
	public PurchasePaymentMethod paymentMethod;
	public enum PurchasePaymentMethod{
		CREDIT_CARD,
		DEBIT_CARD,
		FINANCE
	}
	
	//Constructor
	public Purchase() {
		
	}
	
	//Parametrised constructor
	public Purchase(int purchaseID, int purchaseCustomerID, int purchaseProductID, int purchaseStoreID, int purchaseQuantity, long purchaseTotal, PurchasePaymentMethod paymentMethod) {
        this.setPurchaseID(purchaseID);
        this.setPurchaseCustomerID(purchaseCustomerID);
        this.setPurchaseProductID(purchaseProductID);
        this.setPurchaseStoreID(purchaseStoreID);
        this.setPurchaseQuantity(purchaseQuantity);
        this.setPurchaseTotal(purchaseTotal);
        this.setPaymentMethod(paymentMethod);
    }

	//Getters and setters
	public int getPurchaseID() {
		return PurchaseID;
	}

	public void setPurchaseID(int purchaseID) {
		PurchaseID = purchaseID;
	}

	public int getPurchaseCustomerID() {
		return PurchaseCustomerID;
	}

	public void setPurchaseCustomerID(int purchaseCustomerID) {
		PurchaseCustomerID = purchaseCustomerID;
	}

	public int getPurchaseProductID() {
		return PurchaseProductID;
	}

	public void setPurchaseProductID(int purchaseProductID) {
		PurchaseProductID = purchaseProductID;
	}
	
	public int getPurchaseStoreID() {
		return PurchaseStoreID;
	}

	public void setPurchaseStoreID(int purchaseStoreID) {
		PurchaseStoreID = purchaseStoreID;
	}
	

	public int getPurchaseQuantity() {
		return PurchaseQuantity;
	}

	public void setPurchaseQuantity(int purchaseQuantity) {
		PurchaseQuantity = purchaseQuantity;
	}

	public long getPurchaseTotal() {
		return PurchaseTotal;
	}

	public void setPurchaseTotal(long purchaseTotal) {
		PurchaseTotal = purchaseTotal;
	}

	public PurchasePaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PurchasePaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	
}
