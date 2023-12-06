package Presentation;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import Application.Controller;

//This class shows the buttons that allow the admin to view the different
//data tables and allows manipulation 
public class AdminView extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JPanel panel;
	
	//New controller instance to access the functions from there
    Controller cont = new Controller();

	public AdminView() {
		initialise();
	}
	
	public void initialise(){
		//Sets the frame
	    frame = new JFrame();
	    frame.setTitle("Admin View");
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setSize(900, 500);
	    frame.setLocationRelativeTo(null);

	    //Sets a grid
	    panel = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();

	    // Home text label
	    JLabel homeText = new JLabel("Welcome to the Admin account!", SwingConstants.CENTER);
	    //Padding around the panel
	    gbc.insets = new Insets(20, 20, 0, 20); 
	    //layout
	    gbc.gridwidth = 2; 
	    gbc.gridx = 0; 
	    gbc.gridy = 0; 
	    panel.add(homeText, gbc);

	    // Usage information label
	    // Move to the next row
	    gbc.gridy++; 
	    gbc.insets = new Insets(20, 20, 0, 20);
	    JLabel usageInformation = new JLabel("What would you like to view?");
	    panel.add(usageInformation, gbc);

	    // Purchase 
	    gbc.gridy++; 
	    gbc.gridwidth = 2; 
	    gbc.gridx = 0; 
	    gbc.insets = new Insets(10, 20, 10, 20); 
	    JButton purchaseListButton = new JButton("Purchase List");
	    panel.add(purchaseListButton, gbc);
	    
	    //Customer
	    gbc.gridy++; 
	    gbc.gridwidth = 2; 
	    gbc.gridx = 0; 
	    gbc.insets = new Insets(10, 20, 10, 20); 
	    JButton customerListButton = new JButton("Customer List");
	    panel.add(customerListButton, gbc);
	    
	    //Finance
	    gbc.gridy++; 
	    gbc.gridwidth = 2; 
	    gbc.gridx = 0; 
	    gbc.insets = new Insets(10, 20, 10, 20); 
	    JButton financeListButton = new JButton("Finance List");
	    panel.add(financeListButton, gbc);
	    
	    //Product
	    gbc.gridy++; 
	    gbc.gridwidth = 2; 
	    gbc.gridx = 0;
	    gbc.insets = new Insets(10, 20, 10, 20); 
	    JButton productListButton = new JButton("Product List");
	    panel.add(productListButton, gbc);
	    
	    //Store
	    gbc.gridy++; 
	    gbc.gridwidth = 2; 
	    gbc.gridx = 0; 
	    gbc.insets = new Insets(10, 20, 10, 20); 
	    JButton storeListButton = new JButton("Store List");
	    panel.add(storeListButton, gbc);
	    
	    //Store Product
	    gbc.gridy++; 
	    gbc.gridwidth = 2; 
	    gbc.gridx = 0;
	    gbc.insets = new Insets(10, 20, 10, 20); 
	    JButton storeProductListButton = new JButton("Store Product List");
	    panel.add(storeProductListButton, gbc);
	    
	    //Email Box
	    gbc.gridy++;
	    gbc.gridwidth = 2; 
	    gbc.gridx = 0; 
	    gbc.insets = new Insets(10, 20, 10, 20); 
	    JButton emailBoxButton = new JButton("Email Box");
	    panel.add(emailBoxButton, gbc);


	    //Product button with navigation from controller
	    productListButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	           cont.navigateToProductView();
	        }
	    });
	    
	    //Customer button with navigation from controller
	    customerListButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	cont.navigateToCustomerView();
	        }
	    });
	    
	   //Finance button with navigation from controller
	    financeListButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	cont.navigateToFinanceView();
	        }
	    });
	    
	  //Purchase button with navigation from controller
	    purchaseListButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	cont.navigateToPurchaseView();
	        }
	    });
	    
	    //Store button with navigation from controller
	    storeListButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		cont.navigateToStoreView();

	    	}
	    });
	    
	    //Store product button with navigation from controller
	    storeProductListButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		cont.navigateToStoreProductView();

	    	}
	    });
	    
	    //Email box button with navigation
	    emailBoxButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		EmailBoxView emailBox = new EmailBoxView();
	    		emailBox.setVisible(true);

	    	}
	    });
	  
	    frame.add(panel, BorderLayout.CENTER); 
	}
	
	
	public void show() {
		this.frame.setVisible(true);
	}

}