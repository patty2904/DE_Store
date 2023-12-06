package Application;

import java.io.Serializable;

//Product class represents the general stock
public class Product implements Serializable{
	private int ProductID;
	private String ProductName;
	private long ProductPrice;
	public ProductOffer offer;
	public enum ProductOffer {
		BUY_ONE_GET_ONE_FREE, 
	    THREE_FOR_TWO,        
	    FREE_DELIVERY,       
	    NONE           
	}
	
	//Constructor
	public Product() {
		
	}
	
	//Parametrised constructor
	 public Product(int productID, String productName, long productPrice, ProductOffer offer) {
	        this.ProductID = productID;
	        this.ProductName = productName;
	        this.ProductPrice = productPrice;
	        this.offer = offer;
	    }
	
	//Getters and setters
	public int getProductID() {
		return ProductID;
	}
	public void setProductID(int productID) {
		ProductID = productID;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public long getProductPrice() {
		return ProductPrice;
	}
	public void setProductPrice(long productPrice) {
		ProductPrice = productPrice;
	}
	
	public ProductOffer getOffer() {
		return offer;
	}
	public void setOffer(ProductOffer offer) {
		this.offer = offer;
	}
}
