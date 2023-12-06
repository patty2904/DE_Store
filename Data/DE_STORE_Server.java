package Data;

import java.net.*;
import java.util.ArrayList;

import Application.*;

import java.io.*;

public class DE_STORE_Server {
	private static ServerSocket serverSocket;
	static DE_STORE_Database db = new DE_STORE_Database();

	public static void main(String[] args) {
		DE_STORE_Server server = new DE_STORE_Server();
		server.start(4001);
	}

	// Start server on specified port
	public static void start(int port) {
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				new ClientHandler(serverSocket.accept()).start();
			}
		} catch (IOException err) {
			err.printStackTrace();
		}
	}

	public static class ClientHandler extends Thread {
		private Socket clientSocket;

		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		public void run() {
			try (DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
					ObjectOutputStream dataOutputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {

				System.out.println("Connected from the clientsocket: " + clientSocket);

				String message = dataInputStream.readUTF();
				handleClientRequest(message, dataInputStream, dataOutputStream);

				dataOutputStream.flush();

			} catch (EOFException e) {
		        System.out.println("Client disconnected unexpectedly: " + e.getMessage());
		    } catch (Exception ex) {
		        ex.printStackTrace();
		    }
		}

		// Handle client requests based on message type
		private void handleClientRequest(String message, DataInputStream dataInputStream,
				ObjectOutputStream dataOutputStream) throws IOException {
			String arg;
			String[] splitArg;
			switch (message) {
			case "getAllProductData":
				ArrayList<Product> allProductData = db.getAllProductData();
				dataOutputStream.writeObject(allProductData);
				break;
			case "getAllCustomerData":
				ArrayList<Customer> allCustomerData = db.getAllCustomerData();
				dataOutputStream.writeObject(allCustomerData);
				break;
			case "getAllFinanceData":
				ArrayList<Finance> allFinanceData = db.getAllFinanceData();
				dataOutputStream.writeObject(allFinanceData);
				break;
			case "getAllPurchaseData":
				ArrayList<Purchase> allPurchaseData = db.getAllPurchaseData();
				dataOutputStream.writeObject(allPurchaseData);
				break;
			case "getAllStoreData":
				ArrayList<Store> allStoreData = db.getAllStoreData();
				dataOutputStream.writeObject(allStoreData);
				break;
			case "getAllStoreProductData":
				ArrayList<StoreProduct> allStoreProductData = db.getAllStoreProductData();
				dataOutputStream.writeObject(allStoreProductData);
				break;
			case "getCustomerData":
				arg = dataInputStream.readUTF();
				Customer customer = db.getCustomer(Integer.parseInt(arg));
				dataOutputStream.writeObject(customer);
				break;
			case "getProductData":
				arg = dataInputStream.readUTF();
				Product product = db.getProduct(Integer.parseInt(arg));
				dataOutputStream.writeObject(product);
				break;
			case "getNextPurchaseID":
				int nextPurchaseID = db.getNextPurchaseID();
				dataOutputStream.writeObject(nextPurchaseID);
				break;
			case "getStoreProductQuantity":
				arg = dataInputStream.readUTF();
				splitArg = arg.split(",");
				int quantity = db.getStoreProductQuantity(Integer.parseInt(splitArg[0]), Integer.parseInt(splitArg[1]));
				dataOutputStream.writeObject(quantity);
				break;
			case "updateStoreProductQuantity":
				arg = dataInputStream.readUTF();
				splitArg = arg.split(",");
				db.updateStoreProductQuantity(Integer.parseInt(splitArg[0]), Integer.parseInt(splitArg[1]),
						Integer.parseInt(splitArg[2]));
				dataOutputStream.writeObject(null);
				break;
			case "addNewFinance":
				arg = dataInputStream.readUTF();
				splitArg = arg.split(",");
				db.addNewFinance(Integer.parseInt(splitArg[0]), Integer.parseInt(splitArg[1]),
						Long.parseLong(splitArg[2]), Integer.parseInt(splitArg[3]));
				dataOutputStream.writeObject(null);
			case "updateProductPrice":
				arg = dataInputStream.readUTF();
				splitArg = arg.split(",");
				db.updateProductPrice(Integer.parseInt(splitArg[0]), Double.parseDouble(splitArg[1]));
				dataOutputStream.writeObject(null);
			default:
				break;
			}
		}
	}
}