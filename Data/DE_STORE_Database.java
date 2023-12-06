package Data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

import Application.Customer;
import Application.Customer.CustomerLoyaltyCard;
import Application.Finance;
import Application.Product;
import Application.Product.ProductOffer;
import Application.Purchase;
import Application.Store;
import Application.StoreProduct;

public class DE_STORE_Database {

	private static Connection conn;

	// Open database connection
	public static void databaseConnectionOpen() throws IOException {
		// Try to connect to database
		try {
			Class.forName("org.mariadb.jdbc.Driver");

			conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/DE_STORE?user=root&password=");
		} catch (ClassNotFoundException cnf) {
			System.err.println("Could not load driver");
			System.err.println(cnf.getMessage());
			System.exit(-1);
		} catch (SQLException sqe) {
			System.err.println("Error performing SQL Update");
			System.err.println(sqe.getMessage());
			System.exit(-1);
		}
	}

	// Close database connection
	public static void databaseConnectionClose() {
		try {
			conn.close();
		} catch (SQLException sqe) {
			System.err.println("Error performing SQL Update");
			System.err.println(sqe.getMessage());
			System.exit(-1);
		}
	}

	// NOTE: Some queries used prepared statements and some use statements
	// This is on purpose, as the functionality didn't work with them all being one
	// type of statement
	/*
	 * 
	 * 
	 * CUSTOMER database queries (functions are self explanatory by the name) and
	 * further explanations exist where used
	 * 
	 */
	public static ArrayList<Customer> getAllCustomerData() {
		ArrayList<Customer> allCustomerData = new ArrayList<Customer>();
		try {
			// open connection
			databaseConnectionOpen();

			Statement stm;

			stm = conn.createStatement();

			String sql = "SELECT * FROM Customer";

			ResultSet rst = stm.executeQuery(sql);

			while (rst.next()) {
				int customerID = rst.getInt("CustomerID");
				String customerName = rst.getString("CustomerName");
				String loyaltyCardString = rst.getString("CustomerLoyaltyCard");

				Customer.CustomerLoyaltyCard loyaltyCard;
				try {
					loyaltyCard = Customer.CustomerLoyaltyCard.valueOf(loyaltyCardString.toUpperCase());
				} catch (IllegalArgumentException e) {
					loyaltyCard = Customer.CustomerLoyaltyCard.NONE;
				}

				Customer customer = new Customer(customerID, customerName, loyaltyCard);
				allCustomerData.add(customer);
			}
			// close connection
			databaseConnectionClose();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return allCustomerData;
	}

	public static void addNewCustomer(Customer customer) {
		try {
			databaseConnectionOpen();

			String sql = "INSERT INTO Customer (CustomerName, CustomerLoyaltyCard) VALUES (?, ?)";

			try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				pstmt.setString(1, customer.getCustomerName());
				pstmt.setString(2, customer.getLoyaltyCard().toString()); // Corrected index to 2

				int affectedRows = pstmt.executeUpdate();

				if (affectedRows > 0) {
					// Get the generated customer ID and set it to the customer object
					try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
						if (generatedKeys.next()) {
							customer.setCustomerID(generatedKeys.getInt(1));
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			databaseConnectionClose();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getNextCustomerID() {
		try {
			databaseConnectionOpen();
			String sql = "SELECT MAX(CustomerID) as maxID FROM Customer";
			try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
				if (rs.next()) {
					// Increment
					return rs.getInt("maxID") + 1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				// Handle exception
			}
			databaseConnectionClose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	public static boolean doesCustomerExist(int customerID) {
		try {
			databaseConnectionOpen();
			String sql = "SELECT COUNT(*) FROM Customer WHERE CustomerID = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, customerID);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next() && rs.getInt(1) > 0) {
					// Customer does exist
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			databaseConnectionClose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Customer doesn't exist
		return false;
	}

	public static Customer getCustomer(int customerID) throws IOException {
		Customer customer = null;
		try {
			databaseConnectionOpen();
			String sql = "SELECT * FROM Customer WHERE CustomerID = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, customerID);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					String customerName = rs.getString("CustomerName");
					Customer.CustomerLoyaltyCard loyaltyCard = Customer.CustomerLoyaltyCard
							.valueOf(rs.getString("CustomerLoyaltyCard").toUpperCase());
					customer = new Customer(customerID, customerName, loyaltyCard);
				}
			}
			databaseConnectionClose();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customer;
	}

	// For the customer loyalty card
	public static void updateLoyaltyCard(int customerID, CustomerLoyaltyCard loyaltyCard) {
		try {
			databaseConnectionOpen();

			String sql = "UPDATE Customer SET CustomerLoyaltyCard = ? WHERE CustomerID = ?";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, loyaltyCard.name());
				pstmt.setInt(2, customerID);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			databaseConnectionClose();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * FINANCE database queries
	 * 
	 */
	public static ArrayList<Finance> getAllFinanceData() {
		ArrayList<Finance> allFinanceData = new ArrayList<Finance>();
		try {
			databaseConnectionOpen();

			Statement stm;

			stm = conn.createStatement();

			String sql = "SELECT * FROM Finance";

			ResultSet rst = stm.executeQuery(sql);

			while (rst.next()) {
				int FinanceID = rst.getInt("FinanceID");
				int FinanceCustomerID = rst.getInt("FinanceCustomerID");
				int FinancePurchaseID = rst.getInt("FinancePurchaseID");
				long FinanceAmount = rst.getLong("FinanceAmount");
				int FinanceDuration = rst.getInt("FinanceDuration");

				Finance finance = new Finance(FinanceID, FinanceCustomerID, FinancePurchaseID, FinanceAmount,
						FinanceDuration);
				allFinanceData.add(finance);
			}
			databaseConnectionClose();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return allFinanceData;
	}

	public static void addNewFinance(int financeCustomerID, int financePurchaseID, long financeAmount,
			int financeDuration) {
		try {
			databaseConnectionOpen();

			String sql = "INSERT INTO Finance (FinanceCustomerID, FinancePurchaseID, FinanceAmount, FinanceDuration) VALUES (?, ?, ?, ?)";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, financeCustomerID);
				pstmt.setInt(2, financePurchaseID);
				pstmt.setLong(3, financeAmount);
				pstmt.setInt(4, financeDuration);

				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			databaseConnectionClose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * PRODUCT database queries
	 * 
	 */
	public static ArrayList<Product> getAllProductData() {
		ArrayList<Product> allProductData = new ArrayList<Product>();
		try {
			databaseConnectionOpen();

			Statement stm = conn.createStatement();
			String sql = "SELECT * FROM Product";

			ResultSet rst = stm.executeQuery(sql);

			while (rst.next()) {
				int productID = rst.getInt("ProductID");
				String productName = rst.getString("ProductName");
				int productPrice = rst.getInt("ProductPrice");
				String offerString = rst.getString("ProductOffer");

				Product.ProductOffer offer;
				try {
					offer = Product.ProductOffer.valueOf(offerString.toUpperCase());
				} catch (IllegalArgumentException e) {
					offer = Product.ProductOffer.NONE;
				}

				Product product = new Product(productID, productName, productPrice, offer);
				allProductData.add(product);
			}
			databaseConnectionClose();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return allProductData;
	}

	public static void updateProductPrice(int productId, double newPrice) {
		try {
			databaseConnectionOpen();

			String sql = "UPDATE Product SET ProductPrice = ? WHERE ProductID = ?";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setDouble(1, newPrice);
				pstmt.setInt(2, productId);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			databaseConnectionClose();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateProductOffer(int productId, ProductOffer newOffer) {
		try {
			databaseConnectionOpen();

			String sql = "UPDATE Product SET ProductOffer = ? WHERE ProductID = ?";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, newOffer.name());
				pstmt.setInt(2, productId);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			databaseConnectionClose();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getProductQuantity(int productId) {
		int quantity = 0;
		try {
			databaseConnectionOpen();
			String sql = "SELECT ProductQuantity FROM Product WHERE ProductID = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, productId);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					quantity = rs.getInt("ProductQuantity");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			databaseConnectionClose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quantity;
	}

	public static long getProductPrice(int productId) {
		long price = 0;
		try {
			databaseConnectionOpen();
			String sql = "SELECT ProductPrice FROM Product WHERE ProductID = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, productId);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					price = rs.getLong("ProductPrice");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			databaseConnectionClose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return price;
	}

	public static boolean doesProductExist(int productID) {
		try {
			databaseConnectionOpen();
			String sql = "SELECT COUNT(*) FROM Product WHERE ProductID = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, productID);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next() && rs.getInt(1) > 0) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			databaseConnectionClose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Product getProduct(int productID) throws IOException {
		Product product = null;
		try {
			databaseConnectionOpen();
			String sql = "SELECT * FROM Product WHERE ProductID = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, productID);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					String productName = rs.getString("ProductName");
					long productPrice = rs.getLong("ProductPrice");
					Product.ProductOffer offer = Product.ProductOffer
							.valueOf(rs.getString("ProductOffer").toUpperCase());
					product = new Product(productID, productName, productPrice, offer);
				}
			}
			databaseConnectionClose();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}

	/*
	 * 
	 * PURCHASE (order) database queries
	 * 
	 * 
	 */
	public static ArrayList<Purchase> getAllPurchaseData() {
		ArrayList<Purchase> allPurchaseData = new ArrayList<Purchase>();
		try {
			databaseConnectionOpen();

			Statement stm = conn.createStatement();
			String sql = "SELECT * FROM Purchase";

			ResultSet rst = stm.executeQuery(sql);

			while (rst.next()) {
				int purchaseID = rst.getInt("PurchaseID");
				int purchaseCustomerID = rst.getInt("PurchaseCustomerID");
				int purchaseProductID = rst.getInt("PurchaseProductID");
				int purchaseStoreID = rst.getInt("PurchaseStoreID");
				int purchaseQuantity = rst.getInt("PurchaseQuantity");
				long purchaseTotal = rst.getLong("PurchaseTotal");
				String paymentMethodString = rst.getString("PurchasePaymentMethod");

				Purchase.PurchasePaymentMethod paymentMethod;
				try {
					paymentMethod = Purchase.PurchasePaymentMethod.valueOf(paymentMethodString.toUpperCase());
				} catch (IllegalArgumentException e) {
					paymentMethod = Purchase.PurchasePaymentMethod.CREDIT_CARD; // Assuming a default
				}

				Purchase purchase = new Purchase(purchaseID, purchaseCustomerID, purchaseProductID, purchaseStoreID,
						purchaseQuantity, purchaseTotal, paymentMethod);
				allPurchaseData.add(purchase);
			}
			databaseConnectionClose();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return allPurchaseData;
	}

	public static void addNewPurchase(Purchase newPurchase) {
		try {
			databaseConnectionOpen();
			String sql = "INSERT INTO Purchase (PurchaseStoreID, PurchaseCustomerID, PurchaseProductID, PurchaseQuantity, PurchaseTotal, PurchasePaymentMethod) VALUES (?, ?, ?, ?, ?, ?)";

			try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				// Set values from the newPurchase object
				pstmt.setInt(1, newPurchase.getPurchaseStoreID());
				pstmt.setInt(2, newPurchase.getPurchaseCustomerID());
				pstmt.setInt(3, newPurchase.getPurchaseProductID());
				pstmt.setInt(4, newPurchase.getPurchaseQuantity());
				pstmt.setLong(5, newPurchase.getPurchaseTotal());
				pstmt.setString(6, newPurchase.getPaymentMethod().toString());

				int affectedRows = pstmt.executeUpdate();

				if (affectedRows > 0) {
					// Get the generated purchase ID and set it to the newPurchase object
					try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
						if (generatedKeys.next()) {
							newPurchase.setPurchaseID(generatedKeys.getInt(1));
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			databaseConnectionClose();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getNextPurchaseID() {
		// Default to 1 if there are no entries in the table
		int nextID = 1;
		try {
			databaseConnectionOpen();

			String sql = "SELECT MAX(PurchaseID) FROM Purchase";
			try (Statement stm = conn.createStatement(); ResultSet rs = stm.executeQuery(sql)) {
				if (rs.next()) {
					int highestID = rs.getInt(1);
					nextID = highestID + 1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				// Handle exception
			}

			databaseConnectionClose();
		} catch (Exception e) {
			e.printStackTrace();
			// Handle other exceptions
		}
		return nextID;
	}

	/*
	 * 
	 * STORE database queries
	 * 
	 */
	public static ArrayList<Store> getAllStoreData() {
		ArrayList<Store> allStoreData = new ArrayList<Store>();
		try {
			databaseConnectionOpen();

			Statement stm = conn.createStatement();
			String sql = "SELECT * FROM Store";

			ResultSet rst = stm.executeQuery(sql);

			while (rst.next()) {
				int storeID = rst.getInt("StoreID");
				String storeName = rst.getString("StoreName");

				Store store = new Store(storeID, storeName);
				allStoreData.add(store);
			}
			databaseConnectionClose();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return allStoreData;
	}

	public static boolean doesStoreExist(int storeID) {
		try {
			databaseConnectionOpen();
			String sql = "SELECT COUNT(*) FROM Store WHERE StoreID = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, storeID);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next() && rs.getInt(1) > 0) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			databaseConnectionClose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * 
	 * 
	 * STORE PRODUCT database queries
	 * 
	 */

	public static int getStoreProductQuantity(int storeID, int productID) {
		int quantity = 0;
		try {
			databaseConnectionOpen();
			String sql = "SELECT Quantity FROM StoreProduct WHERE StoreID = ? AND ProductID = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, storeID);
				pstmt.setInt(2, productID);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					quantity = rs.getInt("Quantity");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			databaseConnectionClose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quantity;
	}

	public static ArrayList<StoreProduct> getAllStoreProductData() {
		ArrayList<StoreProduct> allStoreProductData = new ArrayList<StoreProduct>();
		try {
			databaseConnectionOpen();

			Statement stm = conn.createStatement();
			String sql = "SELECT * FROM StoreProduct";

			ResultSet rst = stm.executeQuery(sql);

			while (rst.next()) {
				int storeID = rst.getInt("StoreID");
				int productID = rst.getInt("ProductID");
				int quantity = rst.getInt("Quantity");

				StoreProduct storeProduct = new StoreProduct(storeID, productID, quantity);
				allStoreProductData.add(storeProduct);
			}
			databaseConnectionClose();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return allStoreProductData;
	}

	public static void updateStoreProductQuantity(int storeId, int productId, int newQuantity) {
		try {
			databaseConnectionOpen();
			String sql = "UPDATE StoreProduct SET Quantity = ? WHERE StoreID = ? AND ProductID = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, newQuantity);
				pstmt.setInt(2, storeId);
				pstmt.setInt(3, productId);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			databaseConnectionClose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}