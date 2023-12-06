package Presentation;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

//Main view displayed when system is ran
public class MainView extends JFrame {

	private JFrame frame;
	private JPanel panel;
	private JButton button;

	public MainView() {
		initialise();
	}

	private void initialise() {
		frame = new JFrame();
		frame.setTitle("Home");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(800, 400);
		frame.setLocationRelativeTo(null);

		// Main panel
		panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Home text label
		JLabel homeText = new JLabel("Welcome to the DE_STORE system! Please log in to access the system.",
				SwingConstants.CENTER);
		gbc.insets = new Insets(20, 20, 0, 20);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(homeText, gbc);

		// Usage information label
		//Move to next row
		gbc.gridy++; 
		gbc.insets = new Insets(20, 20, 0, 20);
		JLabel usageInformation = new JLabel("Log in as the administrator to access the functionalities");
		panel.add(usageInformation, gbc);

		// Login label 
		gbc.gridy++; 
		gbc.gridwidth = 1; 
		gbc.insets = new Insets(20, 20, 5, 20); 
		JLabel loginLabel = new JLabel("Enter your login:");
		gbc.gridx = 0; 
		panel.add(loginLabel, gbc);

		//Add login label
		JTextField loginTextField = new JTextField(20);
		gbc.gridx = 1; 
		panel.add(loginTextField, gbc);

		// Password label
		gbc.gridy++; 
		gbc.gridx = 0; 
		JLabel passwordLabel = new JLabel("Enter your password:");
		panel.add(passwordLabel, gbc);
		
		//Add password label
		JPasswordField passwordTextField = new JPasswordField(20);
		gbc.gridx = 1; // Move to column 1 for text field
		panel.add(passwordTextField, gbc);

		// Save button
		gbc.gridy++; 
		gbc.gridwidth = 2; 
		gbc.gridx = 0; 
		gbc.insets = new Insets(20, 20, 20, 20); 
		button = new JButton("Login");
		panel.add(button, gbc);

		//button listener, asks for login and password
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getAccount(loginTextField, passwordTextField);
			}
		});

		// Add panel to the frame
		frame.add(panel, BorderLayout.CENTER); 
	}

	public void show() {
		this.frame.setVisible(true);
	}

	//If the login and password is correct, navigate to the admin view
	public void getAccount(JTextField textField, JPasswordField passwordField) {
		if ((textField.getText().equals("admin")) && (passwordField.getText().equals("password1"))) {
			JOptionPane.showMessageDialog(null, "You have successfuly logged in as the administrator");
			this.frame.setVisible(false);
			AdminView adminView = new AdminView();
			adminView.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Incorrect login or password");
		}
	}
}
