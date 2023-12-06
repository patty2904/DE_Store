package Presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Application.Controller;
import Application.Customer;
import Application.Finance;
import Application.Product;
import Application.Purchase; // Use Purchase instead of Customer
import Application.Store;
import Data.DE_STORE_Database;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PurchaseView extends JFrame {

	private JTable purchaseTable;
	private DefaultTableModel tableModel;
	private StoreProductView storeProductView;

	private Controller cont = new Controller();

	JPanel buttonPanel;

	public PurchaseView() {

		setTitle("Purchase Details");
		setSize(800, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initialiseTable();
	}

	private void initialiseTable() {
		String[] columnNames = { "Store ID", "Purchase ID", "Customer ID", "Product ID", "Quantity", "Total",
				"Payment Method" };

		tableModel = new DefaultTableModel(columnNames, 0);

		ArrayList<Purchase> purchases = cont.returnPurchaseData();
		for (Purchase purchase : purchases) {
			Object[] row = new Object[] { purchase.getPurchaseStoreID(), purchase.getPurchaseID(),
					purchase.getPurchaseCustomerID(), purchase.getPurchaseProductID(), purchase.getPurchaseQuantity(),
					purchase.getPurchaseTotal(), purchase.getPaymentMethod().toString() };
			tableModel.addRow(row);
		}

		purchaseTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(purchaseTable);

		// Display
		JPanel mainPanel = new JPanel(new BorderLayout());

		mainPanel.add(scrollPane, BorderLayout.CENTER);

		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		// Process an order
		JButton processCustomerOrder = new JButton("Process Customer Order");
		buttonPanel.add(processCustomerOrder);

		processCustomerOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processOrder();
			}
		});

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);
	}

	// Add a new customer order (on behalf of the customer, for example)
	public void processOrder() {
		// Create a drop down list for storeID, customerID, and productID
		// A user shouldnt be able to type in any of these if they dont exist
		JComboBox<Integer> storeIDComboBox = new JComboBox<>();
		JComboBox<Integer> customerIDComboBox = new JComboBox<>();
		JComboBox<Integer> productIDComboBox = new JComboBox<>();
		JTextField quantityField = new JTextField(10);
		JComboBox<String> paymentMethodComboBox = new JComboBox<>(
				new String[] { "CREDIT_CARD", "DEBIT_CARD", "FINANCE", "NONE" });

		// Populate the comboboxes with data from the database via the server
		for (Store store : cont.returnStoreData()) {
			//Store 1 cant be selected as it is the central store
			if (store.getStoreID() != 1) { 
				storeIDComboBox.addItem(store.getStoreID());
			}
		}
		for (Customer customer : cont.returnCustomerData()) {
			customerIDComboBox.addItem(customer.getCustomerID());
		}
		for (Product product : cont.returnProductData()) {
			productIDComboBox.addItem(product.getProductID());
		}

		//Populate (and allow for user input for the other fields)
		JPanel panel = new JPanel();
		panel.add(new JLabel("Store ID:"));
		panel.add(storeIDComboBox);
		panel.add(new JLabel("Customer ID"));
		panel.add(customerIDComboBox);
		panel.add(new JLabel("Product ID"));
		panel.add(productIDComboBox);
		//Spacer
		panel.add(Box.createHorizontalStrut(15)); 
		panel.add(new JLabel("Quantity: "));
		panel.add(quantityField);
		panel.add(new JLabel("Payment Method"));
		panel.add(paymentMethodComboBox);

		int result = JOptionPane.showConfirmDialog(null, panel, "Enter New Order Details",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			try {
				// Get selected items from drop down (combo box)
				int storeID = (int) storeIDComboBox.getSelectedItem();
				int customerID = (int) customerIDComboBox.getSelectedItem();
				int productID = (int) productIDComboBox.getSelectedItem();
				int quantity = Integer.parseInt(quantityField.getText());

				//Cannot order from central inventory
				if (storeID == 1) {
					JOptionPane.showMessageDialog(this, "You cannot order from the central inventory.", "Order Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				//Ensure that the store, customer and product do exist from the database
				if (!DE_STORE_Database.doesStoreExist(storeID)) {
					JOptionPane.showMessageDialog(this, "Invalid Store ID", "Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!DE_STORE_Database.doesCustomerExist(customerID)) {
					JOptionPane.showMessageDialog(this, "Invalid Customer ID", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!DE_STORE_Database.doesProductExist(productID)) {
					JOptionPane.showMessageDialog(this, "Invalid Product ID", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				//Changing the price based on the loyalty card and product offers
				
				// Get customer and product data
				Customer customer = cont.getCustomer(customerID);
				Product product = cont.getProduct(productID);

				// Calculate discount based on loyalty card (tier)
				//All get free delivery
				//5% off for red
				//10% off for blue
				//15% off for purple
				double discount = 0;
				switch (customer.getLoyaltyCard()) {
				case RED:
					discount = 0.05;
					break;
				case BLUE:
					discount = 0.10;
					break;
				case PURPLE:
					discount = 0.15;
					break;
				default:
					break;
				}

				//Change the total
				long price = product.getProductPrice();
				long total = price * quantity;

				// Apply discount
				total -= total * discount;

				// Apply free delivery discount (all loyalty cards get this)
				// delivery is 3.50 normally
				// Based on loyalty card and product offer
				if (customer.getLoyaltyCard() != Customer.CustomerLoyaltyCard.NONE
						|| product.getOffer() == Product.ProductOffer.FREE_DELIVERY) {
					total -= 3.50;
				}

				// Apply product offer
				//Remove the price of one item
				if (product.getOffer() == Product.ProductOffer.BUY_ONE_GET_ONE_FREE && quantity >= 2) {
					total -= price;
				} else if (product.getOffer() == Product.ProductOffer.THREE_FOR_TWO && quantity >= 3) {
					total -= price;
				}

				String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();

				// Convert payment method to enum
				Purchase.PurchasePaymentMethod paymentMethodEnum = Purchase.PurchasePaymentMethod
						.valueOf(paymentMethod);

				//New purchase object (used below)
				Purchase newPurchase = new Purchase();
				newPurchase.setPurchaseStoreID(storeID); // Set store ID
				newPurchase.setPurchaseID(cont.getNextPurchaseID());
				newPurchase.setPurchaseCustomerID(customerID);
				newPurchase.setPurchaseProductID(productID);
				newPurchase.setPurchaseQuantity(quantity);
				newPurchase.setPurchaseTotal(total);
				newPurchase.setPaymentMethod(paymentMethodEnum);

				//Ensure there is enough stock by getting the storeID and productID
				// form the storeproduct table of that id
				// via server
				int currentStoreQuantity = cont.getStoreProductQuantity(storeID, productID);
				if (currentStoreQuantity < quantity) {
					JOptionPane.showMessageDialog(this, "Not enough stock in the selected store.", "Stock Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				//Update quantity of store product via  server
				cont.updateStoreProductQuantity(storeID, productID, currentStoreQuantity - quantity);

				//Update database with new purchase 
				DE_STORE_Database.addNewPurchase(newPurchase);

				//Check for low stock and display a message 
				if (storeProductView != null) {
					storeProductView.refreshTable();
					storeProductView.checkForLowStock();
				}

				// If payment method is finance
				if (paymentMethodEnum == Purchase.PurchasePaymentMethod.FINANCE) {
					// Prompt user for finance duration
					String durationStr = JOptionPane.showInputDialog(PurchaseView.this,
							"Enter the finance duration in months:", "Finance Duration", JOptionPane.PLAIN_MESSAGE);

					// validate duration
					int duration = 0;
					if (durationStr != null && !durationStr.trim().isEmpty()) {
						try {
							duration = Integer.parseInt(durationStr);
						} catch (NumberFormatException ex) {
							JOptionPane.showMessageDialog(PurchaseView.this,
									"Invalid duration number. Please enter a numeric value.", "Input Error",
									JOptionPane.ERROR_MESSAGE);
							return; 
						}
					}

					if (duration > 0) {
						Finance newFinance = new Finance();
						newFinance.setFinanceCustomerID(customerID);
						newFinance.setFinancePurchaseID(newPurchase.getPurchaseID());
						newFinance.setFinanceAmount(total);
						newFinance.setFinanceDuration(duration);

						// Add the new finance record via server
						cont.addNewFinance(newFinance);

					}
				}

				// Refresh the table model with the new purchase data (updates in UI)
				tableModel.addRow(new Object[] { newPurchase.getPurchaseStoreID(), newPurchase.getPurchaseID(),
						newPurchase.getPurchaseCustomerID(), newPurchase.getPurchaseProductID(),
						newPurchase.getPurchaseQuantity(), newPurchase.getPurchaseTotal(),
						newPurchase.getPaymentMethod().toString() });
				
				tableModel.fireTableDataChanged();
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Invalid input.", "Input Error", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error adding new purchase: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}