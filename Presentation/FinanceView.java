package Presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Application.Controller;
import Application.Finance; // Use Finance instead of Customer
import Data.DE_STORE_Database;

import java.awt.*;
import java.util.ArrayList;

public class FinanceView extends JFrame {
    private JTable financeTable;
    private DefaultTableModel tableModel;
    
    private Controller cont = new Controller();

    public FinanceView() {
        setTitle("Finance Details");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initialiseTable();
    }

    private void initialiseTable() {
    	//Finance column headers
        String[] columnNames = {"Finance ID", "Customer ID", "Order ID", "Amount", "Duration"};

        tableModel = new DefaultTableModel(columnNames, 0);

        //Get finance data via server
        ArrayList<Finance> finances = cont.returnFinanceData();
        for (Finance finance : finances) {
            Object[] row = new Object[]{
                    finance.getFinanceID(),
                    finance.getFinanceCustomerID(),
                    finance.getFinancePurchaseID(),
                    finance.getFinanceAmount(),
                    finance.getFinanceDuration()
            };
            tableModel.addRow(row);
        }

        financeTable = new JTable(tableModel);
        //Display
        JScrollPane scrollPane = new JScrollPane(financeTable);

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        add(mainPanel);
    }

}