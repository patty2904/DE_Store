package Application;

import java.io.IOException;
import java.util.*;

import Application.Purchase.PurchasePaymentMethod;
import Presentation.CustomerView;
import Presentation.FinanceView;
import Presentation.MainView;
import Presentation.ProductView;
import Presentation.PurchaseView;
import Presentation.StoreProductView;
import Presentation.StoreView;

//Client Controller for MVC in client portion of application.
public class Controller {

	static DE_STORE_Client client = new DE_STORE_Client();

	private static Object request;

	public static void main(String[] args) {

		// Display the main view
		MainView mainView = new MainView();
		mainView.setVisible(true);

	}

	// The following functions are called in the presentation layer
	// Navigates to a list of products
	public void navigateToProductView() {
		ProductView productView = new ProductView();
		productView.setVisible(true);
	}

	// Navigates to a list of customers
	public void navigateToCustomerView() {
		CustomerView customerView = new CustomerView();
		customerView.setVisible(true);
	}

	// Navigates to a list of finance
	public void navigateToFinanceView() {
		FinanceView financeView = new FinanceView();
		financeView.setVisible(true);
	}

	// Navigates to a list of orders (purchase)
	public void navigateToPurchaseView() {
		PurchaseView purchaseView = new PurchaseView();
		purchaseView.setVisible(true);
	}

	// Navigates to a list of stores
	public void navigateToStoreView() {
		StoreView storeView = new StoreView();
		storeView.setVisible(true);
	}

	// Navigates to a list of store products
	public void navigateToStoreProductView() {
		StoreProductView storeProductView = new StoreProductView();
		storeProductView.setVisible(true);
		storeProductView.checkForLowStock();
	}

	// Request the customer data from the server
	public ArrayList<Customer> returnCustomerData() {

		client.startConnection("127.0.0.1", 4001);

		try {
			request = client.sendRequest("getAllCustomerData", null);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		client.stopConnection();

		return (ArrayList<Customer>) request;
	}

	// Request the finance data from the server
	public ArrayList<Finance> returnFinanceData() {
		client.startConnection("127.0.0.1", 4001);
		try {
			request = client.sendRequest("getAllFinanceData", null);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		client.stopConnection();
		return (ArrayList<Finance>) request;
	}

	// Request the product data from the server
	public ArrayList<Product> returnProductData() {

		client.startConnection("127.0.0.1", 4001);

		try {
			request = client.sendRequest("getAllProductData", null);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		client.stopConnection();

		return (ArrayList<Product>) request;

	}

	// Request the product order/purchase from the server
	public ArrayList<Purchase> returnPurchaseData() {

		client.startConnection("127.0.0.1", 4001);

		try {
			request = client.sendRequest("getAllPurchaseData", null);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		client.stopConnection();

		return (ArrayList<Purchase>) request;

	}

	// Request the store data from the server
	public ArrayList<Store> returnStoreData() {

		client.startConnection("127.0.0.1", 4001);

		try {
			request = client.sendRequest("getAllStoreData", null);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		client.stopConnection();

		return (ArrayList<Store>) request;

	}

	// Request the store product data from the server
	public ArrayList<StoreProduct> returnStoreProductData() {

		client.startConnection("127.0.0.1", 4001);

		try {
			request = client.sendRequest("getAllStoreProductData", null);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		client.stopConnection();

		return (ArrayList<StoreProduct>) request;

	}

	// Request the product ID data from the server
	public Product getProduct(int productID) {

		client.startConnection("127.0.0.1", 4001);

		String arg = String.valueOf(productID);

		try {
			request = client.sendRequest("getProductData", arg);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		client.stopConnection();

		return (Product) request;

	}

	// Request the customer ID data from the server
	public Customer getCustomer(int customerID) {

		client.startConnection("127.0.0.1", 4001);

		String arg = String.valueOf(customerID);

		try {
			request = client.sendRequest("getCustomerData", arg);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		client.stopConnection();

		return (Customer) request;

	}

	// Request the next purchase ID from the server
	public int getNextPurchaseID() {

		client.startConnection("127.0.0.1", 4001);

		try {
			request = client.sendRequest("getNextPurchaseID", null);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		client.stopConnection();

		return (int) request;

	}

	// Request the store product quantity from the server
	// Using storeID and productID
	public int getStoreProductQuantity(int storeID, int productID) {

		String arg = String.valueOf(storeID) + "," + String.valueOf(productID);

		client.startConnection("127.0.0.1", 4001);

		try {
			request = client.sendRequest("getStoreProductQuantity", arg);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		client.stopConnection();

		return (int) request;

	}

	// Update the store product quantity from the server
	// Using storeID and productID and newQuantity
	public void updateStoreProductQuantity(int storeID, int productID, int newQuantity) {

		String arg = String.valueOf(storeID) + "," + String.valueOf(productID) + "," + String.valueOf(newQuantity);

		client.startConnection("127.0.0.1", 4001);

		try {
			request = client.sendRequest("updateStoreProductQuantity", arg);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		client.stopConnection();

	}

	// Add a new purchase from the server using all data
	public void addNewPurchase(int purchaseStoreID, int purchaseID, int purchaseCustomerID, int purchaseProductID,
			int purchaseQuantity, long purchaseTotal, PurchasePaymentMethod purchasePaymentMethod) {

		String arg = purchaseStoreID + "," + purchaseID + "," + purchaseCustomerID + "," + purchaseProductID + ","
				+ purchaseQuantity + "," + purchaseTotal + "," + purchasePaymentMethod.name();

		client.startConnection("127.0.0.1", 4001);

		try {
			request = client.sendRequest("addNewPurchase", arg);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		client.stopConnection();

	}

	// Add a new purchase from the service by passing in a finance object from the
	// server
	public void addNewFinance(Finance newFinance) {

		String arg = newFinance.getFinanceCustomerID() + "," + newFinance.getFinancePurchaseID() + ","
				+ newFinance.getFinanceAmount() + "," + newFinance.getFinanceDuration();

		client.startConnection("127.0.0.1", 4001);

		try {
			request = client.sendRequest("addNewFinance", arg);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		client.stopConnection();

	}

	// Update the price from the server using productID and the new price
	public void updatePrice(int productId, double newPrice) {

		String arg = productId + "," + newPrice;

		client.startConnection("127.0.0.1", 4001);

		try {
			request = client.sendRequest("updateProductPrice", arg);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		client.stopConnection();

	}

}