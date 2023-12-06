package Presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Application.Controller;
import Application.Product;
import Data.DE_STORE_Database;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ProductView extends JFrame {
	private JTable productTable;
	private DefaultTableModel tableModel;
	private JPanel buttonPanel;

	private Controller cont = new Controller();

	public ProductView() {
		setTitle("Product List");
		setSize(800, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initialiseTable();
	}

	private void initialiseTable() {
		String[] columnNames = { "Product ID", "Product Name", "Product Price", "Offer" };

		tableModel = new DefaultTableModel(columnNames, 0);

		ArrayList<Product> products = cont.returnProductData();
		for (Product product : products) {
			Object[] row = new Object[] { product.getProductID(), product.getProductName(), product.getProductPrice(),
					product.getOffer().toString() };
			tableModel.addRow(row);
		}

		productTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(productTable);

		// Display
		JPanel mainPanel = new JPanel(new BorderLayout());

		mainPanel.add(scrollPane, BorderLayout.CENTER);

		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		// Change Price Button
		JButton changePriceButton = new JButton("Change Price");
		buttonPanel.add(changePriceButton);

		// Change Offer Button
		JButton changeOfferButton = new JButton("Change Offer");
		buttonPanel.add(changeOfferButton);

		// Add action listeners to these buttons
		changePriceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeProductPrice();
			}
		});

		changeOfferButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeProductOffer();
			}
		});

		// Add the main panel to the frame
		add(mainPanel);
	}

	// Change price
	public void changeProductPrice() {
		// Grab selected row
		int row = productTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "No product selected.");
			return;
		}

		// Get the ID and Price
		int productId = (int) tableModel.getValueAt(row, 0);
		Object price = tableModel.getValueAt(row, 2);

		try {
			// Ensure correct price format
			double newPrice = Double.parseDouble(price.toString());
			if (newPrice < 0 || newPrice > 1000) {
				JOptionPane.showMessageDialog(this, "Price is invalid");
				return;
			}

			// Update price via server
			cont.updatePrice(productId, newPrice);

			// Refresh table
			tableModel.fireTableDataChanged();

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Invalid price format.");
		}
	}

	// Change product offer
	public void changeProductOffer() {
		// Grab selected row
		int row = productTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "No product selected.");
			return;
		}

		int productId = (int) tableModel.getValueAt(row, 0);
		String currentOffer = (String) tableModel.getValueAt(row, 3);

		// Allow for dropdown menu
		String[] offers = { "BUY_ONE_GET_ONE_FREE", "THREE_FOR_TWO", "FREE_DELIVERY", "NONE" };
		String newOffer = (String) JOptionPane.showInputDialog(this, "Select new offer:", "Change Offer",
				JOptionPane.QUESTION_MESSAGE, null, offers, currentOffer);

		// Update at selected row (offer row)
		try {
			tableModel.setValueAt(newOffer, row, 3);
			// Update databse
			DE_STORE_Database.updateProductOffer(productId, Product.ProductOffer.valueOf(newOffer));
			tableModel.fireTableDataChanged();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Unable to change offer: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
