package Presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Application.Controller;
import Application.StoreProduct; // Use StoreProduct instead of Purchase

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StoreProductView extends JFrame {

	private JTable productTable;
	private DefaultTableModel tableModel;
	JPanel buttonPanel;

	private Controller cont = new Controller();

	ArrayList<StoreProduct> storeProducts = cont.returnStoreProductData();

	public StoreProductView() {

		setTitle("Store Product Details");
		setSize(800, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initialiseTable();
	}

	private void initialiseTable() {
		String[] columnNames = { "Store ID", "Product ID", "Quantity" };

		tableModel = new DefaultTableModel(columnNames, 0);

		for (StoreProduct storeProduct : storeProducts) {
			Object[] row = new Object[] { storeProduct.getStoreID(), storeProduct.getProductID(),
					storeProduct.getQuantity() };
			tableModel.addRow(row);
		}

		productTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(productTable);

		// Display
		JPanel mainPanel = new JPanel(new BorderLayout());

		mainPanel.add(scrollPane, BorderLayout.CENTER);

		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		// Order stock
		JButton orderMoreStock = new JButton("Order Stock");
		buttonPanel.add(orderMoreStock);

		orderMoreStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkStock();
			}
		});

		add(mainPanel);
	}

	// Check stock
	public void checkStock() {
		// Get the selected row
		int row = productTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "No product selected.");
			return;
		}

		// Get the appropriate data
		int storeId = (int) tableModel.getValueAt(row, 0);
		int productId = (int) tableModel.getValueAt(row, 1);
		int currentQuantity = (int) tableModel.getValueAt(row, 2);

		// If the stock is less than 6
		if (currentQuantity < 6) {
			// Create a pop-up message
			String additionalStockStr = JOptionPane
					.showInputDialog(
							this, "Enter amount of additional stock to order for Store ID " + storeId
									+ " and Product ID " + productId + ":",
							"Order More Stock", JOptionPane.QUESTION_MESSAGE);

			// Try to order more stock
			if (additionalStockStr != null && !additionalStockStr.isEmpty()) {
				try {
					int additionalStock = Integer.parseInt(additionalStockStr);
					if (additionalStock > 0) {
						// order stock function from below
						int newQuantity = orderStock(storeId, productId, currentQuantity, additionalStock);
						tableModel.setValueAt(newQuantity, row, 2);
						tableModel.fireTableDataChanged();
						JOptionPane.showMessageDialog(this, "More stock ordered for productID: " + productId);
					} else {
						JOptionPane.showMessageDialog(this, "Invalid amount entered");
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Invalid input format. Please enter a valid number.");
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Sufficient stock available, no need to order more.");
		}
	}

	// Order stock
	private int orderStock(int storeId, int productID, int currentQuantity, int additionalStock) {

		// Get current stock in store 1 (central) via server
		int currentStockInStore1 = cont.getStoreProductQuantity(1, productID);

		int newQuantity = 0;

		// For the central inventory
		if (storeId == 1) {
			newQuantity = currentQuantity + additionalStock;

			// update via server
			cont.updateStoreProductQuantity(storeId, productID, newQuantity);

			// For the other stores
		} else if (storeId == 2 || storeId == 3 || storeId == 4) {

			// Check if store 1 has enough stock to transfer
			if (currentStockInStore1 >= additionalStock) {
				// Update the new quantity for the target store
				newQuantity = currentQuantity + additionalStock;

				// Update via server
				cont.updateStoreProductQuantity(storeId, productID, newQuantity);

				cont.updateStoreProductQuantity(1, productID, currentStockInStore1 - additionalStock);

			} else {
				// Handle the case where there isn't enough stock in store 1
				JOptionPane.showMessageDialog(this, "Not enough stock in the central inventory (Store 1).",
						"Stock Error", JOptionPane.ERROR_MESSAGE);
			}

		}
		return newQuantity;
	}

	// Check for low stock
	public void checkForLowStock() {
		StringBuilder lowStockItems = new StringBuilder();

		// Same as email code
		for (StoreProduct storeProduct : storeProducts) {
			if (storeProduct.getQuantity() < 6) {
				lowStockItems.append(String.format("Store ID: %d, Product ID: %d, Current Quantity: %d\n",
						storeProduct.getStoreID(), storeProduct.getProductID(), storeProduct.getQuantity()));
			}
		}

		if (lowStockItems.length() > 0) {
			JOptionPane.showMessageDialog(this,
					"The following store products have low stock and need to be reordered:\n"
							+ lowStockItems.toString(),
					"Low Stock Alert", JOptionPane.WARNING_MESSAGE);
		}
	}

	// Refresh UI
	public void refreshTable() {
		tableModel.setRowCount(0);
		for (StoreProduct storeProduct : storeProducts) {
			Object[] row = new Object[] { storeProduct.getStoreID(), storeProduct.getProductID(),
					storeProduct.getQuantity() };
			tableModel.addRow(row);
		}
		tableModel.fireTableDataChanged();
	}

}