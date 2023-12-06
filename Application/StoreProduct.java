package Application;

import java.io.Serializable;

//A class that represents the products that belong to particular stores
//Makes use of store and product
public class StoreProduct implements Serializable {
    private int StoreID;
    private int ProductID;
    private int Quantity;

    //Constructor
    public StoreProduct() {

    }
	//Parametrised constructor
    public StoreProduct(int storeID, int productID, int quantity) {
        this.setStoreID(storeID);
        this.setProductID(productID);
        this.setQuantity(quantity);
    }

    //Getters and setters
    public int getStoreID() {
        return StoreID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}