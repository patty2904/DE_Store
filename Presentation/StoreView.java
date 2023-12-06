package Presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Application.Controller;
import Application.Store; 

import java.awt.*;
import java.util.ArrayList;

//Display store data as per previous views
public class StoreView extends JFrame {
    
    private JTable storeTable;
    private DefaultTableModel tableModel;
    
    private Controller cont = new Controller();
    
    public StoreView() {
        
        setTitle("Store Details");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initialiseTable();
    }

    private void initialiseTable() {
    	
    	try {
    		
    		String[] columnNames = {"Store ID", "Store Name"};

            tableModel = new DefaultTableModel(columnNames, 0);

            ArrayList<Store> stores = cont.returnStoreData();
            for (Store store : stores) {
                Object[] row = new Object[]{
                        store.getStoreID(),
                        store.getStoreName(),
                };
                tableModel.addRow(row);
            }

            storeTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(storeTable);

            JPanel mainPanel = new JPanel(new BorderLayout());

            mainPanel.add(scrollPane, BorderLayout.CENTER);

            add(mainPanel);
    		
    	} catch (Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
}