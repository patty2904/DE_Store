package Presentation;

import javax.swing.*;

import Application.Controller;
import Application.Product;
import Application.StoreProduct;
import java.awt.*;
import java.util.ArrayList;

public class EmailBoxView extends JFrame {
    private JTextArea textArea;
    
    private Controller cont = new Controller();

    public EmailBoxView() {
        setTitle("Store Manager Email Box");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initialiseComponents();
        // Check for low stock items when the view is initialised
        checkAndDisplayLowStockItems(); 
    }

    private void initialiseComponents() {
        textArea = new JTextArea();
        //Text cant be edited
        textArea.setEditable(false); 
        //Adds test to scroll pane
        JScrollPane scrollPane = new JScrollPane(textArea); 
        add(scrollPane, BorderLayout.CENTER);
    }

    //Display the low stock items
    public void checkAndDisplayLowStockItems() {
    	//Grab the store product data via the server
        ArrayList<StoreProduct> storeProducts = cont.returnStoreProductData();
        //String builder to contain a list of low stock items
        StringBuilder lowStockItems = new StringBuilder("Low Stock Items:\n\n");

        //If an item has less than 6 of it in stock, it has low stock
        //Loop through all items
        for (StoreProduct storeProduct : storeProducts) {
            if (storeProduct.getQuantity() < 6) {
                Product product = null;
				try {
					//Get product via server
					product = cont.getProduct(storeProduct.getProductID());
				} catch (Exception e) {
					e.printStackTrace();
				}
				//Information about low stock items
                lowStockItems.append(String.format("Store ID: %d, Product ID: %d, Name: %s, Current Quantity: %d\n",
                    storeProduct.getStoreID(), storeProduct.getProductID(), product.getProductName(), storeProduct.getQuantity()));
            }
        }

        // Check if there are any low stock items
        if (lowStockItems.length() > "Low Stock Items:\n\n".length()) {
        	//Display text area
            textArea.setText(lowStockItems.toString());
        } else {
        	//If there is nothing running low in stock
            textArea.setText("All items are sufficiently stocked.");
        }
    }

}