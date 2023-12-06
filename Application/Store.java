package Application;

import java.io.Serializable;

//Store class represents the different stores
public class Store implements Serializable {
    private int StoreID;
    private String StoreName;

    //Constructor
    public Store() {

    }

	//Parametrised constructor
    public Store(int storeID, String storeName) {
        this.setStoreID(storeID);
        this.setStoreName(storeName);
    }

    //Getters and setters
    public int getStoreID() {
        return StoreID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }
}