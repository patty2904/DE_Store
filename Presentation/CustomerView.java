package Presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Application.Controller;
import Application.Customer;
import Data.DE_STORE_Database;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CustomerView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable customerTable;
	private DefaultTableModel tableModel;

	private Controller cont = new Controller();

	public CustomerView() {
		setTitle("Customer List");
		setSize(800, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initialiseTable();
	}

	private void initialiseTable() {
		// Display columns
		String[] columnNames = { "Customer ID", "Customer Name", "Loyalty Card Type" };

		tableModel = new DefaultTableModel(columnNames, 0);

		// Get customer data from server
		ArrayList<Customer> customers = cont.returnCustomerData();
		for (Customer customer : customers) {
			Object[] row = new Object[] { customer.getCustomerID(), customer.getCustomerName(),
					customer.getLoyaltyCard() != null ? customer.getLoyaltyCard().toString() : "No Loyalty Card" };
			tableModel.addRow(row);
		}

		// Add customers to table

		customerTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(customerTable);

		// Main panel
		JPanel mainPanel = new JPanel(new BorderLayout());

		// Add the JScrollPane to centre
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		// Create a panel for buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		// Change Price
		JButton makeSpecialOffer = new JButton("Make Special Offer");
		buttonPanel.add(makeSpecialOffer);

		makeSpecialOffer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSpecialOffer();
			}
		});

		// Add new customer
		JButton makeNewCustomer = new JButton("Add New Customer");
		buttonPanel.add(makeNewCustomer);

		makeNewCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNewCustomer();
			}
		});

		// Add the button panel to the bottom of panel
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		// Add main panel to frame
		add(mainPanel);
	}

	// Set a special offer for the customers
	public void addSpecialOffer() {

		//Grab the correct row
		int row = customerTable.getSelectedRow();
		//Display error if there is no product selected
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "No product selected");
			return;
		}

		//Grab customer ID and current offer
		int customerID = (int) tableModel.getValueAt(row, 0);
		String currentLoyaltyOffer = (String) tableModel.getValueAt(row, 2);

		//Give a choice of loyalty offers
		String[] cards = { "RED", "BLUE", "PURPLE", "NONE" };
		String newLoyaltyOffer = (String) JOptionPane.showInputDialog(this, "Select new loyalty Card:",
				"Change Loyalty Card", JOptionPane.QUESTION_MESSAGE, null, cards, currentLoyaltyOffer);

		//Update the model
		try {
			tableModel.setValueAt(newLoyaltyOffer, row, 2); 
			//Update the database
			DE_STORE_Database.updateLoyaltyCard(customerID, Customer.CustomerLoyaltyCard.valueOf(newLoyaltyOffer));
			//Refresh the table
			tableModel.fireTableDataChanged(); // Refresh table
		} catch (Exception e) {
			//In case of error
			JOptionPane.showMessageDialog(this, "Error changing loyalty card: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	//Add a new customer
	public void addNewCustomer() {
		//Allow user to input name and loyalty card type (as the other data is autoamtic)
		JTextField nameField = new JTextField(20);
		JComboBox<String> loyaltyCardComboBox = new JComboBox<>(new String[] { "RED", "BLUE", "PURPLE", "NONE" });

		JPanel panel = new JPanel();
		panel.add(new JLabel("Customer Name:"));
		panel.add(nameField);
		panel.add(Box.createHorizontalStrut(15)); // a spacer
		panel.add(new JLabel("Loyalty Card Type:"));
		panel.add(loyaltyCardComboBox);

		//Display message
		int result = JOptionPane.showConfirmDialog(null, panel, "Enter New Customer Details",
				JOptionPane.OK_CANCEL_OPTION);
		
		//Try to get new customer
		if (result == JOptionPane.OK_OPTION) {
			try {
				//Get the name and loyalty card from input
				String customerName = nameField.getText();
				String loyaltyCard = (String) loyaltyCardComboBox.getSelectedItem();

				// Create new customer and set properties
				Customer newCustomer = new Customer();

				//Automatically get next ID from the database
				newCustomer.setCustomerID(DE_STORE_Database.getNextCustomerID()); 
				newCustomer.setCustomerName(customerName);
				newCustomer.setLoyaltyCard(Customer.CustomerLoyaltyCard.valueOf(loyaltyCard));

				//Add a new customer to the database
				DE_STORE_Database.addNewCustomer(newCustomer);

				// Add the new customer to the table model (updates UI)
				tableModel.addRow(new Object[] { newCustomer.getCustomerID(), newCustomer.getCustomerName(),
						newCustomer.getLoyaltyCard().toString() });

				//Refresh the table
				tableModel.fireTableDataChanged(); 
				//In case of error
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Invalid input", "Input Error",
						JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error adding new customer: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}